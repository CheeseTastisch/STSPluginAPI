package me.lian.sts.data.trainlist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataResponse
import nl.adaptivity.xmlutil.serialization.XmlElement

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
