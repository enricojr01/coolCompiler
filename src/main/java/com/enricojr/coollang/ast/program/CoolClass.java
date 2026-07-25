package com.enricojr.coollang.ast.program;

import java.util.ArrayList;
import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolClass extends CoolBaseNode {
    private CoolIdentifier name;
    private CoolIdentifier parentName;
    private CoolClass parent;
    private ArrayList<CoolAttribute> attributes;
    private ArrayList<CoolMethod> methods;

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

    public String toString() {
        return String.format("CoolClass - %s", this.name);
    }
}
