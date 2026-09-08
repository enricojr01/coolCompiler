package com.enricojr.coollang.ast.program;

import java.util.ArrayList;
import java.util.Objects;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolClass extends CoolBaseNode {
    private CoolIdentifier name;
    private CoolIdentifier parentName;
    private CoolClass parent;
    private ArrayList<CoolAttribute> attributes;
    private ArrayList<CoolMethod> methods;
    private final ArrayList<CoolClass> children = new ArrayList<>();

    public CoolClass() {}

    public CoolClass(CoolIdentifier ci) {
        this.name = ci;
    }

    public static CoolClass factory(String identifier) {
        CoolIdentifier ci = new CoolIdentifier(identifier);
        CoolClass cc = new CoolClass();
        cc.setName(ci);
        return cc;
    }

    public CoolMethod classMethodSearch(CoolIdentifier id) {
        for (CoolMethod cm : this.methods) {
            if (cm.getName().equals(id)) {
                return cm;
            }
        }

        return null;
    }

    public CoolIdentifier getName() {
        return name;
    }

    public String getNameString() {
        return this.name.getValue().toString();
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

    public String getParentNameString() {
        return this.parentName.getValue().toString();
    }

    public void setParentName(CoolIdentifier parentName) {
        this.parentName = parentName;
    }

    public void addChild(CoolClass cc) {
        this.children.add(cc);
    }

    public ArrayList<CoolClass> getChildren() {
        return this.children;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CoolClass coolClass = (CoolClass) o;
        return Objects.equals(getName(), coolClass.getName())
                && Objects.equals(getParentName(), coolClass.getParentName())
                && Objects.equals(getAttributes(), coolClass.getAttributes())
                && Objects.equals(getMethods(), coolClass.getMethods());
    }

    public boolean equalOrSubrelation(CoolClass b) {
        if (this.equals(b)) {
            return true;
        }

        if (this.parent != null) {
            CoolClass next = this.parent;
            while (true) {
                if (b.equals(next)) {
                    return true;
                } else if (next.getParent() != null) {
                    next = next.getParent();
                } else {
                    break;
                }
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getParentName(), getAttributes(), getMethods());
    }

    public void accept(AstVisitor t) {
        t.visitCoolClass(this);
    }
}
