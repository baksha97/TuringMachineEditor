package turing;

import turing.Quadruple;
import turing.State;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Program {

    private int size;
    private ArrayList<Quadruple> quadruples;

    private HashMap<State, Quadruple> quadrupleStates;

    public Program(File f) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(f.getAbsolutePath()));
        this.size = lines.size();
        this.quadruples = new ArrayList<>();
        this.quadrupleStates = new HashMap<>();
        setQuadrupleData(lines);
    }

    public Program(String in){
        List<String> lines = new ArrayList<>();
        Scanner prg = new Scanner(in);
        while(prg.hasNextLine()){
            String line = prg.nextLine().trim();
            if(line.contains("//") || line.equals("")) continue;
            lines.add(line);
        }
        prg.close();
        this.size = lines.size();
        this.quadruples = new ArrayList<>();
        this.quadrupleStates = new HashMap<>();
        setQuadrupleData(lines);
    }

    private void setQuadrupleData(List<String> lines){
        for(String l: lines){
            Quadruple q = new Quadruple((l));
            if(!quadrupleStates.containsKey(q.getStartingState())){
                quadruples.add(q);
                quadrupleStates.put(q.getStartingState(), q);
            }else{
                System.out.println("You seem to have two of the same states in your input.");
            }
        }
    }

    public int size(){
        return this.size();
    }

    public ArrayList<Quadruple> getQuadruples() {
        return quadruples;
    }

    public HashMap<State, Quadruple> getQuadrupleStates() {
        return quadrupleStates;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder(size);
        sb.append("Quadruple Count: ").append(size).append('\n');

        for (Quadruple q: quadruples){
            sb.append(q.toString()).append('\n');
        }
        return sb.toString();
    }

}
