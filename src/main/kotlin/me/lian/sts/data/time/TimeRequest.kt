package me.lian.sts.data.time

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataRequest
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * A request to get the time of the simulator.
 *
 * @property senderTime The time the request was sent.
 */
@Serializable
@SerialName("simzeit")
internal data class TimeRequest(
    @XmlElement(false) @SerialName("sender") val senderTime: Long = System.currentTimeMillis(),
) : DataRequest