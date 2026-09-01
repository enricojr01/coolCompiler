package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;

public class SymbolTableEntry {
    private CoolClass type;
    private CoolBaseNode node;

    public SymbolTableEntry() {}

    public SymbolTableEntry(CoolClass cc, CoolBaseNode cbn) {
        this.type = cc;
        this.node = cbn;
    }

    public CoolClass getType() {
        return type;
    }

    public void setType(CoolClass type) {
        this.type = type;
    }

    public CoolBaseNode getNode() {
        return node;
    }

    public void setNode(CoolBaseNode node) {
        this.node = node;
    }

    public String toString() {
        return String.format("SymbolTableEntry %s", this.type);
    }
}
