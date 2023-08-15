package me.lian.sts.data.platform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * A single [Platform] in the current facility.
 *
 * @property name The name of the [Platform].
 * @property stop Whether this [Platform] is a halt point.
 * @property neighbours The list of [NeighbourPlatform]s, which are platforms connected to this [Platform].
 *
 * @see NeighbourPlatform
 */
@Serializable
@SerialName("bahnsteig")
data class Platform(
    @XmlElement(false) val name: String,
    @XmlElement(false) @SerialName("haltepunkt") val stop: Boolean,
    val neighbours: List<NeighbourPlatform>,
)