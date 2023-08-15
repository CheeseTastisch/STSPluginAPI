package me.lian.sts.data.event

/**
 * The type of [Event].
 *
 * A [Event] is an action a train performs.
 *
 * @property identifier The identifier of the [EventType].
 */
@Suppress("unused")
enum class EventType(val identifier: String) {

    /**
     * The train just entered the current facility.
     */
    ENTRANCE("einfahrt"),

    /**
     * The train arrived at a station.
     */
    ARRIVAL("ankunft"),

    /**
     * The train departed from a station.
     */
    DEPARTURE("abfahrt"),

    /**
     * The train exited the current facility.
     */
    EXIT("ausfahrt"),

    /**
     * A train stopped because of a red signal.
     */
    STOP("rothalt"),

    /**
     * A train that stopped for a red signal continues.
     */
    CONTINUE("wurdegruen"),

    /**
     * Two trains coupled together.
     */
    COUPLE("kuppeln"),

    /**
     * A train split into two.
     */
    SPLIT("fluegeln"),


}