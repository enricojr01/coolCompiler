package com.enricojr.coollang.codegen;

public class MipsStrConst extends MipsConst {
    private MipsIntConst strSize;
    private String contents;

    public MipsIntConst getStrSize() {
        return strSize;
    }

    public void setStrSize(MipsIntConst strSize) {
        this.strSize = strSize;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(".word ").append(this.strSize.toString()).append("\n");
        sb.append(".ascii ").append(this.contents).append("\n");

        // NOTE: not sure what "byte 0" is supposed to achieve here.
        // NOTE: also not entirely sure what "align 2" does, I think its meant to ensure that all strings occupy the same
        // space in memory? that MIPS will take care of padding them out so they're all the same size?
        // NOTE: "word -1" is required by the Cool Runtime, it's for the garbage collector.
        sb.append(".byte 0\n");
        sb.append(".align 2\n");
        sb.append(".word -1\n");

        return super.toString() + sb;
    }
}
