package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.builtins.*;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<CoolIdentifier, CoolClass> types = new HashMap<>();
    private HashMap<CoolIdentifier, MethodTableEntry> methods = new HashMap<>();
    private SymbolTable parent;

    public SymbolTable() {}

    public void addMethod(CoolIdentifier ci, MethodTableEntry mte) {
        this.methods.put(ci, mte);
    }

    public boolean hasMethod(CoolIdentifier ci) {
        if (this.methods.containsKey(ci)) {
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

    public MethodTableEntry getMethod(CoolIdentifier ci) {
        if (this.methods.containsKey(ci)) {
            return this.methods.get(ci);
        }

        if (this.parent != null) {
            SymbolTable next = this.parent;
            while (true) {
                if (next.hasType(ci)) {
                    return next.getMethod(ci);
                } else if (next.getParent() == null) {
                    break;
                } else {
                    next = next.getParent();
                }
            }
        }

        return null;
    }

    public HashMap<CoolIdentifier, MethodTableEntry> getMethods() {
        return methods;
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

    public SymbolTable getParent() {
        return this.parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public HashMap<CoolIdentifier, CoolClass> getTypes() {
        return types;
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
