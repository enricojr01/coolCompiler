package com.enricojr.coollang.ast.program;

import java.util.ArrayList;

public class CoolProgram extends CoolBaseNode {
    // TODO: maybe change to a hashmap for faster lookup?
    private ArrayList<CoolClass> classes;

    public CoolProgram() {}

    public ArrayList<CoolClass> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<CoolClass> classes) {
        this.classes = classes;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (CoolClass cc : this.classes) {
            sb.append(String.format("[%s]\n", cc));
        }

        return sb.toString();
    }

    public boolean findClass(String name) {
        for (CoolClass cc : this.classes) {
            if (cc.getName().getValue().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
