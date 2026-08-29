package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolBaseNode;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<CoolIdentifier, CoolBaseNode> symbols = new HashMap<>();
    private SymbolTable parent;

    public SymbolTable() {}

    public void addSymbol(CoolIdentifier id, CoolBaseNode node) {
        symbols.put(id, node);
    }

    public CoolBaseNode getSymbol(CoolIdentifier id) {
        if (this.symbols.containsKey(id)) {
            return this.symbols.get(id);
        }

        SymbolTable next = this.parent;
        while (next.parent != null) {
            if (next.hasSymbol(id)) {
                return next.getSymbol(id);
            } else {
                next = next.getParent();
            }
        }

        return null;
    }

    public boolean hasSymbol(CoolIdentifier id) {
        if (this.symbols.containsKey(id)) {
            return true;
        }

        SymbolTable next = this.parent;
        while (next.parent != null) {
            if (next.hasSymbol(id)) {
                return true;
            } else {
                next = next.getParent();
            }
        }

        return false;
    }

    public boolean isEmpty() {
        return this.symbols.isEmpty();
    }

    public HashMap<CoolIdentifier, CoolBaseNode> getHashMap() {
        return this.symbols;
    }

    public void addAll(SymbolTable st) {
        this.symbols.putAll(st.getHashMap());
    }

    public SymbolTable getParent() {
        return this.parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.symbols.keySet() != null) {
            for (CoolIdentifier ci : this.symbols.keySet()) {
                sb.append(String.format("%s: %s\n", ci, this.symbols.get(ci)));
            }
        }
        return sb.toString();
    }
}
