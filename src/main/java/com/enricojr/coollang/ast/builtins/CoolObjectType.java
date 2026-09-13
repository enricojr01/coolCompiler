package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.CoolExpr;
import com.enricojr.coollang.ast.expressions.CoolInstantiate;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolFormal;
import com.enricojr.coollang.ast.program.CoolMethod;
import com.enricojr.coollang.ast.program.CoolParamList;
import com.enricojr.coollang.semantic.symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class CoolObjectType extends CoolBuiltInType {
    public CoolObjectType() {
        CoolIdentifier name = new CoolIdentifier("Object");
        this.setName(name);
        this.setSymbols(new SymbolTable());

        ArrayList<CoolFormal> abortFormals = new ArrayList<>();
        CoolParamList abortParams = CoolParamList.factory(abortFormals);
        CoolMethod abort = CoolMethod.factory(
                new CoolIdentifier("abort"),
                abortParams,
                new CoolIdentifier("Object")
        );
        ArrayList<CoolExpr> coolMethodBody = new ArrayList<>(List.of(new CoolInstantiate("Object")));
        abort.setBody(coolMethodBody);

        ArrayList<CoolFormal> typeNameFormals = new ArrayList<>();
        CoolParamList typeNameParams = CoolParamList.factory(typeNameFormals);
        CoolMethod typeName = CoolMethod.factory(
                new CoolIdentifier("type_name"),
                typeNameParams,
                new CoolIdentifier("String")
        );
        ArrayList<CoolExpr> typeNameBody = new ArrayList<>(List.of(new CoolInstantiate("String")));
        typeName.setBody(typeNameBody);

        ArrayList<CoolFormal> copyFormals = new ArrayList<>();
        CoolParamList copyParams = CoolParamList.factory(copyFormals);
        CoolMethod copy = CoolMethod.factory(
                new CoolIdentifier("copy"),
                copyParams,
                new CoolIdentifier("SELF_TYPE")
        );
        ArrayList<CoolExpr> copyBody = new ArrayList<>(List.of(new CoolInstantiate("SELF_TYPE")));
        copy.setBody(copyBody);

        ArrayList<CoolMethod> methods = new ArrayList<>(List.of(abort, typeName, copy));

        this.setMethods(methods);
    }
}
