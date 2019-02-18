package turing;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TuringMachine {
    private Program program;
    private final Tape tape;

    private int executionCount;

    public TuringMachine(Program program, int... inputs){
        this.program = program;
        this.tape = new Tape(inputs);
        this.executionCount = 0;
    }

    public TuringMachine(Program program, String startTape){
        this.program = program;
        this.tape = new Tape(startTape);
    }

    public void changeProgram(Program p){
        this.program = p;
    }

    public HashMap<State, Quadruple> getProgramStates(){
        return program.getQuadrupleStates();
    }

    public State getTapeState(){
        return tape.getCurrentState();
    }

    public boolean hasNextQuadruple(){
        return getProgramStates().containsKey(getTapeState());
    }

    public Quadruple nextQuadruple(){
        return getProgramStates().get(getTapeState());
    }

    public void executeNextQuadruple(){
        executionCount++;
        tape.execute(nextQuadruple());
    }

    public List<Integer> numbersOnTape(){
        return tape.currentNumbersOnTape();
    }

    public String[] getTapeDisplay(){
        return tape.pointedAt(tape.getPos());
    }

    public int getExecutionCount() {
        return executionCount;
    }

    public String toString(){
        return Arrays.asList(getTapeDisplay()).toString();
    }
}
