package me.lian.sts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * The request to get all [Platform]s in the current facility.
 *
 * @see PlatformList
 */
@Serializable
@SerialName("bahnsteigliste")
internal data object PlatformListRequest : DataRequest

/**
 * A list of all [Platform]s in the current facility.
 *
 * @property platforms The list of [Platform]s.
 *
 * @see Platform
 */
@Serializable
@SerialName("bahnsteigliste")
data class PlatformList(val platforms: List<Platform>) : DataResponse

/**
 * A single [Platform] in the current facility.
 *
 * @property name The name of the [Platform].
 * @property stop Whether trains should stop at this [Platform].
 * @property neighbours The list of [NeighbourPlatform]s, which are platforms connected to this [Platform].
 *
 * @see NeighbourPlatform
 */
@Serializable
@SerialName("bahnsteig")
data class Platform(
    @XmlElement(false) val name: String,
    @XmlElement(false) @SerialName("haltepunkt") val stop: Boolean,
    val neighbours: List<NeighbourPlatform>
)

/**
 * A [Platform] that is the neighbour of another [Platform].
 *
 * @property name The name of the neighbouring [Platform].
 *
 * @see Platform
 */
@Serializable
@SerialName("n")
data class NeighbourPlatform(@XmlElement(false) val name: String)

