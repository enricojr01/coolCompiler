package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.semantic.SymbolTable;

public class CoolBaseNode {
    private SymbolTable symbols;
    private int line;
    private int charPos;

    public CoolBaseNode() {}

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getCharPos() {
        return charPos;
    }

    public void setCharPos(int charPos) {
        this.charPos = charPos;
    }

    public SymbolTable getSymbols() {
        return symbols;
    }

    public void setSymbols(SymbolTable symbols) {
        this.symbols = symbols;
    }

}
