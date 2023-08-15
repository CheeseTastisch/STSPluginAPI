package me.lian.sts.data.timetable

import kotlinx.serialization.Serializable
import me.lian.sts.flag.Flag
import nl.adaptivity.xmlutil.serialization.XmlDefault
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import java.time.LocalTime
import java.time.format.DateTimeParseException

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
 * @property hint The hint that was set for the stop or null if no hint was set.
 */
@Serializable
@XmlSerialName("gleis")
internal data class TrainStopResponse(
    @XmlElement(false) @XmlSerialName("plan") val plannedTrack: String,
    @XmlElement(false) @XmlSerialName("name") val track: String,
    @XmlElement(false) @XmlSerialName("an") val arrival: String,
    @XmlElement(false) @XmlSerialName("ab") val departure: String,
    @XmlElement(false) @XmlSerialName("flags") val flags: String = "",
    @XmlElement(false) @XmlDefault("") @XmlSerialName("hinweistext") val hint: String? = null,
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
        hint = hint,
    )

}