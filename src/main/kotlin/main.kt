import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kturing.MachineFactory
import kturing.Quadruple
import kturing.State
import kturing.TuringMachine


val TURING_MACHINE = MachineFactory()
    .makeTuringMachine(
        name = "Development",
        capacity = 100,
        initialNumbers = listOf(2, 3),
        programInput = """
            1,B,R,299
            299,1,B,399
            399,B,R,499
            499,1,R,499
            499,B,R,599
            599,1,R,599
            599,B,1,699
            699,1,R,699
            699,B,L,799
            799,1,L,799
            799,B,L,899
            899,1,L,899
            899,B,L,999
            999,B,1,1099
            1099,1,R,1099
            1099,B,R,1199
            1199,1,1,299
            1199,B,L,1299
            1299,B,1,1399
            1399,1,L,1399
            1399,B,R,1499
            1499,1,B,199
            199,B,L,2

            1m,B,L,2
            2,B,1,3
            3,1,R,3
            3,B,R,4
            4,B,R,4
            4,1,R,5
            5,1,R,5
            5,B,R,6
            6,B,R,6
            6,1,R,7
            7,1,R,7
            7,B,R,8
            8,1,R,8
            8,B,1,9
            9,1,L,9
            9,B,L,10
            10,1,L,10
            10,B,L,19
            19,1,L,11
            19,B,L,19
            11,1,L,11
            11,B,R,12
            12,1,B,13
            13,B,R,14
            14,1,L,15
            15,B,L,15
            15,1,L,16
            16,1,L,16
            16,B,B,2
            14,B,R,17
            17,1,B,18
            17,B,R,27
            27,B,R,27
            18,B,R,29
            27,1,1,17
            29,B,L,22
            29,1,L,20
            20,B,L,20
            20,1,L,21
            21,1,L,21
            21,B,B,1m
            22,B,L,22
            22,1,B,23
            23,B,L,24
            24,1,1,22
            24,B,R,25
            25,B,R,25
            25,1,L,26
        """.trimIndent()
    )

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 900.dp, height = 900.dp)
    ) {

        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        val viewModel = remember { mutableStateOf(TuringMachineViewModel(TURING_MACHINE, listState, coroutineScope)) }

        MaterialTheme {
            TuringMachineApp(viewModel = viewModel)
        }
    }
}

@Composable
fun TuringMachineApp(viewModel: MutableState<TuringMachineViewModel>) {
    Column {
        ExecutionView(viewModel)
        QuadrupleStateView(viewModel.value.quadrupleStates.toList())
    }
}

@Composable
fun QuadrupleStateView(quadrupleStates: List<Pair<State, Quadruple>>) =
    LazyColumn(state = rememberLazyListState()) {
        items(quadrupleStates, key = { it.first }) {
            QuadrupleStateRow(it)
        }
    }

@Composable
fun QuadrupleStateRow(quadrupleState: Pair<State, Quadruple>) =
    Row {
        Text(
            modifier = Modifier
//                .padding(4.dp)
                .drawBehind {
                    drawCircle(
                        color = Color.Black,
                        radius = size.maxDimension
                    )
                },
            text = "${quadrupleState.first}",
            color = Color.White
        )
    }

@Composable
fun ExecutionView(viewModel: MutableState<TuringMachineViewModel>) =
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        ItemRow(viewModel)
        Text("Reel position ${viewModel.value.reelPosition.value}")
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                println("Reel position ${viewModel.value.reelPosition}")
                viewModel.value.executeNextQuadruple()
            }
        ) {
            Text("TM Value")
        }
    }


@Composable
fun ItemRow(viewModel: MutableState<TuringMachineViewModel>) =
    LazyRow(state = viewModel.value.reelListState) {
        itemsIndexed(viewModel.value.reel) { index, item ->
            Box(modifier = Modifier.padding(4.dp)) {
                if (viewModel.value.reelPosition.value == index)
                    Text(
                        "[[[$index]]]",
                        Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                else
                    Text(
                        "$index",
                        Modifier.align(Alignment.Center),
                        color = Color.Blue
                    )
            }
        }
    }

class TuringMachineViewModel(
    private val turingMachine: TuringMachine,
    val reelListState: LazyListState,
    private val reelCoroutineScope: CoroutineScope
) {
    var reel = turingMachine.reel.toMutableStateList()

    var reelPosition = mutableStateOf(turingMachine.reelPosition)
    var executionCount = mutableStateOf(turingMachine.executions)
    var currentStateName = mutableStateOf(turingMachine.currentStateName)
    var currentStateValue = mutableStateOf(turingMachine.currentStateValue)
    var nextQuadruple = mutableStateOf(turingMachine.nextQuadruple())
    var quadrupleStates = turingMachine.quadrupleStates // doesn't change

    private fun updateState() {
        reel = turingMachine.reel.toMutableStateList()
        executionCount.value = turingMachine.executions
        reelPosition.value = turingMachine.reelPosition
        currentStateName.value = turingMachine.currentStateName
        currentStateValue.value = turingMachine.currentStateValue
        nextQuadruple.value = turingMachine.nextQuadruple()
        reelCoroutineScope.launch {
            reelListState.scrollToItem(index = reelPosition.value)
        }
    }

    fun executeNextQuadruple() {
        try {
            turingMachine.executeSubsequentQuadruple()
            updateState()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}