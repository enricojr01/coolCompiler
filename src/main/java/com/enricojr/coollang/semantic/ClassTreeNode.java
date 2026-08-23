package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;

import java.util.ArrayList;

public class ClassTreeNode {
    private CoolIdentifier identifier;
    private CoolClass coolClass;
    private ClassTreeNode parent;
    private ArrayList<ClassTreeNode> children;

    public ClassTreeNode() {
        this.children = new ArrayList<>();
    }

    public ClassTreeNode(CoolClass cc) {
        this();
        this.identifier = cc.getName();
        this.coolClass = cc;
    }

    public void addChild(ClassTreeNode child) {
        this.children.add(child);
    }

    public CoolIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(CoolIdentifier identifier) {
        this.identifier = identifier;
    }

    public CoolClass getCoolClass() {
        return coolClass;
    }

    public void setCoolClass(CoolClass coolClass) {
        this.coolClass = coolClass;
    }

    public ArrayList<ClassTreeNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<ClassTreeNode> children) {
        this.children = children;
    }

    public ClassTreeNode getParent() {
        return parent;
    }

    public void setParent(ClassTreeNode parent) {
        this.parent = parent;
    }

    public String print(StringBuilder sb, int level) {
        String indent = " ";
        sb.append(indent.repeat(level));
        sb.append("%s\n".formatted(this.coolClass));

        for (ClassTreeNode child : children) {
            child.print(sb, level + 2);
        }

        return sb.toString();
    }

    public String toString() {
        return this.coolClass.toString();
    }
}
