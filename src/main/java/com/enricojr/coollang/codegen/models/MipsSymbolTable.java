package com.enricojr.coollang.codegen.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MipsSymbolTable {
    private HashMap<String, MipsBaseConst> table = new HashMap<>();

    public Set<Map.Entry<String, MipsBaseConst>> entrySet() {
        return this.table.entrySet();
    }

    public MipsBaseConst getSymbol(String symbol) {
        return this.table.get(symbol);
    }

    public void putSymbol(String symbol, MipsBaseConst object) {
        this.table.put(symbol, object);
    }

    public String toString() {
        return this.table.toString();
    }
}
