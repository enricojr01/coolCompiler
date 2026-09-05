package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.semantic.SymbolTable;

public class CoolIOType extends CoolBuiltInType {
    public CoolIOType() {
        this.setName(new CoolIdentifier("IO"));
        this.setParentName(new CoolIdentifier("Object"));
        this.setSymbols(new SymbolTable());
    }

    public CoolIOType(CoolClass parent) {
        super(parent);

        // TODO: is there a way to invoke the upper constructor after calling super?
        this.setName(new CoolIdentifier("IO"));
        this.setParentName(new CoolIdentifier("Object"));
        this.setSymbols(new SymbolTable());
    }
}
