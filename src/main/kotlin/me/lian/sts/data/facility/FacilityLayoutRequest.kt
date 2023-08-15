package me.lian.sts.data.facility

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataRequest

/**
 * Requests the track layout of the current facility.
 */
@Serializable
@SerialName("wege")
internal data object FacilityLayoutRequest : DataRequest
