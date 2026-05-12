package ru.itmo.nemat.enigma.model;

import static ru.itmo.nemat.enigma.model.Rotors.ROTOR_TURNOVERS;
import static ru.itmo.nemat.enigma.model.Rotors.ROTOR_WIRINGS;

public class Rotor {
    public String name;
    public String wiring;
    public String turnover;
    public int ringSetting;
    public int position;

    private int[] _forward;
    private int[] _backward;

    public Rotor(String name, int ringSetting, String position) {
        if (!ROTOR_WIRINGS.containsKey(name)) {
            throw new IllegalArgumentException("Неизвестный ротор: " + name);
        }

        this.name = name;
        this.wiring = ROTOR_WIRINGS.get(name);
        this.turnover = ROTOR_TURNOVERS.get(name);
        this.ringSetting = ringSetting - 1;
        this.position = Character.toUpperCase(position.charAt(0)) - 'A';

        this._forward = new int[26];
        this._backward = new int[26];

        for (int i = 0; i < 26; i++) {
            int v = this.wiring.charAt(i) - 'A';
            this._forward[i] = v;
            this._backward[v] = i;
        }
    }

    public void step() {
        this.position = (this.position + 1) % 26;
    }

    public boolean isAtTurnover() {
        return String.valueOf((char) (this.position + 'A')).equals(this.turnover);
    }

    private int _encode(int[] table, int index) {
        int offset = (this.position - this.ringSetting) % 26;
        if (offset < 0) offset += 26;

        int shiftedIn = (index + offset) % 26;
        int output = table[shiftedIn];

        int res = (output - offset) % 26;
        if (res < 0) res += 26;

        return res;
    }

    public int forward(int index) {
        return this._encode(this._forward, index);
    }

    public int backward(int index) {
        return this._encode(this._backward, index);
    }

    public String getPositionLetter() {
        return String.valueOf((char) (this.position + 'A'));
    }

    public void setPosition(String letter) {
        this.position = Character.toUpperCase(letter.charAt(0)) - 'A';
    }

    @Override
    public String toString() {
        return String.format("Rotor(%s, pos=%s, ring=%d)",
                this.name, this.getPositionLetter(), this.ringSetting + 1);
    }
}