package kturing

enum class Command {
    LEFT, RIGHT, FILL, BLANK;

    override fun toString(): String = when (this) {
        LEFT -> "L"
        RIGHT -> "R"
        FILL -> "1"
        BLANK -> "B"
    }
}

data class State internal constructor(val name: String, val value: Int) {

    constructor(name: String, value: Char) : this(name, if (value == 'B') 0 else 1)

    override fun toString() = "Q$name: $value"

}

data class Quadruple internal constructor(
    val startingState: State,
    val command: Command,
    val end: String
)

data class Program internal constructor(
    val rawInput: String,
    val quadrupleStates: Map<State, Quadruple>
) {

    val size = quadrupleStates
        .values
        .toList()
        .size

    fun findQuadruple(state: State): Quadruple? = quadrupleStates[state]

}

open class TapeExecutionException(message: String? = null) : Exception(message)

sealed class TapeProcessResult(val endingState: String, val endingValue: Int) {
    class MovementSuccess(val newPosition: Int, endingState: String, endingValue: Int) :
        TapeProcessResult(endingState, endingValue)

    class WriteSuccess(val index: Int, endingState: String, endingValue: Int) :
        TapeProcessResult(endingState, endingValue)

    class MovementFailure(val index: Int, endingState: String, endingValue: Int) :
        TapeProcessResult(endingState, endingValue)
}

data class Tape internal constructor(private val capacity: Int, val initialNumbers: List<Int> = listOf(1)) {

    internal var currentState: State = State(INITIAL_QUADRUPLE_STATE_NAME, BLANK)
    var reelPosition: Int

    var _reel: MutableList<Int>
    val reel: List<Int>
        get() = _reel

    init {
        val numbersReel = mutableListOf(BLANK)
            .apply {
                initialNumbers.forEach {
                    addAll(IntRange(1, it).map { FILL })
                    add(BLANK)
                }
            }
        val extraSpace = capacity - numbersReel.size
        val spaceOnSidesToFill = extraSpace.floorDiv(2)

        _reel = mutableListOf<Int>().apply {
            addAll(IntRange(1, spaceOnSidesToFill).map { BLANK })
            addAll(numbersReel)
            addAll(IntRange(1, spaceOnSidesToFill).map { BLANK })
        }

        reelPosition = spaceOnSidesToFill
    }


    fun calculateIntegersOnReel() = _reel.countsOfConsecutiveEquality(ignoring = setOf(BLANK))

    internal fun process(quadruple: Quadruple): TapeProcessResult {
        if (quadruple.startingState != currentState)
            throw TapeExecutionException("$currentState does not match ${quadruple.startingState}")

        return when (quadruple.command) {
            Command.LEFT -> move(
                left = true,
                successfulQuadrupleEndName = quadruple.end
            )
            Command.RIGHT -> move(
                left = false,
                successfulQuadrupleEndName = quadruple.end
            )
            Command.FILL -> write(fill = true, successfulQuadrupleEnd = quadruple.end)
            Command.BLANK -> write(fill = false, successfulQuadrupleEnd = quadruple.end)
        }.also { currentState = State(it.endingState, it.endingValue) }

    }

    private fun write(fill: Boolean = true, successfulQuadrupleEnd: String): TapeProcessResult {
        _reel[reelPosition] = if (fill) FILL else BLANK
        return TapeProcessResult.WriteSuccess(
            reelPosition,
            successfulQuadrupleEnd,
            _reel[reelPosition]
        )
    }

    private fun move(left: Boolean = true, successfulQuadrupleEndName: String): TapeProcessResult {
        val reelPositionChange = if (left) -1 else 1
        val isValidPositionChange =
            reelPosition + reelPositionChange in 0 until capacity - 1

        return if (isValidPositionChange) {
            reelPosition += reelPositionChange
            TapeProcessResult.MovementSuccess(
                newPosition = reelPosition,
                successfulQuadrupleEndName, _reel[reelPosition]
            )
        } else TapeProcessResult.MovementFailure(
            reelPosition,
            currentState.name,
            currentState.value
        )
    }

    override fun toString(): String {
        return _reel.toString()
    }

    companion object {
        const val INITIAL_QUADRUPLE_STATE_NAME = "1"
        const val BLANK = 0
        const val FILL = 1
    }

}

data class TuringMachine internal constructor(
    val name: String,
    private val tape: Tape,
    private val program: Program
) {

//    val id = uuid4().toString()
    var executions = 0

    val reel get() = tape.reel
    val reelPosition get() = tape.reelPosition

    val currentStateName get() = tape.currentState.name
    val currentStateValue get() = tape.currentState.value

    // accessible initial configuration
    val initialNumbers get() = tape.initialNumbers
    val initialTapeSize get() = tape.reel.size
    val initialProgramInput get() = program.rawInput

    val currentIntegersOnReel get() = tape.calculateIntegersOnReel()

    fun nextQuadruple(): Quadruple? = program.findQuadruple(tape.currentState)

    fun hasNextQuadruple(): Boolean = nextQuadruple() != null

    fun executeSubsequentQuadruple(): TapeProcessResult = nextQuadruple()
        ?.let { tape.process(it) }
        .also { executions++ }
        ?: throw IllegalStateException("The program does not contain a quadruple to execute.")

}