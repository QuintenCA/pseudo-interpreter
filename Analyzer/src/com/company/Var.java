package com.company;

public class Var {
    String n;
    int v;

    public Var(){}

    public Var (String name, int value) {
        n = name;
        v = value;
    }

    public int getValue() {
        return v;
    }

    public void setValue(int newValue) {
        v = newValue;
    }
}
