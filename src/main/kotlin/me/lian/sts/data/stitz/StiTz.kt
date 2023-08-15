package me.lian.sts.data.stitz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataResponse
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * Information about the StiTz.
 *
 * The StiTz is the Stellwerksim internal telephone network (_Stellwerksim internes Telefonnetz_).
 *
 * @property region The StiTz number of the region.
 * @property general The StiTz number of the general StiTz.
 */
@Serializable
@SerialName("stitz")
data class StiTz(
    @XmlElement(false) val region: String,
    @XmlElement(false) @SerialName("allgemein") val general: String,
) : DataResponse
