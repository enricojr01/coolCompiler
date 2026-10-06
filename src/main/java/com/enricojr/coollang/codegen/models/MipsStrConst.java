package com.enricojr.coollang.codegen.models;

import java.util.Objects;

public class MipsStrConst extends MipsBaseConst {
    private final MipsIntConst size;
    private final String contents;

    public MipsStrConst(String label, int tag, MipsIntConst size, String contents) {
        super(label, tag);
        this.size = size;
        // TODO: oh boy this is kinda weird
        this.contents = Objects.requireNonNullElse(contents, "");
    }

    private int getObjWordSize() {
        // for the first 4 fields of the object;
        int byteSize = 4 * 4;
        int contentSize = this.contents.getBytes().length;
        int terminator = 1;
        int totalByteSize = byteSize + contentSize + terminator;
        int totalWordSize = 0;

        if (totalByteSize % 4 != 0) {
            totalWordSize = totalByteSize / 4 + 1;
        } else {
            totalWordSize = totalByteSize / 4;
        }

        return totalWordSize;
    }

    public MipsIntConst getSize() {
        return this.size;
    }

    public String toCode() {
        StringBuilder sb = new StringBuilder();
        // GC tag
        sb.append(".word -1\n");

        // label
        sb.append(String.format("%s:\n", this.getLabel()));

        // class tag - there are rules on how to select this
        sb.append(String.format(".word %s\n", this.getTag()));

        // size of String object in words
        sb.append(String.format(".word %s\n", this.getObjWordSize()));

        // pointer to dispatch table, fixed and unchanging
        sb.append(".word String_dispTab\n");

        // pointer to int const representing length of string
        sb.append(String.format(".word %s\n", this.size.getLabel()));

        // ascii representing the contents of the string.
        sb.append(String.format(".ascii \"%s\"\n", this.contents));

        sb.append(".byte 0\n");

        return sb.toString();
    }

    @Override
    public String toString() {
        return "<MipsStrConst: " + this.getLabel() + ">";
    }
}
