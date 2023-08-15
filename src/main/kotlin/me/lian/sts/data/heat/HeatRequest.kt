package me.lian.sts.data.heat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataRequest

/**
 * Request the current heat from the STS.
 */
@Serializable
@SerialName("hitze")
internal data object HeatRequest : DataRequest