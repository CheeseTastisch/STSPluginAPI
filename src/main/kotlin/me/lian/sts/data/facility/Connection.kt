package me.lian.sts.data.facility

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlDefault
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * A connection between two shapes.
 *
 * The connection can be defined by two ids, one id and one name or two names.
 * Usually the name is only used for platforms, because they don't have an id,
 * **but this is only an assumption and not documented**.
 *
 * @property id1 The id of the first shape.
 * @property id2 The id of the second shape.
 * @property name1 The name of the first shape.
 * @property name2 The name of the second shape.
 */
@Serializable
@SerialName("connector")
data class Connection(
    @XmlElement(false) @XmlDefault("") @SerialName("enr1") val id1: Int? = null,
    @XmlElement(false) @XmlDefault("") @SerialName("enr2") val id2: Int? = null,
    @XmlElement(false) @XmlDefault("") @SerialName("name1") val name1: String? = null,
    @XmlElement(false) @XmlDefault("") @SerialName("name2") val name2: String? = null,
)