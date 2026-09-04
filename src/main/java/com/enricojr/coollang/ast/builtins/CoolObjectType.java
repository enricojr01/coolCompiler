package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.semantic.SymbolTable;

public class CoolObjectType extends CoolClass {
    public CoolObjectType() {
        CoolIdentifier name = new CoolIdentifier("Object");
        this.setName(name);
        this.setSymbols(new SymbolTable());
    }
}
