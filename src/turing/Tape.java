package turing;

import java.util.ArrayList;
import java.util.List;

public class Tape {
    private static final char BLANK = 'B';
    private static final char FILL = '1';

    private ArrayList<Character> cells;

    private int pos;
    private State currentState;

    private Tape() {}

    public Tape(int... inputs) {
        initialize();
        for (int input : inputs) {
            cells.addAll(intInputAsFill(input));
            cells.add(BLANK);
        }
    }

    public Tape(String input) {
        initialize();

        int charCheck = 0;
        while (input.charAt(charCheck) == BLANK) charCheck++;

        for (int i = charCheck; i < input.length(); i++) {
            char cur = input.charAt(i);
            if (cur == ' ' || cur == ',') continue;
            if (cur != FILL && cur != BLANK)
                throw new IllegalArgumentException("Tape input is not valid for: " + cur);
            cells.add(cur);
        }
        cells.add(BLANK);
    }

    private void initialize() {
        cells = new ArrayList<>();
        cells.add(BLANK);
        pos = 0;
        currentState = new State("1", BLANK);
    }

    private List<Character> intInputAsFill(int x) {
        ArrayList<Character> list = new ArrayList<>(x);
        for (int i = 0; i < x; i++) {
            list.add(FILL);
        }
        return list;
    }

    void execute(Quadruple q) {
        if (!currentState.equals(q.getStartingState()))
            throw new IllegalArgumentException("This function cannot be executed because it does not match state.");

        switch (q.getCommand()) {
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

    private void moveLeft() {
        pos--;
        if (pos == 0) {
            cells.add(0, BLANK);
            pos ++;
        }
    }

    private void moveRight() {
        pos++;
        if (pos == cells.size() - 1) {
            cells.add(BLANK);
        }
    }

    State getCurrentState() {
        return currentState;
    }

    @Override
    public String toString() {
        return cells.toString();
    }

    private int getPos() {
        return pos;
    }

    Partition getTapePartition() {
        return new Partition(getPos(), cells);
    }

    List<Integer> currentNumbersOnTape() {
        ArrayList<Integer> res = new ArrayList<>();
        int current = 0;

        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i) == FILL) {
                current++;
            } else if (i != 0 && cells.get(i) == BLANK && current != 0) {
                res.add(current);
                current = 0;
            }
        }

        if (current != 0) res.add(current);

        return res;
    }


    public static class Partition {

        private final String left;
        private final String position;
        private final String right;

        Partition(int atPos, ArrayList<Character> cells) {
            StringBuilder previous = new StringBuilder();
            String current = "";
            StringBuilder subsequent = new StringBuilder();

            for (int i = 0; i < cells.size(); i++) {
                if (i < atPos)
                    previous.append(cells.get(i)).append(" ");
                else if (i == atPos)
                    current = String.valueOf(cells.get(i));
                else
                    subsequent.append(cells.get(i)).append(" ");

            }
            left = previous.toString();
            position = current;
            right = subsequent.toString();
        }

        public String getLeft() {
            return left;
        }

        public String getPosition() {
            return position;
        }

        public String getRight() {
            return right;
        }

        public String toString() {
            return left + " || " + position + " || " + right;
        }
    }
}
