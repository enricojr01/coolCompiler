package com.enricojr.coollang.codegen.models;

import java.util.ArrayList;

public class MipsProtObj extends MipsBaseConst {
    private int size;
    private MipsDispTab dispTab;
    private ArrayList<String> attribs = new ArrayList<>();

    public MipsProtObj(String label, int tag, MipsDispTab tab, ArrayList<String> attribs) {
        super(label, tag);
        this.dispTab = tab;
        this.attribs.addAll(attribs);
    }

    public MipsDispTab getDispTab() {
        return this.dispTab;
    }

    private int computeSize() {
        // size = size of object in # of words.
        // 3 words for the "header" - tag, size field, ptr to dispatch table
        // + attribs
        return 3 + attribs.size();
    }

    public String toString() {
        return "<MipsProtObj " + this.getLabel() + ">";
    }

    // TODO: consider moving this to an interface?
    public String toCode() {
        StringBuilder sb = new StringBuilder();

        // Garbage collector tag
        sb.append(".word -1\n");

        // label
        sb.append(this.getLabel()).append("_protObj\n");

        // object tag
        sb.append(".word ").append(this.getTag()).append("\n");

        // size of obj in words
        sb.append(".word ").append(this.computeSize()).append("\n");

        // ptr to dispatch table
        // this should probably be a constant somewhere but meh
        sb.append(".word ").append(this.getLabel()).append("_dispTab").append("\n");

        // attributes, this should already be in the correct order coming out of the class
        // i.e. grandparent attrs first
        for (String attr : this.attribs) {
            sb.append(".word ").append(attr).append("\n");
        }

        return sb.toString();
    }
}
