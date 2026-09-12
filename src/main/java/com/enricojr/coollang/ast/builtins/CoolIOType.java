package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.constants.CoolString;
import com.enricojr.coollang.ast.expressions.CoolInstantiate;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolFormal;
import com.enricojr.coollang.ast.program.CoolMethod;
import com.enricojr.coollang.ast.program.CoolParamList;
import com.enricojr.coollang.semantic.symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class CoolIOType extends CoolBuiltInType {
    public CoolIOType() {
        this.setName(new CoolIdentifier("IO"));
        this.setParentName(new CoolIdentifier("Object"));
        this.setSymbols(new SymbolTable());

        // TODO: start adding the built-ins for the IO class,
        // not sure if the methods need to be concrete
//        this.getSymbols().addSymbolType(new CoolIdentifier("out_string"), new CoolClass());

        ArrayList<CoolMethod> methods = new ArrayList<>();

        CoolParamList params = new CoolParamList();
        CoolFormal formal1 = new CoolFormal();
        formal1.setName(new CoolIdentifier("message"));
        formal1.setType(new CoolIdentifier("String"));
        params.setParameters(new ArrayList<>(List.of(formal1)));

        CoolMethod outString = new CoolMethod();
        outString.setName(new CoolIdentifier("out_string"));
        outString.setParameters(params);
        outString.setBody(
                new ArrayList<>(
                        List.of(new CoolInstantiate(new CoolIdentifier("SELF_TYPE")))
                )
        );
        outString.setReturnType(new CoolIdentifier("SELF_TYPE"));

        CoolParamList params2 = new CoolParamList();
        params2.setParameters(new ArrayList<>());
        CoolMethod inString = new CoolMethod();
        inString.setName(new CoolIdentifier("in_string"));
        inString.setBody(new ArrayList<>(List.of(new CoolString("temp"))));
        // holy god this is ugly need to fix
        inString.setParameters(params2);
        inString.setReturnType(new CoolIdentifier("String"));

        this.setMethods(new ArrayList<>(List.of(outString, inString)));
    }

    public CoolIOType(CoolClass parent) {
        super(parent);

        this.setName(new CoolIdentifier("IO"));
        this.setParentName(new CoolIdentifier("Object"));
        this.setSymbols(new SymbolTable());
    }
}
