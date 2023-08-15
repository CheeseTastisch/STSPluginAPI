package me.lian.sts.flag

/**
 * A flag is a task that a train has to execute when in a station.
 *
 * @property identifier The identifier of the flag.
 */
sealed class Flag(
    @Suppress("MemberVisibilityCanBePrivate") val identifier: Char,
) {

    companion object {

        /**
         * The regex used to parse a list of flags from a string.
         *
         * This regex first matches the identifier of the flag, then it matches the flag's parameter,
         * by creating a positive lookbehind for each identifier to check that each identifier
         * has the correct parameter and then capturing it.
         */
        private val FLAG_REGEX = Regex(
            "(?<identifier>[ABDEFKLPRW])" + // matches the identifier of the flag
                    "(?:" + // group for the flag's parameter, to make the OR operator work
                    "(?<=[ADLPR])" + // looks back for any flag without any argument
                    "|(?<=B)(?<id>[1-9])" + // looks back for the B flag and captures the id after it
                    "|(?<=[EFK])\\((?<trainId>\\d+)\\)" + // looks back for any flag with a train id as argument
                    "|(?<=P)\\[(?<direction>[lrud])]" + // locks back for a P flag with a direction as argument and captures it
                    "|(?<=W)\\[(?<firstInOut>\\d+)]\\[(?<secondInOut>\\d+)]" + // looks back for a W flag and captures its arguments
                    ")"
        )

        /**
         * Parses the given [flags] string to a [List] of [Flag]s.
         *
         * This will not check if the flags are valid or can be
         * paired with the other given flags.
         *
         * All flags that are not known or have wrong parameters will be ignored.
         *
         * @throws IllegalStateException If some flag is not valid, but matches the regex (should not happen).
         * @throws NullPointerException If some flag is not valid, but matches the regex (should not happen).
         */
        fun parse(flags: String): List<Flag> {
            return FLAG_REGEX
                .findAll(flags)
                .map {
                    when (it.groups["identifier"]!!.value[0]) {
                        'A' -> EarlyDepartureFlag
                        'B' -> ThemeFlag(it.groups["id"]?.value?.toInt()!!)
                        'D' -> DriveThroughFlag
                        'E' -> ChangeNameFlag(it.groups["trainId"]?.value?.toInt()!!)
                        'F' -> SplitFlag(it.groups["trainId"]?.value?.toInt()!!)
                        'K' -> CoupleFlag(it.groups["trainId"]?.value?.toInt()!!)
                        'L' -> LocomotiveCirculationFlag
                        'P' -> CanSpanFlag(CanSpanFlag.Direction.fromIdentifier(it.groups["direction"]?.value?.get(0)))
                        'R' -> ChangeDirectionFlag
                        'W' -> ChangeLocomotiveFlag(
                            it.groups["firstInOut"]?.value?.toInt()!!,
                            it.groups["secondInOut"]?.value?.toInt()!!
                        )

                        else -> throw IllegalStateException("Unknown flag identifier: ${it.groups["identifier"]!!.value}")
                    }
                }
                .toList()
        }

    }

}

/**
 * A flag that indicates that the train can depart early, when the player presses the "depart" button or
 * when it has reached its departure time, ignoring any delays.
 */
data object EarlyDepartureFlag : Flag('A')

/**
 * A flag that can indicated different things, depending on the theme script it is used with and the [id].
 *
 * In connection with the standard them script (1) the `B1` flag indicates that the train
 * can not have any delay when entering the facility (usually used for trains coming from a depot).
 *
 * @property id The id of the flag.
 */
data class ThemeFlag(val id: Int) : Flag('B')

/**
 * A flag that indicates that the train should not stop at the station, but drive through it.
 */
data object DriveThroughFlag : Flag('D')

/**
 * A flag that indicates that the train should change its name to the name of the train with the given [newTrainId].
 *
 * @property newTrainId The id of the train whose name should be used.
 */
data class ChangeNameFlag(val newTrainId: Int) : Flag('E')

/**
 * A flag that indicates that the train should be split into two trains.
 *
 * The new train will have an id of [decoupledTrainId] and span at the front of the train.
 * The back of the train will be the original train.
 *
 * @property decoupledTrainId The id of the new train.
 */
data class SplitFlag(val decoupledTrainId: Int) : Flag('F')

/**
 * A flag that indicates that the train should couple with the train with the given [with] id.
 *
 * In this case it does not matter which train is the front and which is the back of the train.
 *
 * @property with The id of the train to couple with.
 */
data class CoupleFlag(val with: Int) : Flag('K')

/**
 * A flag that indicates that the locomotive of the train should circulate around the train.
 *
 * The locomotive will have a name of `Lok <train name>`.
 *
 * For this the train is split, with the locomotive at the front and the rest of the train at the back,
 * and then coupled again. With the coupling it technically does not matter which train is the front and which is the back.
 */
data object LocomotiveCirculationFlag : Flag('L')

/**
 * A flag that indicates that a train can start at this station at the start of the simulation.
 *
 * In addition, a [direction] can be specified, which indicates in which direction the train should start.
 * If non is set, the train will start in a random direction.
 *
 * @property direction The direction in which the train should start.
 */
data class CanSpanFlag(val direction: Direction) : Flag('P') {

    /**
     * The direction in which a train will start.
     *
     * @property identifier The identifier of the direction.
     */
    enum class Direction(val identifier: Char?) {

        /**
         * The train will start to the left.
         */
        LEFT('l'),

        /**
         * The train will start to the right.
         */
        RIGHT('r'),

        /**
         * The train will start upwards.
         */
        UP('u'),

        /**
         * The train will start downwards.
         */
        DOWN('d'),

        /**
         * The train will start in a random direction.
         */
        UNDEFINED(null);

        companion object {

            /**
             * Gets the [Direction] from the given [identifier],
             * returns [UNDEFINED] if no direction with the given identifier exists or
             * null is given.
             */
            fun fromIdentifier(identifier: Char?) = entries.find { it.identifier == identifier } ?: UNDEFINED

        }

    }

}

/**
 * A flag that indicates that the train should change its direction.
 */
data object ChangeDirectionFlag : Flag('R')

/**
 * A flag that indicates that the train should change its locomotive.
 *
 * For this the train is split, with the locomotive at the front and the rest of the train at the back,
 * then the new locomotive is coupled with the rest of the train. For this it does not matter if the train or
 * the locomotive is the front or the back.
 *
 * The [firstInOutId] and [secondInOutId] indicate from where/to which the new/old locomotive should come/go.
 * It does not matter what is defined first and what second, only the type of the entry/exit points matters.
 *
 * @property firstInOutId The id of the first in/out point.
 * @property secondInOutId The id of the second in/out point.
 */
data class ChangeLocomotiveFlag(val firstInOutId: Int, val secondInOutId: Int) : Flag('W')