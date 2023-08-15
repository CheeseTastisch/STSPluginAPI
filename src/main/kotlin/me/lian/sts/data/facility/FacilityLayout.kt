package me.lian.sts.data.facility

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataResponse

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