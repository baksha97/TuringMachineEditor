import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
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

// ui inspo:
// https://www.youtube.com/watch?v=XtM-sRLxLLc

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

        val reelListState = rememberLazyListState()
        val reelCoroutineScope = rememberCoroutineScope()
        val quadrupleListState = rememberLazyListState()
        val quadrupleCoroutineScope = rememberCoroutineScope()
        val viewModel = TuringMachineViewModel(
            TURING_MACHINE,
            reelListState,
            reelCoroutineScope,
            quadrupleListState,
            quadrupleCoroutineScope
        )
        val viewModelState = remember { mutableStateOf(viewModel) }

        MaterialTheme {
            TuringMachineApp(viewModelState = viewModelState)
        }
    }
}

@Composable
fun TuringMachineApp(viewModelState: MutableState<TuringMachineViewModel>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TopAppBar is a pre-defined composable that's placed at the top of the screen. It has
        // slots for a title, navigation icon, and actions. Also known as the action bar.
        TopAppBar(
            // The Text composable is pre-defined by to Compose UI library; you can use this
            // composable to render text on the screen
            title = { Text("Turing Machine Editor") },
        )

        ReelView(viewModelState)
        Row {
            QuadrupleStateView(viewModelState)
            Button(
//                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = viewModelState.value::executeNextQuadruple
            ) {
                Text("Next")
            }
        }
        Text("heelo world")


    }
}

@Preview
@Composable
fun TuringMachineAppPreview() {
    val reelListState = rememberLazyListState()
    val reelCoroutineScope = rememberCoroutineScope()
    val quadrupleListState = rememberLazyListState()
    val quadrupleCoroutineScope = rememberCoroutineScope()
    val viewModel = TuringMachineViewModel(
        TURING_MACHINE,
        reelListState,
        reelCoroutineScope,
        quadrupleListState,
        quadrupleCoroutineScope
    )
    val viewModelState = remember { mutableStateOf(viewModel) }

    TuringMachineApp(viewModelState)
}

@Composable
fun QuadrupleStateView(viewModelState: MutableState<TuringMachineViewModel>) {
    val quadrupleStates = viewModelState.value.quadrupleStates.toList()
    LazyColumn(
        state = viewModelState.value.quadrupleListState,
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    viewModelState.value.quadrupleCoroutineScope.launch {
                        viewModelState.value.quadrupleListState.scrollBy(-delta)
                    }
                },
            )
            .fillMaxWidth(0.5f)
    ) {
        items(quadrupleStates, key = { it.first }) {
            val isSelected = viewModelState.value.currentStateName.value == it.first.name
                    && viewModelState.value.currentStateValue.value == it.first.value
            QuadrupleStateRow(it, isSelected = isSelected)
        }
    }
}

@Composable
fun QuadrupleStateRow(quadrupleState: Pair<State, Quadruple>, isSelected: Boolean = false) =
    Card(
        backgroundColor = if (isSelected) Color.Black else Color.Transparent
    ) {
        val circleRadius = 60f
        // Row is a composable that places its children in a horizontal sequence. You
        // can think of it similar to a LinearLayout with the horizontal orientation.
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                24.dp, Alignment.CenterHorizontally
            ),
        ) {
            // A pre-defined composable that's capable of rendering a switch. It honors the Material
            // Design specification.
            Text(
                quadrupleState.first.name,
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.DarkGray,
                            radius = circleRadius
                        )
                    },
            )
            Text(
                "${quadrupleState.first.value}",
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.Gray,
                            radius = circleRadius
                        )
                    },
            )

            Text(
                text = "${quadrupleState.second.command}",
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.DarkGray,
                            radius = circleRadius
                        )
                    },
            )
            Text(
                text = quadrupleState.second.end,
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.DarkGray,
                            radius = circleRadius
                        )
                    },
            )
        }
    }

@Composable
fun ReelView(viewModel: MutableState<TuringMachineViewModel>) = ReelItems(viewModel)



@Composable
fun ReelItems(viewModelState: MutableState<TuringMachineViewModel>) {
    LazyRow(
        state = viewModelState.value.reelListState,
        modifier = Modifier
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    viewModelState.value.reelCoroutineScope.launch {
                        viewModelState.value.reelListState.scrollBy(-delta)
                    }
                },
            )
    ) {
        val circleRadius = 48f
        itemsIndexed(viewModelState.value.reel) { index, item ->
            Box(
                modifier = Modifier.padding(4.dp),
            ) {
                if (viewModelState.value.reelPosition.value == index) Text(
                    "$item",
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .drawBehind {
                            drawCircle(
                                color = Color.Red,
                                radius = circleRadius
                            )
                        },
                )
                else Text(
                    text = "$item",
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .drawBehind {
                            drawCircle(
                                color = Color.Black,
                                radius = circleRadius
                            )
                        },
                )
            }
        }
    }
}

fun LazyListState.animateScrollAndCentralizeItem(index: Int, scope: CoroutineScope) {
    val itemInfo = this.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
    scope.launch {
        if (itemInfo != null) {
            val center = this@animateScrollAndCentralizeItem.layoutInfo.viewportEndOffset / 2
            val childCenter = itemInfo.offset + itemInfo.size / 2
            this@animateScrollAndCentralizeItem.animateScrollBy((childCenter - center).toFloat())
        } else {
            this@animateScrollAndCentralizeItem.animateScrollToItem(index)
        }
    }
}

class TuringMachineViewModel(
    private val turingMachine: TuringMachine,
    val reelListState: LazyListState,
    val reelCoroutineScope: CoroutineScope,
    val quadrupleListState: LazyListState,
    val quadrupleCoroutineScope: CoroutineScope
) {
    var reel = turingMachine.reel.toMutableStateList()

    var reelPosition = mutableStateOf(turingMachine.reelPosition)
    var executionCount = mutableStateOf(turingMachine.executions)
    var currentStateName = mutableStateOf(turingMachine.currentStateName)
    var currentStateValue = mutableStateOf(turingMachine.currentStateValue)
    var nextQuadruple = mutableStateOf(turingMachine.nextQuadruple())
    var quadrupleStates = turingMachine.quadrupleStates // doesn't change

    private val quadrupleStateToIndex: Map<State, Int>

    init {
        val quadrupleStateToIndex = mutableMapOf<State, Int>()
        quadrupleStates
            .toList()
            .forEachIndexed { index, item ->
                quadrupleStateToIndex[item.first] = index
            }
        this.quadrupleStateToIndex = quadrupleStateToIndex
        updateScrollStates()
    }

    private fun updateState() {
        reel = turingMachine.reel.toMutableStateList()
        executionCount.value = turingMachine.executions
        reelPosition.value = turingMachine.reelPosition
        currentStateName.value = turingMachine.currentStateName
        currentStateValue.value = turingMachine.currentStateValue
        nextQuadruple.value = turingMachine.nextQuadruple()
        updateScrollStates()
    }

    private fun updateScrollStates() {
        reelCoroutineScope.launch {
            reelListState.animateScrollAndCentralizeItem(index = reelPosition.value, this)
        }
        quadrupleCoroutineScope.launch {
            quadrupleStateToIndex[State(currentStateName.value, currentStateValue.value)]
                ?.let {
                    quadrupleListState.animateScrollAndCentralizeItem(index = it, this)
                }
        }
    }


    fun executeNextQuadruple() =
        try {
            turingMachine.executeSubsequentQuadruple()
            updateState()
        } catch (e: Exception) {
            e.printStackTrace()
        }

}