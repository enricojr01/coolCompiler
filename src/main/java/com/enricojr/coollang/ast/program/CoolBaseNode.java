package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.semantic.SymbolTable;

import java.util.ArrayList;

public class CoolBaseNode {
    private CoolBaseNode parent;
    private ArrayList<CoolBaseNode> children;
    private SymbolTable symbols;

    public CoolBaseNode() {}

    public CoolBaseNode getParent() {
        return parent;
    }

    public void setParent(CoolBaseNode parent) {
        this.parent = parent;
    }

    public ArrayList<CoolBaseNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<CoolBaseNode> children) {
        this.children = children;
    };

    public void addChild(CoolBaseNode child) {
        this.children.add(child);
    }

    public SymbolTable getSymbols() {
        return symbols;
    }

    public void setSymbols(SymbolTable symbols) {
        this.symbols = symbols;
    }
}
