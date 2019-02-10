package turing;

public class State {
    private String name;
    private char value;

    public State(String n, char v){
        this.name = n;
        this.value = v;
    }

    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o instanceof State) {
            State s = (State) o;
            return name.equals(s.name) && value == s.value;
        }
        return false;
    }

    public int hashCode(){
        return toString().hashCode();
    }

    public String toString(){
        return "(Q" + name + ": @" +  value +")";
    }

}
