package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolBaseNode;

import java.util.HashMap;

public class ScopeTable {
    private HashMap<CoolIdentifier, CoolBaseNode> symbols = new HashMap<>();
    private ScopeTable next;
    private ScopeTable previous;

    public ScopeTable() {}

    public void addSymbol(CoolIdentifier id, CoolBaseNode node) {
        symbols.put(id, node);
    }

    public CoolBaseNode getSymbol(CoolIdentifier id) {
        return symbols.get(id);
    }

    public ScopeTable getNext() {
        return next;
    }

    public void setNext(ScopeTable next) {
        this.next = next;
    }

    public ScopeTable getPrevious() {
        return previous;
    }

    public void setPrevious(ScopeTable previous) {
        this.previous = previous;
    }
}
