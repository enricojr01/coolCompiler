package com.enricojr.coollang.codegen;

import com.enricojr.coollang.codegen.models.MipsAlignedTable;
import com.enricojr.coollang.codegen.models.MipsIntConst;
import com.enricojr.coollang.codegen.models.MipsStrConst;

import java.util.HashMap;
import java.util.Map;

public class MipsClassNameTableGenerator {
    private HashMap<String, MipsStrConst> table;
    private MipsAlignedTable list;

    public MipsClassNameTableGenerator(HashMap<String, MipsStrConst> table) {
        this.table = table;
        this.list = new MipsAlignedTable("class_nameTab", this.table.size());
        this.buildList();
    }

    private void buildList() {
        for (Map.Entry<String, MipsStrConst> e : table.entrySet()) {
            int tag = e.getValue().getTag();
            // isn't there a way to simplify this sort of thing?
            int spot = (tag * 4) / 4;
            this.list.insert(e.getValue(), spot);
        }
    }

    public String getList() {
        return this.list.toString();
    }

    public String getConsts() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, MipsStrConst> e : table.entrySet()) {
            MipsStrConst classObj = e.getValue();
            MipsIntConst sizeObj = classObj.getSize();
            sb.append(sizeObj.toCode());
            sb.append(classObj.toCode());
        }

        return sb.toString();
    }
}
