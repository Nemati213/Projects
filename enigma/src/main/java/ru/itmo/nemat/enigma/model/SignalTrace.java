package ru.itmo.nemat.enigma.model;

import java.util.List;

public record SignalTrace(
        String inputLetter,
        String afterPlugboardIn,
        String afterRotorRFwd,
        String afterRotorMFwd,
        String afterRotorLFwd,
        String afterReflector,
        String afterRotorLBwd,
        String afterRotorMBwd,
        String afterRotorRBwd,
        String afterPlugboardOut,
        String outputLetter,
        List<String> rotorPositions
) {}