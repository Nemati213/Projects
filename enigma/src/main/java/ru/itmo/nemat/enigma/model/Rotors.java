package ru.itmo.nemat.enigma.model;

import java.util.Map;

public class Rotors {
    public static final Map<String, String> ROTOR_WIRINGS = Map.of(
            "I",   "EKMFLGDQVZNTOWYHXUSPAIBRCJ",
            "II",  "AJDKSIRUXBLHWTMCQGZNPYFVOE",
            "III", "BDFHJLCPRTXVZNYEIWGAKMUSQO",
            "IV",  "ESOVPZJAYQUIRHXLNFTGKDCMWB",
            "V",   "VZBRGITYUPSDNHLXAWMJQOFECK"
    );

    public static final Map<String, String> ROTOR_TURNOVERS = Map.of(
            "I",   "Q",
            "II",  "E",
            "III", "V",
            "IV",  "J",
            "V",   "Z"
    );

    public static final Map<String, String> REFLECTOR_WIRINGS = Map.of(
            "UKW-B", "YRUHQSLDPXNGOKMIEBFZCWVJAT",
            "UKW-C", "FVPJIAOYEDRZXWGCTKUQSBNMHL"
    );

    public static final String ETW_WIRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
}