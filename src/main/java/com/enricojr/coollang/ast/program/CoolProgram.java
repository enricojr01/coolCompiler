package com.enricojr.coollang.ast.program;

import java.util.ArrayList;

public class CoolProgram extends CoolBaseNode {
    private ArrayList<CoolClass> classes;

    public CoolProgram() {}

    public ArrayList<CoolClass> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<CoolClass> classes) {
        this.classes = classes;
    }
}
