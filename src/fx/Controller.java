package fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import turing.Program;
import turing.Tape;
import turing.TuringMachine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Controller implements Initializable {

    private static final String ABOUT_CONTEXT_MESSAGE =
            "You can find the latest source code on Github @baksha97."
            + "\nhttps://github.com/baksha97/TuringMachineEditor";
    private static final String SELECTED_CELL_STYLE = "-fx-text-fill: green; -fx-font-size: 32px;";
    private static final String UNSELECTED_CELL_STYLE = "-fx-font: 16 arial;";
    private static final String DEFAULT_PROGRAM_NAME = "current-program.txt";

    private TuringMachine tm;
    private FileChooser fileChooser;

    @FXML
    public TextField inputField;
    public TextArea programArea;
    public Label stateLabel;
    public Label prevQuadLabel;
    public Label nextQuadLabel;
    public Label currentNumsLabel;
    public TextFlow tapeFlow;
    public Label countLabel;
    public TextArea outputArea;
    public TextField stepByField;


    //Execution Buttons
    public void onSetClick() {
        saveEditor();
        outputArea.setText("");
        if(!setupTuringMachine()) return;
        updateInterface();
    }

    public void onRunClick() {
        saveEditor();
        tm.changeProgram(new Program(programArea.getText().trim()));
        while (tm.hasNextQuadruple()) {
            tm.executeNextQuadruple();
        }
        updateInterface();
    }

    public void onStepClick() {
        saveEditor();
        tm.changeProgram(new Program(programArea.getText().trim()));
        try {
            int steps = Integer.valueOf(stepByField.getText().trim());
            for (int i = 0; i < steps && tm.hasNextQuadruple(); i++) {
                tm.executeNextQuadruple();
            }
        } catch (Exception e) {
            e.printStackTrace();
            outputArea.appendText("Invalid onStepClick count input.\n");
        }
        updateInterface();
    }

    public void keyPressed(KeyEvent e){
        if(e.getCode().equals(KeyCode.ENTER)){
            onStepClick();
        }
    }


    //Initialize Turing Machine
    private boolean inputIsTape(){
        return inputField.getText().trim().contains("B");
    }

    private boolean setupTuringMachine(){
        Program p = new Program(programArea.getText().trim());
        Tape tape;
        try {
            if (inputIsTape()) tape = new Tape(inputField.getText().trim());
            else {
                tape = new Tape(Arrays.stream(inputField.getText().trim().split(","))
                        .mapToInt(s -> Integer.parseInt(s.trim())).toArray());
            }
            tm = new TuringMachine(p, tape);
            return true;
        }catch (Exception e){
            outputArea.setText("Invalid input.\n");
            return false;
        }
    }


    //Configure display
    private void updateInterface(){
        String prevQuadString =(tm.getPreviousQuadruple() != null) ? tm.getPreviousQuadruple().toString() : "Not executed";
        prevQuadLabel.setText(prevQuadString);
        setTapeFlow();
        nextQuadLabel.setText(((tm.nextQuadruple()) != null) ? tm.nextQuadruple().toString() : "None Available");
        currentNumsLabel.setText(tm.numbersOnTape().toString());
        if (tm.hasNextQuadruple()) {
            stateLabel.setText(tm.getTapeState().toString());
            outputArea.appendText("Execution #" + tm.getExecutionCount() + " on:\n");
            outputArea.appendText(tm.toString() + "\n");
            outputArea.appendText(prevQuadString +"\n\n");
        }else{
            nextQuadLabel.setText("None Available");
            stateLabel.setText("MACHINE HALTED @" + tm.getTapeState());
        }
    }

    private void setTapeFlow() {
        tapeFlow.getChildren().clear();
        Tape.TapePartition parts = tm.getTapePartition();
        Text prev = new Text(parts.getLeft());
        Text cur = new Text(parts.getPosition());
        Text sub = new Text(parts.getRight());
        prev.setStyle(UNSELECTED_CELL_STYLE);
        cur.setStyle(SELECTED_CELL_STYLE);
        sub.setStyle(UNSELECTED_CELL_STYLE);
        tapeFlow.getChildren().addAll(prev, cur, sub);
        countLabel.setText(String.valueOf(tm.getExecutionCount()));
    }


    //Menu Buttons
    public void onOpenClick() {
        File picked = fileChooser.showOpenDialog(null);
        if (picked == null) return;
        load_file(picked.getAbsolutePath());
    }

    public void onSaveAsClick() {
        File file = fileChooser.showSaveDialog(null);
        if (file == null) return;
        save_file(file.getAbsolutePath());
    }

    public void onAboutClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, ABOUT_CONTEXT_MESSAGE);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("computer_icon.png"));
        alert.show();
    }

    //File editor management
    private void saveEditor() {
        save_file(DEFAULT_PROGRAM_NAME);
    }

    private void save_file(String location) {
        List<String> lines = new ArrayList<>();
        Scanner prg = new Scanner(programArea.getText());
        while (prg.hasNextLine()) {
            String line = prg.nextLine();
            lines.add(line);
        }

        Path file = Paths.get(location);
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load_file(String fileLocation) {
        programArea.setText("");
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileLocation));
            for (String line : lines) {
                programArea.appendText(line + "\n");
            }
        } catch (IOException e) {
            outputArea.setText("No program found.");
        }
    }

    private void initializeFileChooser(){
        fileChooser = new FileChooser();
        fileChooser.setTitle("File Management");
        fileChooser.setInitialDirectory(Paths.get(".").toFile());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeFileChooser();
        load_file(DEFAULT_PROGRAM_NAME);
    }
}
