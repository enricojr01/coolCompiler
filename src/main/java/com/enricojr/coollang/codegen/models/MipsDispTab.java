package com.enricojr.coollang.codegen.models;

import java.util.ArrayList;

public class MipsDispTab {
    private String label;
    private ArrayList<String> methods;

    public MipsDispTab(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
