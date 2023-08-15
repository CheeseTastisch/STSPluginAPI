package me.lian.sts.data.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataResponse
import nl.adaptivity.xmlutil.serialization.XmlDefault
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * A [Event] that occurred.
 *
 * This is the raw response witch will be parsed into a [Event] by [toEvent].
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
 * @see Event
 */
@Serializable
@SerialName("ereignis")
internal data class EventResponse(
    @XmlElement(false) @SerialName("zid") val trainId: Int,
    @XmlElement(false) @SerialName("art") val type: String,
    @XmlElement(false) val name: String,
    @XmlElement(false) @SerialName("verspaetung") val delay: Int,
    @XmlElement(false) @XmlDefault("") @SerialName("gleis") val track: String? = null,
    @XmlElement(false) @XmlDefault("") @SerialName("plangleis") val plannedTrack: String? = null,
    @XmlElement(false) @SerialName("von") val source: String,
    @XmlElement(false) @SerialName("nach") val destination: String,
    @XmlElement(false) @SerialName("sichtbar") val visible: Boolean,
    @XmlElement(false) @SerialName("amgleis") val atPlatform: Boolean,
) : DataResponse {

    /**
     * Converts the raw [EventResponse] into a well-formed [Event].
     */
    fun toEvent() = Event(
        trainId = trainId,
        type = EventType.entries.first { it.identifier == type },
        name = name,
        delay = delay,
        track = track,
        plannedTrack = plannedTrack,
        source = source,
        destination = destination,
        visible = visible,
        atPlatform = atPlatform,
    )

}