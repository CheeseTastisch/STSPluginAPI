package me.lian.sts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * A request to get the time of the simulator.
 *
 * @property senderTime The time the request was sent.
 */
@Serializable
@SerialName("simzeit")
internal data class TimeRequest(
    @XmlElement(false)@SerialName("sender") val senderTime: Long = System.currentTimeMillis(),
) : DataRequest

/**
 * The information about the time of the simulator.
 *
 * @property senderTime The time the request was sent (got from the request).
 * @property time The time of the simulator.
 */
@Serializable
@SerialName("simzeit")
data class Time(
    @XmlElement(false)@SerialName("sender") val senderTime: Long,
    @XmlElement(false)@SerialName("zeit") val time: Long,
) : DataResponse