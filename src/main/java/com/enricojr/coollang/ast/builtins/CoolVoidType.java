package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.semantic.symboltable.SymbolTable;

public class CoolVoidType extends CoolBuiltInType {
    public CoolVoidType() {
        this.setName(new CoolIdentifier("void"));
        this.setParentName(new CoolIdentifier("Object"));
        this.setSymbols(new SymbolTable());
    }
}
