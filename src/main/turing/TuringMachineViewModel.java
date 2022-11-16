package turing;

public class TuringMachineViewModel {

    private static final String NEXT_QUAD_NULL = "None available";
    private static final String PREV_QUAD_NULL = "Not executed";
    private final TuringMachine tm;

    public TuringMachineViewModel(TuringMachine tm) {
        this.tm = tm;
    }

    public String getNumbersOnTape() {
        return tm.numbersOnTape().toString();
    }

    public String getPrevQuad() {
        return (tm.getPreviousQuadruple() != null) ? tm.getPreviousQuadruple().toString() : PREV_QUAD_NULL;
    }

    public String getNextQuad() {
        return ((tm.nextQuadruple()) != null) ? tm.nextQuadruple().toString() : NEXT_QUAD_NULL;
    }

    public String getExecutionCount() {
        return "Execution #" + tm.getExecutionCount();
    }

    public String getPosition() {
        return "Cell position: #" + tm.getPos();
    }

    public String getTapeState() {
        if (!tm.hasNextQuadruple()) return "MACHINE HALTED @" + tm.getTapeState();
        else return tm.getTapeState().toString();
    }

    public String getTape() {
        return tm.toString();
    }

    public String toString() {
        return getTape();
    }

}
