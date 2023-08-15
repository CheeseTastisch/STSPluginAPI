package me.lian.sts.data.timetable

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataRequest
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * A request to get the [Timetable] of a train by its [id][trainId].
 *
 * @property trainId The id of the train.
 */
@Serializable
@SerialName("zugfahrplan")
internal data class TimetableRequest(
    @XmlElement(false) @SerialName("zid") val trainId: Int,
) : DataRequest