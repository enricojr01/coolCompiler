package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.constants.CoolLiteral;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolMethod;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<CoolIdentifier, SymbolTableEntry> types = new HashMap<>();
    private HashMap<CoolIdentifier, MethodTableEntry> methods = new HashMap<>();
    private SymbolTable parent;

    public SymbolTable() {}

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    public void addMethod(CoolIdentifier ci, SymbolTable parameters, CoolClass returnType, CoolMethod methodObj) {
        MethodTableEntry mte = new MethodTableEntry();
        mte.setName(ci);
        mte.setParameters(parameters);
        mte.setReturnType(returnType);
        mte.setMethodObj(methodObj);

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

    public CoolClass getMethodType(CoolIdentifier ci) {
        if (this.methods.containsKey(ci)) {
            MethodTableEntry mte = this.methods.get(ci);
            return mte.getReturnType();
        }

        if (this.parent != null) {
            SymbolTable next = this.parent;
            while (true) {
                if (next.hasType(ci)) {
                    return next.getMethodType(ci);
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

    public void addSymbolType(CoolIdentifier ci, CoolClass cc) {
        SymbolTableEntry ste = new SymbolTableEntry();
        ste.setType(cc);
        this.types.put(ci, ste);
    }

    public void addSymbolType(CoolIdentifier ci, CoolClass cc, CoolLiteral cl) {
        SymbolTableEntry ste = new SymbolTableEntry();
        ste.setType(cc);
        ste.setValue(cl);

        this.types.put(ci, ste);
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

    public CoolClass getSymbolType(CoolIdentifier ci) {
        if (this.types.containsKey(ci)) {
            SymbolTableEntry ste = this.types.get(ci);
            return ste.getType();
        }

        if (this.parent != null) {
            SymbolTable next = this.parent;
            while (true) {
                if (next.hasType(ci)) {
                    return next.getSymbolType(ci);
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

    public HashMap<CoolIdentifier, SymbolTableEntry> getTypes() {
        return types;
    }

    public void printSymbolTableChain() {
        System.out.println(this);
        if (this.parent != null && this.getParent() != null) {
            SymbolTable next = this.parent;
            while (true) {
                System.out.println(next.toString());
                if (next.getParent() == null) {
                    break;
                } else {
                    next = next.getParent();
                }
            }
        }
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
