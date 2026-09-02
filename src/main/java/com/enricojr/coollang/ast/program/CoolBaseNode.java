package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.semantic.SymbolTable;

public class CoolBaseNode {
    private CoolBaseNode parent;
    private SymbolTable symbols;

    public CoolBaseNode() {}

    public CoolBaseNode getParent() {
        return parent;
    }

    public void setParent(CoolBaseNode parent) {
        this.parent = parent;
    }

    public SymbolTable getSymbols() {
        return symbols;
    }

    public void setSymbols(SymbolTable symbols) {
        this.symbols = symbols;
    }
}
