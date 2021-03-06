package turing;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Tape {
    private static final char BLANK = 'B';
    private static final char FILL = '1';
    private ObservableList<Character> cells;
    private int pos;
    private State currentState;

    private Tape() {
    }

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

    ObservableList<Character> getCells() {
        return cells;
    }

    private void initialize() {
        cells = FXCollections.observableArrayList();
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
            throw new IllegalArgumentException("This function cannot be executed because it does not match state."
                                                +"\nState: "  + currentState
                                                +"\nQuadruple: " + q.getStartingState()
            );

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
        if (pos <= 0) {
            cells.add(0, BLANK);
            pos++;
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
        String s = cells.toString()
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")
                .replace(" ", "");

        return s.substring(0, getPos()) + " || " + s.charAt(getPos()) + " || " + s.substring(getPos() + 1);
    }

    int getPos() {
        return pos;
    }


    List<Integer> currentNumbersOnTape() {
        ArrayList<Integer> res = new ArrayList<>();
        int current = 0;

        for (Character cell : cells) {
            if (cell == FILL) {
                current++;
            } else if (current != 0 && cell == BLANK) {
                res.add(current);
                current = 0;
            }
        }

        if (current != 0) res.add(current);

        return res;
    }

}
