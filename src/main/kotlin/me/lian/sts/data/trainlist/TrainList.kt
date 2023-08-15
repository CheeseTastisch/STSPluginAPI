package me.lian.sts.data.trainlist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataResponse

/**
 * A list of all trains.
 *
 * @property trains The list of trains.
 */
@Serializable
@SerialName("zugliste")
internal data class TrainList(
    val trains: List<Train>,
) : DataResponse
