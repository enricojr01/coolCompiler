package com.enricojr.coollang.codegen.models;

public class MipsIntConst extends MipsBaseConst {
    private int size = 4;
    private int value;

    public MipsIntConst(String label, int tag, int value) {
        super(label, tag);
        this.value = value;
    }

    public String toCode() {
        StringBuilder sb = new StringBuilder();

        // GC tag
        sb.append(".word -1\n");

        // label
        sb.append(String.format("%s:\n", this.getLabel()));

        // class tag
        sb.append(String.format(".word %s\n", this.getTag()));

        // size of objects in words - fixed to 4 for ints basically
        sb.append(String.format(".word %s\n", this.size));

        // pointer to dispatch table
        sb.append(".word Int_dispTab\n");

        // actual value
        sb.append(String.format(".word %s\n", this.value));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "<MipsIntConst: " + this.getLabel() + ">";
    }
}
