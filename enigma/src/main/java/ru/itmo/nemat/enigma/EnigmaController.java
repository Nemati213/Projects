package ru.itmo.nemat.enigma;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import ru.itmo.nemat.enigma.model.EnigmaMachine;
import ru.itmo.nemat.enigma.model.SignalTrace;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class EnigmaController {

    @FXML private TextArea traceArea;
    @FXML private TextField outputArea;
    @FXML private Button openPlugboardBtn;
    @FXML private Button resetBtn;

    @FXML private ComboBox<Integer> ring1Selector, ring2Selector, ring3Selector;

    @FXML private Label rotor1Top, rotor1Active, rotor1Bottom;
    @FXML private Label rotor2Top, rotor2Active, rotor2Bottom;
    @FXML private Label rotor3Top, rotor3Active, rotor3Bottom;
    @FXML private Button rotor1UpBtn, rotor1DownBtn;
    @FXML private Button rotor2UpBtn, rotor2DownBtn;
    @FXML private Button rotor3UpBtn, rotor3DownBtn;

    @FXML private StackPane lampQ, lampW, lampE, lampR, lampT, lampZ, lampU, lampI, lampO;
    @FXML private StackPane lampA, lampS, lampD, lampF, lampG, lampH, lampJ, lampK;
    @FXML private StackPane lampP, lampY, lampX, lampC, lampV, lampB, lampN, lampM, lampL;

    @FXML private Button btnQ, btnW, btnE, btnR, btnT, btnZ, btnU, btnI, btnO;
    @FXML private Button btnA, btnS, btnD, btnF, btnG, btnH, btnJ, btnK;
    @FXML private Button btnP, btnY, btnX, btnC, btnV, btnB, btnN, btnM, btnL;

    @FXML private ComboBox<String> rotor1Selector, rotor2Selector, rotor3Selector;

    private EnigmaMachine machine;
    private StackPane currentLitLamp = null;
    private final Map<Character, StackPane> lampsMap = new HashMap<>();
    private final List<String[]> plugboardPairs = new ArrayList<>(); // Храним кабели

    @FXML
    public void initialize() {
        List<String> availableRotors = List.of("I", "II", "III", "IV", "V");
        rotor1Selector.getItems().addAll(availableRotors);
        rotor2Selector.getItems().addAll(availableRotors);
        rotor3Selector.getItems().addAll(availableRotors);
        rotor1Selector.setValue("I");
        rotor2Selector.setValue("II");
        rotor3Selector.setValue("III");

        List<Integer> rings = new ArrayList<>();
        for (int i = 1; i <= 26; i++) rings.add(i);
        ring1Selector.getItems().addAll(rings);
        ring2Selector.getItems().addAll(rings);
        ring3Selector.getItems().addAll(rings);
        ring1Selector.setValue(1);
        ring2Selector.setValue(1);
        ring3Selector.setValue(1);

        rotor1Selector.setOnAction(e -> initMachine());
        rotor2Selector.setOnAction(e -> initMachine());
        rotor3Selector.setOnAction(e -> initMachine());

        ring1Selector.setOnAction(e -> initMachine());
        ring2Selector.setOnAction(e -> initMachine());
        ring3Selector.setOnAction(e -> initMachine());

        initMachine();
        mapLamps();
        setupKeyboard();
        setupRotorButtons();
        openPlugboardBtn.setOnAction(e -> openPlugboardDialog());
        resetBtn.setOnAction(e -> resetMachine());
        updateRotorUI();
    }

    private void initMachine() {
        List<String> currentPositions = (machine == null)
                ? List.of("A", "A", "A")
                : machine.getRotorPositions();

        int r1 = ring1Selector.getValue() != null ? ring1Selector.getValue() : 1;
        int r2 = ring2Selector.getValue() != null ? ring2Selector.getValue() : 1;
        int r3 = ring3Selector.getValue() != null ? ring3Selector.getValue() : 1;

        machine = new EnigmaMachine(
                List.of(rotor1Selector.getValue(), rotor2Selector.getValue(), rotor3Selector.getValue()),
                "UKW-B",
                List.of(r1, r2, r3),
                currentPositions,
                plugboardPairs
        );

        if (rotor1Active != null) updateRotorUI();
    }
    private void handleKeyPress(char key) {
        EnigmaMachine.EncryptionResult result = machine.encodeLetter(key);

        String encryptedStr = result.letter();
        SignalTrace t = result.trace();

        outputArea.setText(outputArea.getText() + encryptedStr);

        StackPane targetLamp = lampsMap.get(encryptedStr.charAt(0));
        if (targetLamp != null) {
            targetLamp.getStyleClass().add("lamp-on");
            currentLitLamp = targetLamp;
        }

        if (t != null) {
            String logEntry = String.format(
                    "Input: %s -> Output: %s\n" +
                            "  Stecker: %s\n" +
                            "  Rotors Forward:  [%s] -> [%s] -> [%s]\n" +
                            "  Reflector: %s\n" +
                            "  Rotors Backward: [%s] -> [%s] -> [%s]\n" +
                            "--------------------\n",
                    t.inputLetter(), t.outputLetter(),
                    t.afterPlugboardIn(),
                    t.afterRotorRFwd(), t.afterRotorMFwd(), t.afterRotorLFwd(),
                    t.afterReflector(),
                    t.afterRotorLBwd(), t.afterRotorMBwd(), t.afterRotorRBwd()
            );
            traceArea.appendText(logEntry);
        }

        updateRotorUI();
    }

    @FXML
    private void clearTrace() {
        traceArea.clear();
    }

    private void setRotorLabels(char activeLetter, Label top, Label active, Label bottom) {
        active.setText(String.valueOf(activeLetter));
        top.setText(String.valueOf((char) ((activeLetter - 'A' + 25) % 26 + 'A')));
        bottom.setText(String.valueOf((char) ((activeLetter - 'A' + 1) % 26 + 'A')));
    }

    private void setupRotorButtons() {
        rotor1UpBtn.setOnAction(e -> shiftRotor(0, 1));
        rotor1DownBtn.setOnAction(e -> shiftRotor(0, -1));
        rotor2UpBtn.setOnAction(e -> shiftRotor(1, 1));
        rotor2DownBtn.setOnAction(e -> shiftRotor(1, -1));
        rotor3UpBtn.setOnAction(e -> shiftRotor(2, 1));
        rotor3DownBtn.setOnAction(e -> shiftRotor(2, -1));
    }

    private void shiftRotor(int index, int direction) {
        List<String> currentPos = machine.getRotorPositions();

        List<String> mutablePos = new ArrayList<>(currentPos);

        char currentChar = mutablePos.get(index).charAt(0);
        char nextChar = (char) ((currentChar - 'A' + direction + 26) % 26 + 'A');

        mutablePos.set(index, String.valueOf(nextChar));
        machine.reset(mutablePos);

        updateRotorUI();
    }

    private void updateRotorUI() {
        List<String> pos = machine.getRotorPositions();

        setRotorLabels(pos.get(0).charAt(0), rotor1Top, rotor1Active, rotor1Bottom);

        setRotorLabels(pos.get(1).charAt(0), rotor2Top, rotor2Active, rotor2Bottom);

        setRotorLabels(pos.get(2).charAt(0), rotor3Top, rotor3Active, rotor3Bottom);
    }

    private void openPlugboardDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Plugboard Connection");
        dialog.setHeaderText("Add Cable");
        dialog.setContentText("Enter two letters:");

        dialog.setGraphic(null);

        try {
            String css = getClass().getResource("/styles.css").toExternalForm();
            dialog.getDialogPane().getStylesheets().add(css);
        } catch (NullPointerException e) {
            System.out.println("Файл CSS не найден, проверьте путь!");
        }

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            String clean = input.toUpperCase().replaceAll("\\s+", "");
            if (clean.length() == 2 && Character.isLetter(clean.charAt(0)) && Character.isLetter(clean.charAt(1))) {
                plugboardPairs.add(new String[]{String.valueOf(clean.charAt(0)), String.valueOf(clean.charAt(1))});
                initMachine();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Cable connected");
                alert.setContentText("Connection: " + clean.charAt(0) + " - " + clean.charAt(1));
                alert.setGraphic(null);

                try {
                    alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
                } catch (Exception e) {}

                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Enter exactly two letters.");
                alert.setGraphic(null);

                try {
                    alert.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
                } catch (Exception e) {}

                alert.show();
            }
        });
    }
    private void resetMachine() {
        outputArea.setText("");
        if (currentLitLamp != null) {
            currentLitLamp.getStyleClass().remove("lamp-on");
            currentLitLamp = null;
        }
        plugboardPairs.clear();
        initMachine();
        updateRotorUI();
    }

    private void mapLamps() {
        lampsMap.put('Q', lampQ); lampsMap.put('W', lampW); lampsMap.put('E', lampE); lampsMap.put('R', lampR);
        lampsMap.put('T', lampT); lampsMap.put('Z', lampZ); lampsMap.put('U', lampU); lampsMap.put('I', lampI); lampsMap.put('O', lampO);
        lampsMap.put('A', lampA); lampsMap.put('S', lampS); lampsMap.put('D', lampD); lampsMap.put('F', lampF);
        lampsMap.put('G', lampG); lampsMap.put('H', lampH); lampsMap.put('J', lampJ); lampsMap.put('K', lampK);
        lampsMap.put('P', lampP); lampsMap.put('Y', lampY); lampsMap.put('X', lampX); lampsMap.put('C', lampC);
        lampsMap.put('V', lampV); lampsMap.put('B', lampB); lampsMap.put('N', lampN); lampsMap.put('M', lampM); lampsMap.put('L', lampL);
    }

    private void setupKeyboard() {
        Button[] keys = {btnQ, btnW, btnE, btnR, btnT, btnZ, btnU, btnI, btnO,
                btnA, btnS, btnD, btnF, btnG, btnH, btnJ, btnK,
                btnP, btnY, btnX, btnC, btnV, btnB, btnN, btnM, btnL};

        for (Button btn : keys) {
            btn.setOnMousePressed(e -> handleKeyPress(btn.getText().charAt(0)));
            btn.setOnMouseReleased(e -> {
                if (currentLitLamp != null) {
                    currentLitLamp.getStyleClass().remove("lamp-on");
                    currentLitLamp = null;
                }
            });
        }
    }

}