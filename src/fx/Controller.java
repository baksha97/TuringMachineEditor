package fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import turing.Program;
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

    public void step() {
        saveEditor();
        tm.changeProgram(new Program(programArea.getText().trim()));
        try {
            int steps = Integer.valueOf(stepByField.getText().trim());
            for (int i = 0; i < steps; i++) {
                stepAndUpdateUI();
            }
        } catch (Exception e) {
            outputArea.appendText("Invalid step count input.\n");
        }
    }

    public void set() {
        saveEditor();
        Program p = new Program(programArea.getText().trim());
        try {
            int[] numbers = Arrays.asList(inputField.getText().trim().split(","))
                    .stream()
                    .map(String::trim)
                    .mapToInt(Integer::parseInt).toArray();
            tm = new TuringMachine(p, numbers);
        } catch (Exception e) {
            String in = inputField.getText().trim();
            tm = new TuringMachine(p, in);
        }
        stateLabel.setText(tm.getTapeState().toString());
        nextQuadLabel.setText(tm.nextQuadruple().toString());
        setTapeFlow();
        currentNumsLabel.setText(tm.numbersOnTape().toString());
        outputArea.setText("");
        prevQuadLabel.setText("");
    }

    public void run() {
        saveEditor();
        while (tm.hasNextQuadruple()) {
            stepAndUpdateUI();
        }
    }

    public void onOpenClicked() {
        File picked = fileChooser.showOpenDialog(null);
        if (picked == null) return;
        load_file(picked.getAbsolutePath());
    }

    public void saveAsDidClick() {
        File file = fileChooser.showSaveDialog(null);
        if (file == null) return;
        save_file(file.getAbsolutePath());
    }


    public void onAboutClicked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, ABOUT_CONTEXT_MESSAGE);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("computer_icon.png"));
        alert.show();
    }

    private void stepAndUpdateUI() {
        if (tm.hasNextQuadruple()) {
            prevQuadLabel.setText(tm.nextQuadruple().toString());
            outputArea.appendText("Execution #" + (tm.getExecutionCount() + 1) + " on:\n");
            outputArea.appendText(tm.toString() + "\n");
            outputArea.appendText(tm.nextQuadruple().toString() + "\n\n");
            tm.executeNextQuadruple();
            setTapeFlow();
            nextQuadLabel.setText(((tm.nextQuadruple()) != null) ? tm.nextQuadruple().toString() : "None Available");
            stateLabel.setText(tm.getTapeState().toString());
            currentNumsLabel.setText(tm.numbersOnTape().toString());
        } else {
            nextQuadLabel.setText("None Available");
            stateLabel.setText("MACHINE HALTED @" + tm.getTapeState());
        }
    }

    private void setTapeFlow() {
        tapeFlow.getChildren().clear();
        String[] prevCurSub = tm.getTapeDisplay();

        Text prev = new Text(prevCurSub[0]);
        Text cur = new Text(prevCurSub[1]);
        Text sub = new Text(prevCurSub[2]);
        prev.setStyle(UNSELECTED_CELL_STYLE);
        cur.setStyle(SELECTED_CELL_STYLE);
        sub.setStyle(UNSELECTED_CELL_STYLE);
        tapeFlow.getChildren().addAll(prev, cur, sub);
        countLabel.setText(String.valueOf(tm.getExecutionCount()));
    }

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
