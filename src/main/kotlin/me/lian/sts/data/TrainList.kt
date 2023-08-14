package me.lian.sts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * Request to get a list of all trains.
 */
@Serializable
@SerialName("zugliste")
internal data object TrainListRequest : DataRequest

/**
 * A list of all trains.
 *
 * @property trains The list of trains.
 */
@Serializable
@SerialName("zugliste")
data class TrainList(val trains: List<Train>) : DataResponse

/**
 * A train.
 *
 * @property trainId The id of the train.
 * @property name The name of the train.
 */
@Serializable
@SerialName("zug")
data class Train(
    @XmlElement(false) @SerialName("zid") val trainId: Int,
    @XmlElement(false) @SerialName("name") val name: String,
) : DataResponse
