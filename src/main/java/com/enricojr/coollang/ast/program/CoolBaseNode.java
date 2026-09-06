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

    // NOTE: This is the only way I could think of to get symbols out of
    //       a let or case statement that may be deeply nested.
    public String symbolTableReport() {
        StringBuilder sb = new StringBuilder();
        if (this.symbols != null) {
            sb.append(String.format("Symbol Table for Expression: %s", this.getClass().getName()));
            sb.append(this.symbols.toString());
        } else {
            sb.append(String.format("Expression %s has no symbol table\n", this.getClass().getName()));
        }
        return sb.toString();
    }
}
