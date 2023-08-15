package me.lian.sts.data.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataRequest
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * The request to listen to a specific [type] of [Event]s for a specific [trainId].
 *
 * @property trainId The identifier of the train to listen to.
 * @property type The type of [Event] to listen to.
 *
 * @see EventResponse
 * @see EventType
 * @see Event
 */
@Serializable
@SerialName("ereignis")
internal data class EventRequest(
    @XmlElement(false) @SerialName("zid") val trainId: Int,
    @XmlElement(false) @SerialName("art") val type: EventType
) : DataRequest