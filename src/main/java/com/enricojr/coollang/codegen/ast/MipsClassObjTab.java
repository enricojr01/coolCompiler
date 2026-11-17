package com.enricojr.coollang.codegen.ast;

import com.enricojr.coollang.codegen.ast.consts.MipsBaseConst;

import java.util.List;

public class MipsClassObjTab extends MipsBaseConst {
    private List<String> internalTab;

    public MipsClassObjTab(List<String> table) {
        super("class_objTab", -1);
        this.internalTab = table;
    }

    public int indexOf(String name) {
        return this.internalTab.indexOf(name);
    }

    public String toString() {
        return this.internalTab.toString();
    }
}
