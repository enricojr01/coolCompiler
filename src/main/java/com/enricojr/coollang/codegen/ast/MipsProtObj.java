package com.enricojr.coollang.codegen.ast;

import com.enricojr.coollang.codegen.ast.consts.MipsBaseConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MipsProtObj extends MipsBaseConst {
    private MipsDispTab dispTab;
    private List<String> attribTab;
    private HashMap<String, String> attribDefaults;

    public MipsProtObj(
            String label,
            int tag,
            MipsDispTab dispTab,
            List<String> attribTab,
            HashMap<String, String> attribDefaults
    ) {
        super(label, tag);
        this.dispTab = dispTab;
        this.attribTab = attribTab;
        this.attribDefaults = attribDefaults;
    }

    private int computeSize() {
        // size = size of object in # of words.
        // 3 words for the "header" - tag, size field, ptr to dispatch table
        // + attribs
        return 3 + this.attribTab.size();
    }

    public MipsDispTab getDispTab() {
        return this.dispTab;
    }

    public List<String> getAttribTab() {
        return this.attribTab;
    }

    public HashMap<String, String> getAttribDefaults() {
        return this.attribDefaults;
    }

    public int findMethodPos(String name) {
        return this.dispTab.indexOf(name);
    }

    public int findAttribPos(String name) {
        return this.attribTab.indexOf(name);
    }

    public String getAttribDefault(String name) {
        return this.attribDefaults.get(name);
    }

    public String toCode() {
        StringBuilder sb = new StringBuilder();

        // Garbage collector tag
        sb.append("# eyecatcher\n");
        sb.append(".word -1\n");

        // label
        sb.append(this.getLabel()).append("_protObj:\n");

        // object tag
        sb.append("# class tag\n");
        sb.append(".word ").append(this.getTag()).append("\n");

        // size of obj in words
        sb.append("# size of object in words\n");
        sb.append(".word ").append(this.computeSize()).append("\n");

        // ptr to dispatch table
        // this should probably be a constant somewhere but meh
        sb.append("# pointer to dispatch table\n");
        sb.append(".word ").append(this.getLabel()).append("_dispTab").append("\n");

        // attributes, this should already be in the correct order coming out of the class
        // i.e. grandparent attrs first
        for (String attr : this.attribTab) {
            String defaultVal = this.attribDefaults.get(attr);
            sb.append("# default value for attrib: " + attr).append("\n");
            sb.append(".word ").append(defaultVal).append("\n");
        }

        return sb.toString();

    }

    public String toString() {
        return "<MipsProtObj " + this.getLabel() + ">";
    }
}
