package turing;

import java.util.HashMap;
import java.util.List;

public class TuringMachine {
    private final Tape tape;
    private Program program;
    private int executionCount;
    private Quadruple previousQuadruple;

    public TuringMachine(Program program, Tape tape) {
        this.program = program;
        this.tape = tape;
        this.executionCount = 0;
    }


    public void changeProgram(Program p) {
        this.program = p;
    }

    private HashMap<State, Quadruple> getProgramStates() {
        return program.getQuadrupleStates();
    }

    public State getTapeState() {
        return tape.getCurrentState();
    }

    public Quadruple getPreviousQuadruple() {
        return this.previousQuadruple;
    }

    public boolean hasNextQuadruple() {
        return getProgramStates().containsKey(getTapeState());
    }

    public Quadruple nextQuadruple() {
        return getProgramStates().get(getTapeState());
    }

    public void executeNextQuadruple() {
        executionCount++;
        previousQuadruple = nextQuadruple();
        tape.execute(nextQuadruple());
    }

    public List<Integer> numbersOnTape() {
        return tape.currentNumbersOnTape();
    }

    public Tape.Partition getTapePartition() {
        return tape.getTapePartition();
    }

    public int getExecutionCount() {
        return executionCount;
    }

    public String toString() {
        return getTapePartition().toString();
    }
}
