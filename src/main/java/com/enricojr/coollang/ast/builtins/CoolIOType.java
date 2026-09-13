package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.constants.CoolString;
import com.enricojr.coollang.ast.expressions.CoolExpr;
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
        ArrayList<CoolMethod> methods = new ArrayList<>();

        ArrayList<CoolFormal> outStringFormals = new ArrayList<>(
                List.of(new CoolFormal("message", "String"))
        );
        CoolParamList outStringParams = CoolParamList.factory(outStringFormals);
        CoolMethod outString = CoolMethod.factory(
                new CoolIdentifier("out_string"),
                outStringParams,
                new CoolIdentifier("SELF_TYPE")
        );
        // NOTE: All these functions will need to have dummy bodies to pass the type checker;
        ArrayList<CoolExpr> outStringBody = new ArrayList<>(
                List.of(new CoolInstantiate("SELF_TYPE"))
        );
        outString.setBody(outStringBody);

        ArrayList<CoolFormal> outIntFormals = new ArrayList<>(List.of(new CoolFormal("num", "Int")));
        CoolParamList outIntParams = CoolParamList.factory(outIntFormals);
        CoolMethod outInt = CoolMethod.factory(
                new CoolIdentifier("out_int"),
                outIntParams,
                new CoolIdentifier("SELF_TYPE")
        );
        ArrayList<CoolExpr> outIntBody = new ArrayList<>(
                List.of(new CoolInstantiate("SELF_TYPE"))
        );
        outInt.setBody(outIntBody);

        // NOTE: in_string doesn't take any arguments but I need to put an empty param list in anyways
        ArrayList<CoolFormal> inStringFormals = new ArrayList<>();
        CoolParamList inStringParams = CoolParamList.factory(inStringFormals);
        CoolMethod inString = CoolMethod.factory(
                new CoolIdentifier("in_string"),
                inStringParams,
                new CoolIdentifier("String")
        );
        ArrayList<CoolExpr> inStringBody = new ArrayList<>(
                List.of(new CoolInstantiate("String"))
        );
        inString.setBody(inStringBody);

        ArrayList<CoolFormal> inIntFormals = new ArrayList<>();
        CoolParamList inIntParams = CoolParamList.factory(inIntFormals);
        CoolMethod inInt = CoolMethod.factory(
                new CoolIdentifier("in_int"),
                inIntParams,
                new CoolIdentifier("Int")
        );
        ArrayList<CoolExpr> inIntBody = new ArrayList<>(
                List.of(new CoolInstantiate("Int"))
        );
        inInt.setBody(inIntBody);

        this.setMethods(new ArrayList<>(List.of(outString, outInt, inString, inInt)));
    }
}
