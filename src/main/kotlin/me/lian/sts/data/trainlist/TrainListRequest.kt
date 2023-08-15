package me.lian.sts.data.trainlist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataRequest

/**
 * Request to get a list of all trains.
 */
@Serializable
@SerialName("zugliste")
internal data object TrainListRequest : DataRequest