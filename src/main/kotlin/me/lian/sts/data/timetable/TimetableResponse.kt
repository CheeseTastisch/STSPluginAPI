package me.lian.sts.data.timetable

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.lian.sts.data.DataResponse
import nl.adaptivity.xmlutil.serialization.XmlElement
import java.time.format.DateTimeParseException

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