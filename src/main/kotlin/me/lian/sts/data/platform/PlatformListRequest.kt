package me.lian.sts.data.platform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataRequest

/**
 * The request to get all [Platform]s in the current facility.
 *
 * @see PlatformList
 */
@Serializable
@SerialName("bahnsteigliste")
internal data object PlatformListRequest : DataRequest
