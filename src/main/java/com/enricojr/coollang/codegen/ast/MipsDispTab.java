package com.enricojr.coollang.codegen.ast;

import com.enricojr.coollang.codegen.ast.consts.MipsBaseConst;

import java.util.LinkedList;
import java.util.List;

public class MipsDispTab extends MipsBaseConst {
    private List<String> methodMap;

    public MipsDispTab(String label, List<String> methodMap) {
        super(label, -1);
        this.methodMap = methodMap;
    }

    public List<String> getMethodMap() {
        return this.methodMap;
    }

    public int indexOf(String key) {
        return this.methodMap.indexOf(key);
    }

    public int size() {
        return this.methodMap.size();
    }

    public String toCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getLabel()).append("_dispTab:").append("\n");

        for (String method : this.methodMap) {
            sb.append(".word ").append(method).append("\n");
        }

        return sb.toString();
    }

    public String toString() {
        return "<MipsDispTab: " + this.getLabel() + ">";
    }
}
