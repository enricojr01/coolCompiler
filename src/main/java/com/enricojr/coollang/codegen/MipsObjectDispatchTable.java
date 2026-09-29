package com.enricojr.coollang.codegen;

public class MipsObjectDispatchTable {
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(".word Object.abort\n");
        sb.append(".word Object.type_name\n");
        sb.append("");
        return sb.toString();
    }
}
