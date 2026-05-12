package ru.itmo.nemat.enigma.model;

import java.util.ArrayList;
import java.util.List;

public class EnigmaMachine {
    private List<Rotor> rotors;
    private Reflector reflector;
    private Plugboard plugboard;

    public EnigmaMachine(
            List<String> rotorNames,
            String reflectorName,
            List<Integer> ringSettings,
            List<String> startPositions,
            List<String[]> plugboardPairs
    ) {
        if (rotorNames.size() != 3) {
            throw new IllegalArgumentException("Модель M3 использует ровно 3 ротора.");
        }

        this.rotors = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.rotors.add(new Rotor(rotorNames.get(i), ringSettings.get(i), startPositions.get(i)));
        }

        this.reflector = new Reflector(reflectorName);
        this.plugboard = new Plugboard();
        if (plugboardPairs != null && !plugboardPairs.isEmpty()) {
            this.plugboard.setConnections(plugboardPairs);
        }
    }

    private void stepRotors() {
        Rotor l = rotors.get(0);
        Rotor m = rotors.get(1);
        Rotor r = rotors.get(2);

        boolean rAtTurnover = r.isAtTurnover();
        boolean mAtTurnover = m.isAtTurnover();

        if (mAtTurnover) {
            l.step();
            m.step();
        } else if (rAtTurnover) {
            m.step();
        }

        r.step();
    }

    public EncryptionResult encodeLetter(char letter) {
        String input = String.valueOf(letter).toUpperCase();
        if (!Character.isLetter(letter)) {
            throw new IllegalArgumentException("Символ '" + letter + "' не является буквой A-Z.");
        }

        List<String> positionsBefore = getRotorPositions();
        stepRotors();

        int idx = input.charAt(0) - 'A';

        idx = plugboard.encode(idx);
        String afterPlugIn = String.valueOf((char) (idx + 'A'));

        idx = rotors.get(2).forward(idx);
        String afterRFwd = String.valueOf((char) (idx + 'A'));
        idx = rotors.get(1).forward(idx);
        String afterMFwd = String.valueOf((char) (idx + 'A'));
        idx = rotors.get(0).forward(idx);
        String afterLFwd = String.valueOf((char) (idx + 'A'));

        idx = reflector.reflect(idx);
        String afterRef = String.valueOf((char) (idx + 'A'));

        // 6-8. Обратный проход (L -> M -> R)
        idx = rotors.get(0).backward(idx);
        String afterLBwd = String.valueOf((char) (idx + 'A'));
        idx = rotors.get(1).backward(idx);
        String afterMBwd = String.valueOf((char) (idx + 'A'));
        idx = rotors.get(2).backward(idx);
        String afterRBwd = String.valueOf((char) (idx + 'A'));

        idx = plugboard.encode(idx);
        String output = String.valueOf((char) (idx + 'A'));

        SignalTrace trace = new SignalTrace(
                input, afterPlugIn, afterRFwd, afterMFwd, afterLFwd,
                afterRef, afterLBwd, afterMBwd, afterRBwd, output, output,
                positionsBefore
        );

        return new EncryptionResult(output, trace);
    }

    public String encrypt(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toUpperCase().toCharArray()) {
            if (Character.isLetter(c)) {
                result.append(encodeLetter(c).letter());
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public EncryptWithTraceResult encryptWithTrace(String text) {
        StringBuilder result = new StringBuilder();
        List<SignalTrace> traces = new ArrayList<>();

        for (char c : text.toUpperCase().toCharArray()) {
            if (Character.isLetter(c)) {
                EncryptionResult res = encodeLetter(c);
                result.append(res.letter());
                traces.add(res.trace());
            }
        }
        return new EncryptWithTraceResult(result.toString(), traces);
    }

    public void reset(List<String> startPositions) {
        for (int i = 0; i < rotors.size(); i++) {
            rotors.get(i).setPosition(startPositions.get(i));
        }
    }

    public List<String> getRotorPositions() {
        return List.of(rotors.get(0).getPositionLetter(), rotors.get(1).getPositionLetter(), rotors.get(2).getPositionLetter());
    }

    public record EncryptionResult(String letter, SignalTrace trace) {}
    public record EncryptWithTraceResult(String text, List<SignalTrace> traces) {}
}