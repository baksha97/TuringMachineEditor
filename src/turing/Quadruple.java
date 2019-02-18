package turing;

public class Quadruple {

    public enum Command{
        LEFT, RIGHT, FILL, BLANK;

        static Command fromString(String s){
            switch (s){
                case "L": return LEFT;
                case "R": return RIGHT;
                case "1": return FILL;
                case "B": return BLANK;
                default: throw new IllegalArgumentException("Command not valid: " + s);
            }
        }
    }

    private final String start;
    private final char conditional;

    private final Command command;
    private final String end;

    private final State startingState;

    public Quadruple(String line){
        String[] partition = line.split(",", 4); //ex: q1,B,R,q2
        this.start = partition[0];
        this.conditional = partition[1].charAt(0);
        this.command = Command.fromString(partition[2]);
        this.end = partition[3];

        this.startingState = new State(start, conditional);
    }

    public String getStart() {
        return start;
    }

    public char getConditional() {
        return conditional;
    }

    public Command getCommand() {
        return command;
    }

    public String getEnd() {
        return end;
    }

    public State getStartingState() {
        return startingState;
    }

    public String toString(){
        return "(Q" + start + "," + conditional + "," + command + ",Q" + end + ")";
    }
}
