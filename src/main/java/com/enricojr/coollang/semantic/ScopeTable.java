package com.enricojr.coollang.semantic;

public class ScopeTable {
    private HashMap<CoolIdentifier, CoolBaseNode> symbols = new HashMap<>();
    private ScopeTable next;
    private ScopeTable previous;

    public ScopeTable() {}

    public void addSymbol(CoolIdentifier id, CoolBaseNode node) {
        symbols.put(id, node);
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
