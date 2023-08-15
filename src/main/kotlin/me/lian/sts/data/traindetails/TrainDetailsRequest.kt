package me.lian.sts.data.traindetails

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataRequest
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * Request to get details about a specific train by its [id][trainId].
 *
 * @property trainId The id of the train.
 */
@Serializable
@SerialName("zugdetails")
internal data class TrainDetailsRequest(
    @XmlElement(false) @SerialName("zid") val trainId: Int,
) : DataRequest