package me.lian.sts.data.heat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataResponse
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * The current heat of the STS
 *
 * @property heat The current heat of the STS.
 */
@Serializable
@SerialName("hitze")
internal data class Heat(
    @XmlElement(false) @SerialName("hitze") val heat: Long,
) : DataResponse
