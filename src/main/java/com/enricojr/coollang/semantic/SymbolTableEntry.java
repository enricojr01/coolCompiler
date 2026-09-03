package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolLiteral;
import com.enricojr.coollang.ast.program.CoolClass;

public class SymbolTableEntry {
    private CoolClass type;
    private CoolLiteral value;

    public SymbolTableEntry() {}

    public SymbolTableEntry(CoolClass type, CoolLiteral value) {
        this.type = type;
        this.value = value;
    }

    public CoolClass getType() {
        return type;
    }

    public String getTypeString() {
        return this.type.getName().getValue().toString();
    }

    public void setType(CoolClass type) {
        this.type = type;
    }

    public CoolLiteral getValue() {
        return value;
    }

    public void setValue(CoolLiteral value) {
        this.value = value;
    }
}
