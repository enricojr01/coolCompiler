package com.enricojr.coollang.ast.program;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.builtins.CoolSelfType;
import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolClass extends CoolBaseNode {
    private CoolIdentifier name;
    private CoolIdentifier parentName;
    private CoolClass parent;
    private ArrayList<CoolAttribute> attributes;
    private ArrayList<CoolMethod> methods;
    private final ArrayList<CoolClass> children = new ArrayList<>();
    private CoolClass computedType;

    public CoolClass() {
        this.setComputedType(this);
    }

    public CoolClass(CoolIdentifier ci) {
        this.name = ci;
        this.setComputedType(this);
    }

    public static CoolClass factory(String identifier) {
        CoolIdentifier ci = new CoolIdentifier(identifier);
        CoolClass cc = new CoolClass();
        cc.setAttributes(new ArrayList<>());
        cc.setMethods(new ArrayList<>());
        cc.setName(ci);
        cc.setComputedType(cc);
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

    public CoolClass getComputedType() {
        return computedType;
    }

    private void setComputedType(CoolClass computedType) {
        this.computedType = computedType;
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

        // NOTE: a <= CoolSelfType is _always false_
        if (b instanceof CoolSelfType) {
            return false;
        }

        // NOTE: (SELF_TYPE(a) <= b) == true if and only if a <= b
        if (a instanceof CoolSelfType) {
            CoolClass typeOf = ((CoolSelfType) a).getTypeOf();
            if (CoolClass.equalOrSubrelation(typeOf, b)) {
                return true;
            } else {
                return false;
            }
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

    // TODO: I really need to find a better way to do this.
    private static LinkedList<CoolClass> whichLonger(LinkedList<CoolClass> a, LinkedList<CoolClass> b) {
        return a.size() > b.size() ? a : b;
    }

    private static LinkedList<CoolClass> whichShorter(LinkedList<CoolClass> a, LinkedList<CoolClass> b) {
        return a.size() < b.size() ? a : b;
    }

    public static CoolClass leastCommonAncestor(CoolClass a, CoolClass b) {
        if (a.equals(b)) {
            return a;
        }
        // NOTE: method overloading not appropriate here because the calling code will never upcast anything to
        // CoolSelfType i.e. it'll always be CoolClass
        if (a instanceof CoolSelfType && b instanceof CoolSelfType) {
            CoolClass concreteA = ((CoolSelfType) a).getTypeOf();
            CoolClass concreteB = ((CoolSelfType) b).getTypeOf();
            return CoolClass.leastCommonAncestor(concreteA, concreteB);
        }
        if (a instanceof CoolSelfType && b != null) {
            CoolClass concreteA = ((CoolSelfType) a).getTypeOf();
            return CoolClass.leastCommonAncestor(concreteA, b);
        }
        if (b instanceof CoolSelfType) {
            CoolClass concreteB = ((CoolSelfType) b).getTypeOf();
            return CoolClass.leastCommonAncestor(a, concreteB);
        }

        LinkedList<CoolClass> stack1 = new LinkedList<>();
        LinkedList<CoolClass> stack2 = new LinkedList<>();

        CoolClass next1 = a;
        CoolClass next2 = b;

        stack1.push(a);
        while (next1 != null) {
            next1 = next1.getParent();
            if (next1 != null) {
                stack1.addLast(next1);
            }
        }

        stack2.push(b);
        while (next2 != null) {
            next2 = next2.getParent();
            if (next2 != null) {
                stack2.addLast(next2);
            }
        }

        LinkedList<CoolClass> longer = null;
        LinkedList<CoolClass> shorter = null;
        if (stack1.size() != stack2.size()) {
            longer = CoolClass.whichLonger(stack1, stack2);
            shorter = CoolClass.whichShorter(stack1, stack2);
            while (longer.size() != shorter.size()) {
                longer.removeFirst();
            }
            // TODO: very tired rn, figure out a way to deduplicate this block later.
            while (!stack1.isEmpty() && !stack2.isEmpty()) {
                CoolClass c1 = stack1.removeFirst();
                CoolClass c2 = stack2.removeFirst();
                if (c1.equals(c2)) {
                    return c1;
                }
            }
        } else {
            while (!stack1.isEmpty() && !stack2.isEmpty()) {
                CoolClass c1 = stack1.removeFirst();
                CoolClass c2 = stack2.removeFirst();
                if (c1.equals(c2)) {
                    return c1;
                }
            }

        }

        // NOTE: they should be equal in length at this point if they're not already

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
