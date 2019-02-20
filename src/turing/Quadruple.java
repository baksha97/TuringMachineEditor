package turing;

public class Quadruple {

    private final String start;
    private final char conditional;
    private final Command command;
    private final String end;
    private final State startingState;

    public Quadruple(String line) {
        try {
            String[] partition = line.split(",", 4); //ex: q1,B,R,q2
            this.start = partition[0];
            this.conditional = partition[1].charAt(0);
            this.command = Command.fromString(partition[2]);
            this.end = partition[3];
            this.startingState = new State(start, conditional);
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Line does not fit format: " + line);
        }
    }

    Command getCommand() {
        return command;
    }

    String getEnd() {
        return end;
    }

    State getStartingState() {
        return startingState;
    }

    public String toString() {
        return "Q" + start + ", " + conditional + ", " + command + ", Q" + end;
    }

    public enum Command {
        LEFT, RIGHT, FILL, BLANK;

        static Command fromString(String s) {
            switch (s) {
                case "L":
                    return LEFT;
                case "R":
                    return RIGHT;
                case "1":
                    return FILL;
                case "B":
                    return BLANK;
                default:
                    throw new IllegalArgumentException("Command not valid: " + s);
            }
        }

        public String toString() {
            switch (this) {
                case LEFT:
                    return "L";
                case RIGHT:
                    return "R";
                case FILL:
                    return "1";
                case BLANK:
                    return "B";
            }
            return null;
        }
    }
}
