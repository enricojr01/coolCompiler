package com.enricojr.coollang.codegen.models;

import java.util.ArrayList;

public class MipsAlignedTable {
    private final MipsBaseConst[] internal;
    private final String label;

    public MipsAlignedTable(String label, int size) {
        this.internal = new MipsBaseConst[size];
        this.label = label;
    }

    public void insert(MipsBaseConst obj, int pos) {
        this.internal[pos] = obj;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s:\n", this.label));

        for (MipsBaseConst mbc : this.internal) {
            sb.append(String.format(".word %s\n", mbc.getLabel()));
        }

        return sb.toString();
    }
}
