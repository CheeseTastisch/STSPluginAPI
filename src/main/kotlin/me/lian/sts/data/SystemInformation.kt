package me.lian.sts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * A request to get information about the current system.
 */
@Serializable
@SerialName("anlageninfo")
internal data object SystemInformationRequest : DataRequest

/**
 * Information about the current system.
 *
 * @property buildNumber The build number of the simulator.
 * @property facilityId The id of the current facility.
 * @property facilityName The name of the current facility.
 * @property region The region the current facility is in.
 * @property online Whether the player is in the online mode.
 */
@Serializable
@SerialName("anlageninfo")
data class SystemInformation(
    @XmlElement(false) @SerialName("simbuild") val buildNumber: String,
    @XmlElement(false) @SerialName("aid") val facilityId: Int,
    @XmlElement(false) @SerialName("name") val facilityName: String,
    @XmlElement(false) val region: String,
    @XmlElement(false) val online: Boolean
) : DataResponse