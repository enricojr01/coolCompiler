package com.enricojr.coollang.ast.program;

import java.util.ArrayList;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.semantic.SymbolTable;

public class CoolClass extends CoolBaseNode {
    private CoolIdentifier name;
    private CoolIdentifier parentName;
    private CoolClass parent;
    private ArrayList<CoolAttribute> attributes;
    private ArrayList<CoolMethod> methods;
    private SymbolTable symbols;

    public CoolClass() {}

    public CoolIdentifier getName() {
        return name;
    }

    public void setName(CoolIdentifier name) {
        this.name = name;
    }

    public ArrayList<CoolAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<CoolAttribute> attributes) {
        this.attributes = attributes;
    }

    public ArrayList<CoolMethod> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<CoolMethod> methods) {
        this.methods = methods;
    }

    public CoolClass getParent() {
        return parent;
    }

    public void setParent(CoolClass parent) {
        this.parent = parent;
    }

    public CoolIdentifier getParentName() {
        return parentName;
    }

    public void setParentName(CoolIdentifier parentName) {
        this.parentName = parentName;
    }

    public SymbolTable getSymbols() {
        return symbols;
    }

    public void setSymbols(SymbolTable symbols) {
        this.symbols = symbols;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("(CoolClass - %s", this.name.getValue()));

        if (this.parentName != null) {
            sb.append(String.format(" extends %s)", this.parentName.getValue()));
        } else {
            sb.append(")");
        }

        return sb.toString();
    }
}
