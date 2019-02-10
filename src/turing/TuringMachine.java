package turing;


import java.util.HashMap;
import java.util.List;

public class TuringMachine {
    private Program program;
    private Tape tape;
    private String startingTape;

    public TuringMachine(Program program, int... inputs){
        this.program = program;
        this.tape = new Tape(inputs);
        this.startingTape = tape.toString().replace("[", "").replace("]","").replace(",","");
    }

    public TuringMachine(Program program, String startTape){
        this.program = program;
        this.tape = new Tape(startTape);
        this.startingTape = startTape;
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
        tape.execute(nextQuadruple());
    }

    public List<Integer> numbersOnTape(){
        return tape.currentNumbersOnTape();
    }

    public String getStartingTape(){
        return startingTape;
    }

    public String[] getTapeDisplay(){
        return tape.pointedAt(tape.getPos());
    }


}
