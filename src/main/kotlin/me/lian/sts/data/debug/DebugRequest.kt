package me.lian.sts.data.debug

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataRequest
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * A request to set the debug mode.
 *
 * @property debug Whether the debug mode should be enabled.
 */
@Serializable
@SerialName("debug")
internal data class DebugRequest(
    @XmlElement(false) val debug: Boolean,
) : DataRequest