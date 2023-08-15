package me.lian.sts.data.register

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataRequest
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * A request to register a plugin.
 *
 * @property name The name of the plugin.
 * @property author The author of the plugin.
 * @property version The version of the plugin.
 * @property protocol The protocol version of the plugin.
 * @property description The description of the plugin.
 */
@Serializable
@SerialName("register")
internal data class RegisterRequest(
    @XmlElement(false) val name: String,
    @XmlElement(false) @SerialName("autor") val author: String,
    @XmlElement(false) val version: String,
    @XmlElement(false) @SerialName("protokoll") val protocol: Int,
    @XmlElement(false) @SerialName("text") val description: String,
) : DataRequest