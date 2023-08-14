package me.lian.sts.api

import me.lian.sts.data.*
import me.lian.sts.exception.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * The api for communicating with the STS, with completable futures.
 *
 * This api will always return a completable future, which will be completed when a response is received.
 *
 * This api uses the [BlockingSTS] under the hood and just converts the blocking calls to completable futures.
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
 * @property threads The amount of threads used for async operations, default are 4.
 * @constructor Creates a new [BlockingSTS]
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class CompletableSTS(
    val host: String,
    val name: String,
    val author: String,
    val version: String,
    val description: String,
    val threads: Int = 4
) {

    /**
     * The thread pool used for async operations.
     */
    private var threadPool: ExecutorService? = null

    /**
     * The blocking sts used for blocking operations.
     */
    private val blockingSTS = BlockingSTS(host, name, author, version, description)

    /**
     * Checks if this class is initialized.
     */
    val isInitialized: Boolean
        get() = threadPool != null && blockingSTS.isInitialized

    /**
     * A save thread pool getter.
     *
     * @throws NotInitializedException If the api is not initialized yet.
     */
    private val pool: ExecutorService
        get() = threadPool ?: throw NotInitializedException

    /**
     * Executes the call async and returns a completable future which will be completed when the call is completed.
     */
    private fun <T> asyncCall(call: () -> T): CompletableFuture<T> = CompletableFuture.supplyAsync(call, pool)

    /**
     * Creates the tcp connection, connects to the plugin api and registers the plugin.
     *
     * In addition, this creates a new thread pool with the given amount of threads,
     * if there is currently no thread pool, either because the [connect] methods was
     * never called before or because the [shutdown] method was called.
     *
     * @return A completable future which will be completed when the plugin is registered.
     * @throws TcpException If the connection could not be established.
     * @throws RegisterException If the plugin could not be registered.
     */
    fun connect(): CompletableFuture<Unit> {
        if (threadPool == null) threadPool = Executors.newFixedThreadPool(threads)

        return asyncCall { blockingSTS.connect() }
    }

    /**
     * Disconnects from the plugin api,
     * after this is completed, the [shutdown] method should be called to shut down the thread pool.
     *
     * If the [shutdown] method is not called, the JVM will maybe not exit.
     */
    fun disconnect() = asyncCall { blockingSTS.disconnect() }

    /**
     * Shuts down the thread pool.
     *
     * @throws IllegalStateException If the [disconnect] method was not called before.
     */
    fun shutdown() {
        if (blockingSTS.isInitialized) throw IllegalStateException("The api is still initialized, please call disconnect first.")

        threadPool?.shutdown()
        threadPool = null
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
    fun getTime() = asyncCall { blockingSTS.getTime() }

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
    fun getTimeHandler() = asyncCall { blockingSTS.getTimeHandler() }

    /**
     * Gets information about the simulator.
     *
     * @return The simulator information.
     *
     * @see SystemInformation
     */
    fun getSystemInformation() = asyncCall { blockingSTS.getSystemInformation() }

    /**
     * Gets a list of all platforms on the current facility and their connections.
     *
     * @return The list of platforms.
     *
     * @see Platform
     */
    fun getPlatforms() = asyncCall { blockingSTS.getPlatforms() }

    /**
     * Gets a list of all trains on the current facility.
     *
     * @return The list of trains.
     *
     * @see Train
     */
    fun getTrains() = asyncCall { blockingSTS.getTrains() }

    /**
     * Gets details about a specific train.
     *
     * @param trainId The id of the train.
     * @return The train details.
     *
     * @see TrainDetails
     */
    fun getTrainDetails(trainId: Int) = asyncCall { blockingSTS.getTrainDetails(trainId) }

    /**
     * Gets the timetable of a specific train.
     *
     * @param trainId The id of the train.
     * @return The timetable.
     *
     * @see Timetable
     */
    fun getTimetable(trainId: Int) = asyncCall { blockingSTS.getTimetable(trainId) }

    /**
     * Gets the facility layout.
     *
     * @return The facility layout.
     *
     * @see FacilityLayout
     */
    fun getFacilityLayout() = asyncCall { blockingSTS.getFacilityLayout() }

    /**
     * Subscribes to a specific event on a specific train.
     *
     * @param trainId The id of the train.
     * @param eventType The type of the event.
     * @param callback The callback which will be called when the event occurs.
     */
    fun onTrainEvent(trainId: Int, eventType: EventType, callback: (Event) -> Unit) =
        pool.execute { blockingSTS.onTrainEvent(trainId, eventType, callback) }

    /**
     * Subscribes to all events on a specific train.
     *
     * @param trainId The id of the train.
     * @param callback The callback which will be called when the event occurs.
     */
    fun onTrainEvent(trainId: Int, callback: (Event) -> Unit) =
        pool.execute { blockingSTS.onTrainEvent(trainId, callback) }

}