package me.lian.sts.data.facility

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlDefault
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * A shape is a part of a track, like a signal, entry point, etc.
 *
 * The following types are known:
 * - 2: Signals
 * - 3: Switches (pointing downwards)
 * - 4: Switches (pointing upwards)
 * - 5: Platforms
 * - 6: Entry points
 * - 7: Exit points
 * - 12: Halt points
 *
 * @property type The type of the shape.
 * @property name The name of the shape.
 * @property id The id of the shape.
 */
@Serializable
@SerialName("shape")
data class Shape(
    @XmlElement(false) @SerialName("type") val type: Int,
    @XmlElement(false) val name: String,
    @XmlElement(false) @XmlDefault("") @SerialName("enr") val id: Int? = null,
)