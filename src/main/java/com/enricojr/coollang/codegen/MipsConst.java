package com.enricojr.coollang.codegen;

public class MipsConst {
    private String label;
    private int id;
    private int size;
    private int dispatchTable;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDispatchTable() {
        return dispatchTable;
    }

    public void setDispatchTable(int dispatchTable) {
        this.dispatchTable = dispatchTable;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.label).append(":\n");
        sb.append(".word ").append(this.id).append("\n");
        sb.append(".word ").append(this.size).append("\n");
        sb.append(".word ").append(this.dispatchTable).append("\n");
        return sb.toString();
    }
}
