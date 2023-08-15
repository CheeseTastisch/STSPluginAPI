package me.lian.sts.data.timetable

import me.lian.sts.flag.Flag
import java.time.LocalTime

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
    val flags: List<Flag>,
)