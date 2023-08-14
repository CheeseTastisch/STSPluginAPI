package me.lian.sts.api

import kotlinx.coroutines.*
import me.lian.sts.data.*
import me.lian.sts.exception.*

/**
 * The api for communicating with the STS, in a suspending way.
 *
 * This api will suspend until a result is received from the server.
 *
 * This api uses the [BlockingSTS] under the hood and just converts to suspending calls.
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
 * @property dispatcher The dispatcher used for suspending calls.
 * @constructor Creates a new [BlockingSTS]
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class SuspendingSTS(
    val host: String,
    val name: String,
    val author: String,
    val version: String,
    val description: String,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    
    /**
     * The blocking sts used for blocking operations.
     */
    private val blockingSTS = BlockingSTS(host, name, author, version, description)

    /**
     * Checks if this class is initialized.
     */
    val isInitialized: Boolean
        get() = blockingSTS.isInitialized

    /**
     * Executes the call async and returns a completable future which will be completed when the call is completed.
     */
    private suspend fun <T> suspendingCall(call: () -> T) = withContext(dispatcher) { call() }

    /**
     * Creates the tcp connection, connects to the plugin api and registers the plugin.
     *
     * @return A completable future which will be completed when the plugin is registered.
     * @throws TcpException If the connection could not be established.
     * @throws RegisterException If the plugin could not be registered.
     */
    suspend fun connect() = suspendingCall { blockingSTS.connect() }

    /**
     * Disconnects from the plugin api.
     */
    suspend fun disconnect() = suspendingCall { blockingSTS.disconnect() }

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
    suspend fun getTime() = suspendingCall { blockingSTS.getTime() }

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
    suspend fun getTimeHandler() = suspendingCall { blockingSTS.getTimeHandler() }

    /**
     * Gets information about the simulator.
     *
     * @return The simulator information.
     *
     * @see SystemInformation
     */
    suspend fun getSystemInformation() = suspendingCall { blockingSTS.getSystemInformation() }

    /**
     * Gets a list of all platforms on the current facility and their connections.
     *
     * @return The list of platforms.
     *
     * @see Platform
     */
    suspend fun getPlatforms() = suspendingCall { blockingSTS.getPlatforms() }

    /**
     * Gets a list of all trains on the current facility.
     *
     * @return The list of trains.
     *
     * @see Train
     */
    suspend fun getTrains() = suspendingCall { blockingSTS.getTrains() }

    /**
     * Gets details about a specific train.
     *
     * @param trainId The id of the train.
     * @return The train details.
     *
     * @see TrainDetails
     */
    suspend fun getTrainDetails(trainId: Int) = suspendingCall { blockingSTS.getTrainDetails(trainId) }

    /**
     * Gets the timetable of a specific train.
     *
     * @param trainId The id of the train.
     * @return The timetable.
     *
     * @see Timetable
     */
    suspend fun getTimetable(trainId: Int) = suspendingCall { blockingSTS.getTimetable(trainId) }

    /**
     * Gets the facility layout.
     *
     * @return The facility layout.
     *
     * @see FacilityLayout
     */
    suspend fun getFacilityLayout() = suspendingCall { blockingSTS.getFacilityLayout() }

    /**
     * Subscribes to a specific event on a specific train.
     *
     * @param trainId The id of the train.
     * @param eventType The type of the event.
     * @param callback The callback which will be called when the event occurs.
     */
    suspend fun onTrainEvent(trainId: Int, eventType: EventType, callback: (Event) -> Unit) =
        suspendingCall { blockingSTS.onTrainEvent(trainId, eventType, callback) }

    /**
     * Subscribes to all events on a specific train.
     *
     * @param trainId The id of the train.
     * @param callback The callback which will be called when the event occurs.
     */
    suspend fun onTrainEvent(trainId: Int, callback: (Event) -> Unit) =
        suspendingCall { blockingSTS.onTrainEvent(trainId, callback) }

}