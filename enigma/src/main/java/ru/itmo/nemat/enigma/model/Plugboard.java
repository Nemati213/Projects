package ru.itmo.nemat.enigma.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Plugboard {
    private int[] table;

    public Plugboard() {
        this.table = new int[26];
        clear();
    }


    public void addConnection(String letterA, String letterB) {
        int a = letterA.toUpperCase().charAt(0) - 'A';
        int b = letterB.toUpperCase().charAt(0) - 'A';

        if (a == b) {
            throw new IllegalArgumentException("Нельзя соединить букву саму с собой.");
        }
        if (table[a] != a) {
            throw new IllegalArgumentException("Буква " + letterA.toUpperCase() + " уже задействована в паре.");
        }
        if (table[b] != b) {
            throw new IllegalArgumentException("Буква " + letterB.toUpperCase() + " уже задействована в паре.");
        }

        table[a] = b;
        table[b] = a;
    }

    public void removeConnection(String letter) {
        int i = letter.toUpperCase().charAt(0) - 'A';
        int j = table[i];
        table[i] = i;
        table[j] = j;
    }

    public void clear() {
        for (int i = 0; i < 26; i++) {
            table[i] = i;
        }
    }

    public void setConnections(List<String[]> pairs) {
        clear();
        try{
            for (String[] pair : pairs) {
                addConnection(pair[0], pair[1]);
            }
        } catch (Exception e) {

        }

    }

    public int encode(int index) {
        return table[index];
    }

    public List<String[]> getPairs() {
        Set<Integer> seen = new HashSet<>();
        List<String[]> pairs = new ArrayList<>();

        for (int i = 0; i < 26; i++) {
            int j = table[i];
            if (i != j && !seen.contains(i)) {
                pairs.add(new String[]{
                        String.valueOf((char) (i + 'A')),
                        String.valueOf((char) (j + 'A'))
                });
                seen.add(i);
                seen.add(j);
            }
        }
        return pairs;
    }

    @Override
    public String toString() {
        List<String[]> pairs = getPairs();
        StringBuilder sb = new StringBuilder("Plugboard([");
        for (int i = 0; i < pairs.size(); i++) {
            sb.append("(").append(pairs.get(i)[0]).append(", ").append(pairs.get(i)[1]).append(")");
            if (i < pairs.size() - 1) sb.append(", ");
        }
        sb.append("])");
        return sb.toString();
    }
}