package com.enricojr.coollang.codegen.models;

import java.util.ArrayList;

public class MipsFunctionObj extends MipsBaseConst {
    private String functionName;
    private ArrayList<String> commands = new ArrayList<>();

    public MipsFunctionObj(String name) {
        this.functionName = name;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public void addCommand(String command) {
        this.commands.add(command);
    }

    public String toCode() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.functionName).append(":\n");

        return sb.toString();
    }
}
