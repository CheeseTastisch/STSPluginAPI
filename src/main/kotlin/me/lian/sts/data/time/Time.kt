package me.lian.sts.data.time

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataResponse
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * The information about the time of the simulator.
 *
 * @property senderTime The time the request was sent (got from the request).
 * @property time The time of the simulator.
 */
@Serializable
@SerialName("simzeit")
data class Time(
    @XmlElement(false) @SerialName("sender") val senderTime: Long,
    @XmlElement(false) @SerialName("zeit") val time: Long,
) : DataResponse