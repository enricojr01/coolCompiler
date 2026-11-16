package com.enricojr.coollang.codegen.ast;

import com.enricojr.coollang.codegen.ast.consts.MipsBaseConst;

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

    public MipsProtObj findProtObj(String name) {
        String fullName = String.format("%s_protObj", name);
        MipsBaseConst result = this.table.get(fullName);
        if (result instanceof MipsProtObj) {
            return ((MipsProtObj) result);
        } else {
            return null;
        }
    }

    public void putSymbol(String symbol, MipsBaseConst object) {
        this.table.put(symbol, object);
    }

    public String toString() {
        return this.table.toString();
    }
}
