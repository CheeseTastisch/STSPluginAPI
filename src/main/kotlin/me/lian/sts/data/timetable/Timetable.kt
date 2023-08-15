package me.lian.sts.data.timetable

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