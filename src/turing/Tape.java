package turing;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tape {
    private static final char BLANK = 'B';
    private static final char FILL = '1';

    private ArrayList<Character> cells;
    private int[] inputs;

    private int pos;
    private State currentState;

    private Tape(){}

    private void initialize(){
        cells = new ArrayList<>();
        cells.add(BLANK);
        pos = 0;
        currentState = new State("1", BLANK);
    }

    public Tape(int... inputs){
        initialize();
        this.inputs = inputs;
        for(int input: inputs){
            cells.addAll(intInputAsFill(input));
            cells.add(BLANK);
        }
    }

    public Tape(String input){
        initialize();
        if(input.charAt(0) == BLANK) cells.remove(0); //remove blank if there is a blank as a placeholder in the manual input
        for(int i =0; i<input.length(); i++){
            char cur = input.charAt(i);
            if(cur == ' ') continue;
            if(cur != FILL && cur != BLANK)
                throw new IllegalArgumentException("Tape input is not valid for: " + cur);
            cells.add(cur);
        }
        cells.add(BLANK);
        this.inputs = currentNumbersOnTape().stream().mapToInt(i->i).toArray();
    }

    private List<Character> intInputAsFill(int x){
        ArrayList<Character> list = new ArrayList<>(x);
        for(int i=0; i<x; i++){
            list.add(FILL);
        }
        return list;
    }

    public void execute(Quadruple q){
        if(!currentState.equals(q.getStartingState()))
            throw new IllegalArgumentException("This function cannot be executed.");

        switch (q.getCommand()){
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
            case FILL:
                cells.set(pos, FILL);
                break;
            case BLANK:
                cells.set(pos, BLANK);
                break;
        }

        currentState = new State(q.getEnd(), cells.get(pos));

    }

    private void moveLeft(){
        pos--;
        if(pos < 0)
            cells.add(0, BLANK);
    }

    private void moveRight(){
        pos++;
        if(pos == cells.size()) {
            cells.add(BLANK);
            cells.add(BLANK);
        }
    }

    public State getCurrentState() {
        return currentState;
    }

    @Override
    public String toString() {
        return cells.toString();
    }

    public int getPos() {
        return pos;
    }

    public char getCurrentCell(){
        return cells.get(pos);
    }

    public String[] pointedAt(int atPos){
        String previous = "";
        String current = "";
        String subsequent = "";

        for(int i=0; i<this.cells.size(); i++){
            if(i<atPos)
                previous += cells.get(i) + " ";
            else if(i == atPos)
                current = String.valueOf(cells.get(i));
            else
                subsequent += cells.get(i) + " ";

        }

        return new String[]{previous, current, subsequent};
    }

    //TODO: check this sometimes
    public List<Integer> currentNumbersOnTape(){
        ArrayList<Integer> res = new ArrayList<>();
        int i = 1;
        int current = 0;

        while(i<cells.size()){
            if(cells.get(i-1) == FILL && cells.get(i) == BLANK){
                res.add(current);
                current = 0;
            } else if(cells.get(i) == FILL){
                current++;
            }
            i++;
        }

        if(current != 0) res.add(current);

        return res;
    }
}
