package me.lian.sts.api

import me.lian.sts.data.*
import me.lian.sts.data.RegisterRequest
import me.lian.sts.data.TimeRequest
import me.lian.sts.tcp.TcpConnection
import me.lian.sts.exception.*

/**
 * The api for communicating with the STS, in a blocking way.
 *
 * This api will block the current thread until a response is received.
 *
 * All methods can throw
 * - a [NotRegisteredException] if the plugin is not registered,
 * - a [IllegalDataException] if the send data is not valid,
 * - a [XmlException] if some error occurred with XML or
 * - a [ServerException] if some other, not recoverable error on the server side occurred.
 *
 * To use the api, the [connect] method must be called first, otherwise a [NotInitializedException] will be thrown.
 *
 * After the [disconnect] method was called, the api can not be used anymore,
 * if used a [NotInitializedException] will be thrown,
 * however the [connect] method can be called again to reinitialize the api.
 *
 * @property host The host of the plugin api.
 * @property name The name of the plugin.
 * @property author The author of the plugin.
 * @property version The version of the plugin.
 * @property description The description of the plugin.
 * @constructor Creates a new [BlockingSTS]
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class BlockingSTS(
    val host: String,
    val name: String,
    val author: String,
    val version: String,
    val description: String
) {

    /**
     * The tcp connection used to communicate with the plugin api.
     */
    private var tcpConnection: TcpConnection? = null

    /**
     * A save tcp connection getter.
     *
     * @throws NotInitializedException If the api is not initialized yet.
     */
    private val connection: TcpConnection
        get() = tcpConnection ?: throw NotInitializedException

    /**
     * The time handler
     */
    private var timeHandler: TimeHandler? = null

    /**
     * Checks if this class is initialized.
     */
    val isInitialized: Boolean
        get() = tcpConnection != null

    /**
     * Creates the tcp connection, connects to the plugin api and registers the plugin.
     *
     * @throws TcpException If the connection could not be established.
     * @throws RegisterException If the plugin could not be registered.
     */
    fun connect() {
        tcpConnection = TcpConnection(host)
        tcpConnection!!.connect()
        tcpConnection!!.send(RegisterRequest(name, author, version, 1, description))

        val response = tcpConnection!!.receive()
        if (response !is Status) throw RegisterException("Expected status response, got ${response::class.simpleName}.")
        if (response.code == 450) throw XmlException(response.content)
        if (response.code == 500) throw ServerException(response.content)
        if (response.code != 220) throw RegisterException("Could not register plugin: ${response.content}")
    }

    /**
     * Disconnects from the plugin api.
     */
    fun disconnect() {
        tcpConnection?.disconnect()
        tcpConnection = null
    }

    /**
     * Request the time in game of the STS.
     *
     * This will always send a request to the plugin api.
     * Please use [getTimeHandler] to only send a request once.
     *
     * @return The time in game.
     *
     * @see Time
     * @see getTimeHandler
     */
    fun getTime(): Time {
        connection.send(TimeRequest())

        val response = connection.receive { it is Status || it is Time }

        return when (response) {
            is Status -> handleStatus(response)
            else -> response as Time
        }
    }

    /**
     * Gets a time handler which can be used to get the current time in the simulation.
     *
     * This will block the thead once, until the [TimeHandler] is created,
     * afterward the [TimeHandler] won't block the thread anymore.
     *
     * @return The time handler.
     *
     * @see TimeHandler
     * @see getTime
     */
    fun getTimeHandler(): TimeHandler {
        if (timeHandler == null) timeHandler = TimeHandler(getTime(), System.currentTimeMillis())
        return timeHandler!!
    }

    /**
     * Gets information about the simulator.
     *
     * @return The simulator information.
     *
     * @see SystemInformation
     */
    fun getSystemInformation(): SystemInformation {
        connection.send(SystemInformationRequest)

        val response = connection.receive { it is Status || it is SystemInformation }
        return when (response) {
            is Status -> handleStatus(response)
            else -> response as SystemInformation
        }
    }

    /**
     * Gets a list of all platforms on the current facility and their connections.
     *
     * @return The list of platforms.
     *
     * @see Platform
     */
    fun getPlatforms(): List<Platform> {
        connection.send(PlatformListRequest)

        val response = connection.receive { it is Status || it is PlatformList }
        return when (response) {
            is Status -> handleStatus(response)
            else -> (response as PlatformList).platforms
        }
    }

    /**
     * Gets a list of all trains on the current facility.
     *
     * @return The list of trains.
     *
     * @see Train
     */
    fun getTrains(): List<Train> {
        connection.send(TrainListRequest)

        val response = connection.receive { it is Status || it is TrainList }
        return when (response) {
            is Status -> handleStatus(response)
            else -> (response as TrainList).trains
        }
    }

    /**
     * Gets details about a specific train.
     *
     * @param trainId The id of the train.
     * @return The train details.
     *
     * @see TrainDetails
     */
    fun getTrainDetails(trainId: Int): TrainDetails {
        connection.send(TrainDetailsRequest(trainId))

        val response = connection.receive { it is Status || (it is TrainDetails && it.trainId == trainId) }
        return when (response) {
            is Status -> handleStatus(response)
            else -> response as TrainDetails
        }
    }

    /**
     * Gets the timetable of a specific train.
     *
     * @param trainId The id of the train.
     * @return The timetable.
     *
     * @see Timetable
     */
    fun getTimetable(trainId: Int): Timetable {
        connection.send(TimetableRequest(trainId))

        val response = connection.receive { it is Status || (it is TimetableResponse && it.trainId == trainId) }
        return when (response) {
            is Status -> handleStatus(response)
            else -> (response as TimetableResponse).toTimetable()
        }
    }

    /**
     * Gets the facility layout.
     *
     * @return The facility layout.
     *
     * @see FacilityLayout
     */
    fun getFacilityLayout(): FacilityLayout {
        connection.send(FacilityLayoutRequest)

        val response = connection.receive { it is Status || it is FacilityLayout }
        return when (response) {
            is Status -> handleStatus(response)
            else -> response as FacilityLayout
        }
    }

    /**
     * Subscribes to a specific event on a specific train.
     *
     * @param trainId The id of the train.
     * @param eventType The type of the event.
     * @param callback The callback which will be called when the event occurs.
     */
    fun onTrainEvent(trainId: Int, eventType: EventType, callback: (Event) -> Unit) {
        connection.send(EventRequest(trainId, eventType))
        connection.onReceive { if (it is Event && it.trainId == trainId && it.type == eventType) callback(it) }
    }

    /**
     * Subscribes to all events on a specific train.
     *
     * @param trainId The id of the train.
     * @param callback The callback which will be called when the event occurs.
     */
    fun onTrainEvent(trainId: Int, callback: (Event) -> Unit) {
        EventType.entries.forEach { connection.send(EventRequest(trainId, it)) }
        connection.onReceive { if (it is Event && it.trainId == trainId) callback(it) }
    }

    /**
     * Handles a [Status] response appropriately.
     *
     * @throws NotRegisteredException If the plugin is not registered.
     * @throws IllegalDataException If the data is not valid.
     * @throws InvalidTrainIdException If the train id is not valid.
     * @throws UnknownTrainIdException If the train id is unknown.
     * @throws UnknownEventTypeException If the event type is unknown.
     * @throws XmlException If some error occurred with XML.
     * @throws ServerException If some other, not recoverable error on the server side occurred.
     */
    private fun handleStatus(status: Status): Nothing {
        when (status.code) {
            300 -> throw NotRegisteredException
            400 -> throw IllegalDataException(status.content)
            401 -> throw InvalidTrainIdException(status.content.toInt())
            402 -> throw UnknownTrainIdException(status.content.toInt())
            403 -> throw UnknownEventTypeException(status.content)
            450 -> throw XmlException(status.content)
            500 -> throw ServerException(status.content)
            else -> throw IllegalStateException("Unknown status code: ${status.code}")
        }
    }

}