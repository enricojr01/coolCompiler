package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<CoolIdentifier, CoolBaseNode> symbols = new HashMap<>();
    private HashMap<CoolIdentifier, CoolClass> types = new HashMap<>();
    private SymbolTable parent;

    public SymbolTable() {}

    public void addSymbol(CoolIdentifier id, CoolBaseNode node) {
        symbols.put(id, node);
    }

    public CoolBaseNode getSymbol(CoolIdentifier id) {
        if (this.symbols.containsKey(id)) {
            return this.symbols.get(id);
        }

        if (this.parent != null) {
            SymbolTable next = this.parent;
            while (true) {
                if (next.hasSymbol(id)) {
                    return next.getSymbol(id);
                } else if (next.getParent() == null) {
                    break;
                } else {
                    next = next.getParent();
                }
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

    public void addType(CoolIdentifier ci, CoolClass cc) {
        this.types.put(ci, cc);
    }

    public boolean hasType(CoolIdentifier ci) {
        if (this.types.containsKey(ci)) {
            return true;
        }

        if (this.parent != null) {
            SymbolTable next = this.parent;
            while (true) {
                if (next.hasType(ci)) {
                    return true;
                } else if (next.getParent() == null) {
                    break;
                } else {
                    next = next.getParent();
                }
            }
        }

        return false;
    }

    public CoolClass getType(CoolIdentifier ci) {
        if (this.types.containsKey(ci)) {
            return this.types.get(ci);
        }

        if (this.parent != null) {
            SymbolTable next = this.parent;
            while (true) {
                if (next.hasType(ci)) {
                    return next.getType(ci);
                } else if (next.getParent() == null) {
                    break;
                } else {
                    next = next.getParent();
                }
            }
        }
        return null;
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

    public HashMap<CoolIdentifier, CoolBaseNode> getSymbols() {
        return symbols;
    }

    public void setSymbols(HashMap<CoolIdentifier, CoolBaseNode> symbols) {
        this.symbols = symbols;
    }

    public HashMap<CoolIdentifier, CoolClass> getTypes() {
        return types;
    }

    public void setTypes(HashMap<CoolIdentifier, CoolClass> types) {
        this.types = types;
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
