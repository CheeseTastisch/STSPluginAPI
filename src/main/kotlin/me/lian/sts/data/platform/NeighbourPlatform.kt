package me.lian.sts.data.platform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * A [Platform] that is the neighbour of another [Platform].
 *
 * @property name The name of the neighbouring [Platform].
 *
 * @see Platform
 */
@Serializable
@SerialName("n")
data class NeighbourPlatform(
    @XmlElement(false) val name: String,
)