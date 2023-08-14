package me.lian.sts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlDefault
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

/**
 * Details about a specific train.
 *
 * @property trainId The id of the train.
 * @property name The name of the train.
 * @property delay The delay of the train. (in the format of +/- minutes, e.g. +2)
 * @property track The track the train is currently on or will be on next or
 * null if the train is heading for exiting the facility or the train was redirected.
 * @property plannedTrack The track that was originally planed (by the timetable) the train would be on or
 * would be on next or null if the train is heading for exiting the facility or the train was redirected.
 * @property source The source of the train.
 * @property destination The destination of the train.
 * @property visible Whether the train is visible and therefor in the area of this facility or not yet.
 * @property atPlatform Whether the train is at a [platform][track].
 * @property userText The text the user has set for this train or null if none was set.
 * @property userTextSender The name of the user who set the [userText] or null if none was set.
 * @property hint A hint for this train or null if none was set.
 */
@Serializable
@SerialName("zugdetails")
data class TrainDetails(
    @XmlElement(false) @SerialName("zid") val trainId: Int,
    @XmlElement(false) val name: String,
    @XmlElement(false) @SerialName("verspaetung") val delay: Int,
    @XmlElement(false) @XmlDefault("") @SerialName("gleis") val track: String? = null,
    @XmlElement(false) @XmlDefault("") @SerialName("plangleis") val plannedTrack: String? = null,
    @XmlElement(false) @SerialName("von") val source: String,
    @XmlElement(false) @SerialName("nach") val destination: String,
    @XmlElement(false) @SerialName("sichtbar") val visible: Boolean,
    @XmlElement(false) @SerialName("amgleis") val atPlatform: Boolean,
    @XmlElement(false) @XmlDefault("") @SerialName("usertext") val userText: String? = null,
    @XmlElement(false) @XmlDefault("") @SerialName("usertextsender") val userTextSender: String? = null,
    @XmlElement(false) @XmlDefault("") @SerialName("hinweistext") val hint: String? = null,
) : DataResponse