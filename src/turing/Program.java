package turing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Program {

    private final ArrayList<Quadruple> quadruples;

    private final HashMap<State, Quadruple> quadrupleStates;

    public Program(String in) {
        List<String> lines = new ArrayList<>();
        Scanner prg = new Scanner(in);
        while (prg.hasNextLine()) {
            String line = prg.nextLine().trim();
            if (line.contains("//") || line.equals("")) continue;
            lines.add(line);
        }
        prg.close();
        this.quadruples = new ArrayList<>();
        this.quadrupleStates = new HashMap<>();
        setQuadrupleData(lines);
    }

    private void setQuadrupleData(List<String> lines) {
        for (String l : lines) {
            Quadruple q = new Quadruple(l);

            if (quadrupleStates.containsKey(q.getStartingState())) {
                throw new IllegalArgumentException("You seem to have two of the same states in your input.");
            }

            quadruples.add(q);
            quadrupleStates.put(q.getStartingState(), q);
        }
    }

    public int size() {
        return this.quadruples.size();
    }

    HashMap<State, Quadruple> getQuadrupleStates() {
        return quadrupleStates;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Quadruple Count: ").append(size()).append('\n');

        for (Quadruple q : quadruples) {
            sb.append(q.toString()).append('\n');
        }
        return sb.toString();
    }

}
