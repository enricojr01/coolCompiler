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

        if (this.parent != null) {
            CoolClass next = this.parent;
            while (true) {
                CoolMethod cm = next.classMethodSearch(id);
                if (cm != null) {
                    return cm;
                } else if (next.getParent() != null){
                    next = next.getParent();
                } else {
                    break;
                }
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

    // TODO: will this even work? Now that I look at it again I'm not sure it will
    // TODO: no, I'm sure it will work
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

    // TODO: need to actually start using this I think its a lot clearer than the old one
    public static boolean equalOrSubrelation(CoolClass a, CoolClass b) {
        // NOTE: returns whether a <= b for conformance purposes
        // i.e. a <= b if they are the same type, or share a common ancestor.
        if (a.equals(b)) {
            return true;
        }

        if (a.getParent() != null) {
            CoolClass next = a.getParent();
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

    public static CoolClass leastCommonAncestor(CoolClass a, CoolClass b) {
        CoolClass next1 = a.getParent();
        CoolClass next2 = b.getParent();

        if (next1 != null && next2 != null) {
            while(true) {
                if (next1.equals(next2)) {
                    // really it doesn't matter which one at this point
                    return next1;
                } else if (next1.getParent() == null || next2.getParent() == null){
                    break;
                } else {
                    next1 = next1.getParent();
                    next2 = next2.getParent();
                }
            }
        }

        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getParentName(), getAttributes(), getMethods());
    }

    public void accept(AstVisitor t) {
        t.visitCoolClass(this);
    }
}
