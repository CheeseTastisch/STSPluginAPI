package me.lian.sts.parsing

import kotlinx.serialization.*
import me.lian.sts.data.DataRequest
import me.lian.sts.data.DataResponse
import nl.adaptivity.xmlutil.serialization.XML

/**
 * A parser for parsing the XML responses from the server and
 * the XML requests to the server.
 */
@OptIn(InternalSerializationApi::class)
internal object XmlParser {

    /**
     * The kotlinx.serialization XML parser instance.
     */
    private val xml = XML()

    /**
     * A regex for getting the root element of the XML.
     */
    private val rootRegex = Regex("<(\\w+)")

    /**
     * All response types the server can respond with.
     *
     * For this to work, all response types must be annotated with [SerialName] and extend [DataResponse].
     */
    private val responseTypes by lazy {
        DataResponse::class
            .sealedSubclasses
            .filter { it.annotations.any {  annotation -> annotation is SerialName } }
            .map { it.annotations.filterIsInstance<SerialName>().first().value to it }
    }

    /**
     * Parses a response from the server.
     *
     * For this the root element of the XML is used to determine the type of the response.
     *
     * @param xml The XML to parse.
     * @return The parsed response.
     *
     * @throws IllegalArgumentException If no response type could be found for the root element.
     */
    fun parseResponse(xml: String): DataResponse? {
        val rootElement = rootRegex.find(xml)?.groupValues?.get(1) ?: throw IllegalArgumentException("Invalid XML")

        val type = responseTypes.firstOrNull { it.first == rootElement } ?: throw IllegalArgumentException("Invalid XML")
        return try {
            this.xml.decodeFromString(type.second.serializer(), xml)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Parses a request to the server.
     *
     * @param request The request to parse.
     * @return The parsed request.
     */
    fun parseRequest(request: DataRequest): String? {
        @Suppress("UNCHECKED_CAST")
        return try {
            this.xml.encodeToString(request::class.serializer() as KSerializer<DataRequest>, request)
        } catch (e: Exception) {
            null
        }
    }

}