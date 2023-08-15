package me.lian.sts.data.event

import me.lian.sts.data.DataResponse

/**
 * A [Event] that occurred.
 *
 * @property trainId The identifier of the train that performed the [Event].
 * @property type The type of [Event] that occurred.
 * @property name The name of the train that performed the [Event].
 * @property delay The delay of the train that performed the [Event].
 * @property track The track the train that performed the [Event] is currently on or will be on next
 * or null if the train is heading to exit.
 * @property plannedTrack The track the train that performed the [Event] should currently be on or
 * should be on next if the track hadn't been changed or null if the train is heading to
 * @property source The entrance the train that performed the [Event] entered the system from
 * or will enter the system from.
 * @property destination The exit the train that performed the [Event] will exit the system to.
 * @property visible Whether the train that performed the [Event] is visible or not in the system yet.
 * @property atPlatform Whether the train that performed the [Event] is currently at a platform or
 * on open track.
 *
 * @see EventType
 */
data class Event(
    val trainId: Int,
    val type: EventType,
    val name: String,
    val delay: Int,
    val track: String? = null,
    val plannedTrack: String? = null,
    val source: String,
    val destination: String,
    val visible: Boolean,
    val atPlatform: Boolean,
) : DataResponse
