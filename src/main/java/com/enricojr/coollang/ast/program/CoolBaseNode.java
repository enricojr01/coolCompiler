package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.semantic.SymbolTable;
import com.enricojr.coollang.semantic.TypeEnvironment;

import java.util.ArrayList;

public class CoolBaseNode {
    private CoolBaseNode parent;
    private SymbolTable symbols;
    private TypeEnvironment types;

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

    public TypeEnvironment getTypes() {
        return types;
    }

    public void setTypes(TypeEnvironment types) {
        this.types = types;
    }
}
