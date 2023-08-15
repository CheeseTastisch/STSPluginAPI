package me.lian.sts.data.system

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataRequest

/**
 * A request to get information about the current system.
 */
@Serializable
@SerialName("anlageninfo")
internal data object SystemInformationRequest : DataRequest