package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<CoolIdentifier, CoolClass> types = new HashMap<>();
    private SymbolTable parent;

    public SymbolTable() {}

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

    public SymbolTable getParent() {
        return this.parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public HashMap<CoolIdentifier, CoolClass> getTypes() {
        return types;
    }

    public void setTypes(HashMap<CoolIdentifier, CoolClass> types) {
        this.types = types;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.types.keySet() != null) {
            // TODO: replace this with entrySet()
            for (CoolIdentifier ci : this.types.keySet()) {
                sb.append(String.format("%s: %s\n", ci, this.types.get(ci)));
            }
        }
        return sb.toString();
    }
}
