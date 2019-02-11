package turing;
import java.util.ArrayList;
import java.util.List;

public class Tape {
    private static final char BLANK = 'B';
    private static final char FILL = '1';

    private ArrayList<Character> cells;

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
        if(pos < 0) {
            cells.add(0, BLANK);
            cells.add(0, BLANK);
            pos += 2;
        }
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
        int current = 0;

        for (int i = 0; i<cells.size() ; i++){
            if(cells.get(i) == FILL){
                current++;
            }else if(i != 0 && cells.get(i) == BLANK && current != 0){
                res.add(current);
                current = 0;
            }else{
                System.out.println("Not adding "+current +" from index:" + i);
            }
        }

        if(current != 0) res.add(current);

        return res;
    }
}
