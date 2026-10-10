package com.enricojr.coollang.codegen.models;

import java.util.ArrayList;

public class MipsDispTab extends MipsBaseConst {
    private ArrayList<String> methodNames = new ArrayList<>();

    // IMPLEMENTATION NOTE: method names need to be listed in "reverse order",
    // i.e. the first methods in the table should be Object, and then the next
    // child of object, and the next one, and so on until we get to the target class
    // itself.
    // I've decided as of right now that the CoolClass itself will be responsible for putting
    // everything in the correct order to simplify this class.
    public MipsDispTab(String label) {
        super(label, -1);
    }

    public void addMethodName(String fullyQualifiedMethodName) {
        String instr = ".word " + fullyQualifiedMethodName;
        this.methodNames.add(instr);
    }

    public void addMethodNames(ArrayList<String> methodNames) {
        this.methodNames.addAll(methodNames);
    }

    public String toCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getLabel()).append("_dispTab:").append("\n");

        for (String mName : this.methodNames) {
            sb.append(".word ").append(mName).append("\n");
        }

        return sb.toString();
    }

    public String toString() {
        return "<MipsDispTab>";
    }
}
