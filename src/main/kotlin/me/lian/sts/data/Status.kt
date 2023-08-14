package me.lian.sts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlValue

/**
 * A response to a connection to the plugin api or to a [RegisterRequest].
 *
 * @property code The status code of the response.
 * @property content The content of the response.
 */
@Serializable
@SerialName("status")
data class Status(
    @XmlElement(false) val code: Int,
    @XmlValue val content: String
) : DataResponse