package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.semantic.SymbolTable;

public class CoolBaseNode {
    private SymbolTable symbols;

    public CoolBaseNode() {}

    public SymbolTable getSymbols() {
        return symbols;
    }

    public void setSymbols(SymbolTable symbols) {
        this.symbols = symbols;
    }

}
