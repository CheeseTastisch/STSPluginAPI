package me.lian.sts.api

import me.lian.sts.data.time.Time


/**
 * The [TimeHandler] is used to get the time in the simulation without needing to make a request every time.
 *
 * For this the [TimeHandler] time handler requires a single [Time] response and a [Long] value for the time
 * the value was received. After that the [TimeHandler] will calculate the time in the simulation.
 *
 * @property time The time in the simulation.
 * @property timeReceived The time the [time] was received.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class TimeHandler(
    private val time: Time,
    private val timeReceived: Long,
) {

    /**
     * The base time to calculate the time in the simulation.
     */
    private val baseTime by lazy { time.time + (timeReceived - time.senderTime) / 2 }

    /**
     * Gets the total milliseconds since 0:0:0 in the simulation.
     */
    fun getMillis() = baseTime + (System.currentTimeMillis() - timeReceived)

    /**
     * Gets the total seconds since 0:0:0 in the simulation.
     */
    fun getTotalSeconds() = getMillis() / 1000

    /**
     * Get the time as hours, minutes and seconds in the simulation.
     */
    fun getTime() = getTotalSeconds().let {
        val hours = it / 3600
        val minutes = (it % 3600) / 60
        val seconds = it % 60
        Triple(hours, minutes, seconds)
    }

    /**
     * Get the hours of the time in the simulation.
     */
    fun getHours() = getMillis() / 3600000

    /**
     * Get the minutes of the time in the simulation.
     */
    fun getMinutes() = (getMillis() / 60000) % 60

    /**
     * Get the seconds of the time in the simulation.
     */
    fun getSeconds() = (getMillis() / 1000) % 60

}