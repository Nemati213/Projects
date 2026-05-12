package ru.itmo.nemat.enigma.model;

import static ru.itmo.nemat.enigma.model.Rotors.REFLECTOR_WIRINGS;

public class Reflector {
    public String name;
    public String wiring;
    private int[] _table;

    public Reflector(String name) {
        if (!REFLECTOR_WIRINGS.containsKey(name)) {
            throw new IllegalArgumentException(
                    "Неизвестный рефлектор: " + name + ". Допустимые: " + REFLECTOR_WIRINGS.keySet()
            );
        }
        this.name = name;
        this.wiring = REFLECTOR_WIRINGS.get(name);

        this._table = new int[this.wiring.length()];
        for (int i = 0; i < this.wiring.length(); i++) {
            this._table[i] = this.wiring.charAt(i) - 'A';
        }
    }

    public int reflect(int index) {
        return this._table[index];
    }

    @Override
    public String toString() {
        return "Reflector(" + this.name + ")";
    }
}