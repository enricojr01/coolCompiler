package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolBaseNode;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<CoolIdentifier, CoolBaseNode> symbols = new HashMap<>();

    public SymbolTable() {}

    public void addSymbol(CoolIdentifier id, CoolBaseNode node) {
        symbols.put(id, node);
    }

    public CoolBaseNode getSymbol(CoolIdentifier id) {
        return symbols.get(id);
    }

    public boolean hasSymbol(CoolIdentifier id) {
        return symbols.containsKey(id);
    }

    public HashMap<CoolIdentifier, CoolBaseNode> getHashMap() {
        return this.symbols;
    }

    public void addAll(SymbolTable st) {
        this.symbols.putAll(st.getHashMap());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (CoolIdentifier ci : this.symbols.keySet()) {
            sb.append(String.format("%s: %s\n", ci, this.symbols.get(ci)));
        }
        return sb.toString();
    }
}
