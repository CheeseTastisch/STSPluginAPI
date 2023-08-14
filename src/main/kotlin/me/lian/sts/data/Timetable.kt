package me.lian.sts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.flag.Flag
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import java.time.LocalTime
import java.time.format.DateTimeParseException

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

/**
 * The response from the plugin api to a [TimetableRequest].
 *
 * This is a raw response, that will be parsed to a [Timetable] by the system.
 *
 * @property trainId The id of the train.
 * @property stops The stops of the train.
 */
@Serializable
@SerialName("zugfahrplan")
internal data class TimetableResponse(
    @XmlElement(false) @SerialName("zid") val trainId: Int,
    @SerialName("gleis") val stops: List<TrainStopResponse>,
) : DataResponse {

    /**
     * Converts the raw [TimetableResponse] to a [Timetable].
     *
     * @throws IllegalStateException If some flag is not valid, but matches the regex (should not happen).
     * @throws NullPointerException If some flag is not valid, but matches the regex (should not happen).
     * @throws DateTimeParseException If the arrival or departure time is not in the correct format (should not happen).
     */
    fun toTimetable() = Timetable(
        trainId = trainId,
        stops = stops.map { it.toTrainStop() },
    )

}

/**
 * A timetable of a train that can be identified by its [id][trainId].
 *
 * @property trainId The id of the train.
 * @property stops The stops of the train.
 */
data class Timetable(
    val trainId: Int,
    val stops: List<TrainStop>,
)

/**
 * A response from the plugin api to a [TimetableRequest] that contains a single stop of a train.
 *
 * This is a raw response, that will be parsed to a [TrainStop] by the system.
 *
 * @property plannedTrack The track originally planned for the stop.
 * @property track The track that is currently set for the stop.
 * @property arrival The arrival time of the train.
 * @property departure The departure time of the train.
 * @property flags The flags of the stop.
 */
@Serializable
@XmlSerialName("gleis")
internal data class TrainStopResponse(
    @XmlElement(false) @XmlSerialName("plan") val plannedTrack: String,
    @XmlElement(false) @XmlSerialName("name") val track: String,
    @XmlElement(false) @XmlSerialName("an") val arrival: String,
    @XmlElement(false) @XmlSerialName("ab") val departure: String,
    @XmlElement(false) @XmlSerialName("flags") val flags: String = "",
) {

    /**
     * Converts the raw [TrainStopResponse] to a [TrainStop].
     *
     * @throws IllegalStateException If some flag is not valid, but matches the regex (should not happen).
     * @throws NullPointerException If some flag is not valid, but matches the regex (should not happen).
     * @throws DateTimeParseException If the arrival or departure time is not in the correct format (should not happen).
     */
    fun toTrainStop() = TrainStop(
        planned = plannedTrack,
        track = track,
        arrival = LocalTime.parse(arrival),
        departure = LocalTime.parse(departure),
        flags = Flag.parse(flags),
    )

}

/**
 * A single stop in the [Timetable] of a train.
 *
 * @property planned The track originally planned for the stop.
 * @property track The track that is currently set for the stop.
 * @property arrival The arrival time of the train.
 * @property departure The departure time of the train.
 * @property flags The flags of the stop.
 */
data class TrainStop(
    val planned: String,
    val track: String,
    val arrival: LocalTime,
    val departure: LocalTime,
    val flags: List<Flag>
)