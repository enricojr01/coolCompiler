package com.enricojr.coollang.codegen.models;

import java.util.ArrayList;

public class MipsFunctionObj extends MipsBaseConst {
    private ArrayList<String> commands = new ArrayList<>();

    public MipsFunctionObj(String name) {
        super(name, -1);
    }

    public void addCommand(String command) {
        this.commands.add(command);
    }

    public String toCode() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.getLabel()).append(":\n");

        return sb.toString();
    }
}
