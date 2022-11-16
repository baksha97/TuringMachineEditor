package fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import turing.Program;
import turing.Tape;
import turing.TuringMachine;
import turing.TuringMachineViewModel;

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
                    + "\n\thttps://github.com/baksha97/TuringMachineEditor"
                    + "\nBe sure to check out any branches for works in progress & please create an issue if a problem is found.";
    private static final String DEFAULT_PROGRAM_NAME = "current-program.txt";

    @FXML
    public TextField inputField;
    public TextArea programArea;
    public Label stateLabel;
    public Label prevQuadLabel;
    public Label nextQuadLabel;
    public Label currentNumsLabel;
    public ListView<Character> listView;
    public Label countLabel;
    public TextArea outputArea;
    public TextField stepByField;
    public Label positionLabel;
    public TextField tapeSizeField;

    private TuringMachine tm;
    private TuringMachineViewModel tmvm;
    private FileChooser fileChooser;
    private int tapeSize;

    //Execution Buttons
    public void onSetClick() {
        outputArea.setText("");
        saveEditor();
        if (!setupTuringMachine()) return;
        updateInterface();
    }

    public void onRunClick() {
        saveEditor();
        if (tm == null) {
            println("Setup first.");
            return;
        }
        updateProgram();
        while (tm.hasNextQuadruple()) {
            tm.executeNextQuadruple();
        }
        updateInterface();
    }

    public void onStepClick() {
        saveEditor();
        if (tm == null) {
            println("Setup first.");
            return;
        }
        updateProgram();
        try {
            int steps = Integer.valueOf(stepByField.getText().trim());
            for (int i = 0; i < steps && tm.hasNextQuadruple(); i++) {
                tm.executeNextQuadruple();
            }
        } catch (Exception e) {
            println("Invalid step count input.");
            println(e.getLocalizedMessage());
        }
        updateInterface();
    }

    //Initialize Turing Machine
    private boolean inputIsTape() {
        return inputField.getText().contains("B");
    }

    private boolean setupTuringMachine() {
        try {
            tapeSize = Integer.parseInt(tapeSizeField.getText());
            Program p = new Program(programArea.getText().trim());
            Tape tape;

            if (inputIsTape()) {
                tape = new Tape(inputField.getText().trim());
            } else {
                tape = new Tape(Arrays.stream(inputField.getText().trim().split(","))
                        .mapToInt(s -> Integer.parseInt(s.trim())).toArray());
            }
            this.tm = new TuringMachine(p, tape);
            this.tmvm = new TuringMachineViewModel(tm);
            setupListViewForTuring();
            return true;
        } catch (Exception e) {
            println("Invalid input.");
            println(e.getLocalizedMessage());
            return false;
        }
    }

    private void setupListViewForTuring() {
        listView.setItems(tm.getObservableList());
        listView.setCellFactory(cell -> new ListCell<Character>() {
            @Override
            protected void updateItem(Character item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.valueOf(item));
                    setFont(Font.font(tapeSize));
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
    }

    //Use new program upon every execution
    private void updateProgram() {
        tm.changeProgram(new Program(programArea.getText().trim()));
    }

    //Configure display
    private void updateInterface() {
        prevQuadLabel.setText(tmvm.getPrevQuad());
        nextQuadLabel.setText(tmvm.getNextQuad());
        currentNumsLabel.setText(tmvm.getNumbersOnTape());
        countLabel.setText(tmvm.getExecutionCount());
        positionLabel.setText(tmvm.getPosition());
        stateLabel.setText(tmvm.getTapeState());

        if (tm.hasNextQuadruple()) {
            println(tmvm.getExecutionCount());
            printlnt(tmvm.getPrevQuad());
            printlnt(tmvm);
            printlnt(tmvm.getNumbersOnTape());
        }

        listView.scrollTo(tm.getPos());
        listView.getSelectionModel().select(tm.getPos());
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
        alert.setHeaderText("About Editor");
        alert.show();
    }

    //Keyboard events
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                if (e.isShiftDown()) onSetClick();
                else onStepClick();
                break;
            case F11:
                updateProgram();
                updateInterface();
                break;
            default:
                break;
        }
    }

    //Output area printing
    private void println(Object o) {
        outputArea.appendText(o + "\n");
    }

    private void printlnt(Object o) {
        println("\t" + o);
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
            println(e.getLocalizedMessage());
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
            println("No program found.");
            println(e.getLocalizedMessage());
        }
    }

    private void initializeFileChooser() {
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


