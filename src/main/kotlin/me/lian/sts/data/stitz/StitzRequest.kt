package me.lian.sts.data.stitz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataRequest

/**
 * Request information about the StiTz.
 *
 * The StiTz is the Stellwerksim internal telephone network (_Stellwerksim internes Telefonnetz_).
 */
@Serializable
@SerialName("stitz")
internal data object StiTzRequest : DataRequest