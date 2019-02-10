package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import turing.Program;
import turing.TuringMachine;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Controller implements Initializable {
    @FXML
    public TextField inputField;
    public TextArea programArea;
    public Label stateLabel;
    public Label prevQuadLabel;
    public Label nextQuadLabel;
    public Label currentNumsLabel;
    public TextFlow tapeFlow;

    private TuringMachine tm;

    public void step(){
        saveEditor();
        tm.changeProgram(new Program(programArea.getText().trim()));
        if(tm.hasNextQuadruple()){
            prevQuadLabel.setText(tm.nextQuadruple().toString());
            tm.executeNextQuadruple();
            setExecutionArea();
            nextQuadLabel.setText(((tm.nextQuadruple()) != null) ? tm.nextQuadruple().toString() : "None Available");
            stateLabel.setText(tm.getTapeState().toString());
            currentNumsLabel.setText(tm.numbersOnTape().toString());
        }else{
            nextQuadLabel.setText("None Available");
            stateLabel.setText("MACHINE HALTED @" + tm.getTapeState());
        }
    }

    private void setExecutionArea(){
        tapeFlow.getChildren().clear();
        String[] prevCurSub = tm.getTapeDisplay();
        Text prev = new Text(prevCurSub[0]);
        prev.setStyle("-fx-font: 16 arial;");
        Text cur = new Text(prevCurSub[1]);
        cur.setStyle("-fx-text-fill: green; -fx-font-size: 32px;");
        Text sub = new Text(prevCurSub[2]);
        sub.setStyle("-fx-font: 16 arial;");
        tapeFlow.getChildren().addAll(prev, cur, sub);
    }

    public void set(){
        saveEditor();
        int[] numbers = Arrays.asList(inputField.getText().trim().split(","))
                .stream()
                .map(String::trim)
                .mapToInt(Integer::parseInt).toArray();
        Program p = new Program(programArea.getText().trim());
        tm = new TuringMachine(p, numbers);

        stateLabel.setText(tm.getTapeState().toString());
        nextQuadLabel.setText(tm.nextQuadruple().toString());
        setExecutionArea();
        currentNumsLabel.setText(tm.numbersOnTape().toString());
    }


    private void saveEditor(){
        List<String> lines = new ArrayList<>();
        Scanner prg = new Scanner(programArea.getText());
        while(prg.hasNextLine()){
            String line = prg.nextLine();
            lines.add(line);
        }

        Path file = Paths.get("current-program.txt");
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        saveEditor();
        while(tm.hasNextQuadruple()){
            prevQuadLabel.setText(tm.nextQuadruple().toString());
            tm.executeNextQuadruple();
            setExecutionArea();
            nextQuadLabel.setText(((tm.nextQuadruple()) != null) ? tm.nextQuadruple().toString() : "None Available");
            stateLabel.setText(tm.getTapeState().toString());
            currentNumsLabel.setText(tm.numbersOnTape().toString());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
