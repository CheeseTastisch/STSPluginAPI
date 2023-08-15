package me.lian.sts.data.platform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataResponse

/**
 * A list of all [Platform]s in the current facility.
 *
 * @property platforms The list of [Platform]s.
 *
 * @see Platform
 */
@Serializable
@SerialName("bahnsteigliste")
data class PlatformList(
    val platforms: List<Platform>,
) : DataResponse
