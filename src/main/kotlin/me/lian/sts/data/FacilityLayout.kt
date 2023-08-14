package me.lian.sts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlDefault
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * Requests the track layout of the current facility.
 */
@Serializable
@SerialName("wege")
internal data object FacilityLayoutRequest : DataRequest

/**
 * The track layout of the current facility.
 *
 * @property shapes The shapes of the track, where shapes are parts of tracks, like signals, entry points, etc.
 * @property connections The connections between the shapes.
 */
@Serializable
@SerialName("wege")
data class FacilityLayout(
    @SerialName("shape") val shapes: List<Shape>,
    @SerialName("connector") val connections: List<Connection>,
) : DataResponse

/**
 * A shape is a part of a track, like a signal, entry point, etc.
 *
 * **The type is not documented, but it seems to be:**
 * - 2: Signals
 * - 3: Switches (pointing downwards)
 * - 4: Switches (pointing upwards)
 * - 5: Platforms
 * - 6: Entry points
 * - 7: Exit points
 * - 12: ??? (_often found with platforms, transition points and junctions, but no idea what it is_)
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
) : DataResponse

/**
 * A connection between two shapes.
 *
 * The connection can be defined by two ids, one id and one name or two names.
 * Usually the name is only used for platforms, because they don't have an id,
 * **but this is only an assumption and not documented**.
 *
 * @property id1 The id of the first shape.
 * @property id2 The id of the second shape.
 * @property name1 The name of the first shape.
 * @property name2 The name of the second shape.
 */
@Serializable
@SerialName("connector")
data class Connection(
    @XmlElement(false) @XmlDefault("") @SerialName("enr1") val id1: Int? = null,
    @XmlElement(false) @XmlDefault("") @SerialName("enr2") val id2: Int? = null,
    @XmlElement(false) @XmlDefault("") @SerialName("name1") val name1: String? = null,
    @XmlElement(false) @XmlDefault("") @SerialName("name2") val name2: String? = null,
) : DataResponse