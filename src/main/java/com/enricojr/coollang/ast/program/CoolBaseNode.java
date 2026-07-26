package com.enricojr.coollang.ast.program;

import java.util.ArrayList;

public class CoolBaseNode {
    private CoolBaseNode parent;
    private ArrayList<CoolBaseNode> children;

    public CoolBaseNode() {}

    public CoolBaseNode getParent() {
        return parent;
    }

    public void setParent(CoolBaseNode parent) {
        this.parent = parent;
    }

    public ArrayList<CoolBaseNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<CoolBaseNode> children) {
        this.children = children;
    };

    public void addChild(CoolBaseNode child) {
        this.children.add(child);
    }
}
