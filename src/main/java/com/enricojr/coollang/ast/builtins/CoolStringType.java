package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.CoolExpr;
import com.enricojr.coollang.ast.expressions.CoolInstantiate;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolFormal;
import com.enricojr.coollang.ast.program.CoolMethod;
import com.enricojr.coollang.ast.program.CoolParamList;

import java.util.ArrayList;
import java.util.List;

public class CoolStringType extends CoolBuiltInType {
    public CoolStringType() {
        this.setName(new CoolIdentifier("String"));
        this.setParentName(new CoolIdentifier("Object"));

        ArrayList<CoolFormal> lengthFormals = new ArrayList<>();
        CoolParamList lengthParams = CoolParamList.factory(lengthFormals);
        CoolMethod length = CoolMethod.factory(
                new CoolIdentifier("length"),
                lengthParams,
                new CoolIdentifier("Int")
        );
        ArrayList<CoolExpr> lengthBody = new ArrayList<>(List.of(new CoolInstantiate("Int")));
        length.setBody(lengthBody);

        ArrayList<CoolFormal> concatFormals = new ArrayList<>(List.of(new CoolFormal("s", "String")));
        CoolParamList concatParams = CoolParamList.factory(concatFormals);
        CoolMethod concat = CoolMethod.factory(
                new CoolIdentifier("concat"),
                concatParams,
                new CoolIdentifier("String")
        );
        ArrayList<CoolExpr> concatBody = new ArrayList<>(List.of(new CoolInstantiate("String")));
        concat.setBody(concatBody);

        ArrayList<CoolFormal> substrFormals = new ArrayList<>(
                List.of(
                        new CoolFormal("i", "Int"),
                        new CoolFormal("l", "Int")
                )
        );
        CoolParamList substrParams = CoolParamList.factory(substrFormals);
        CoolMethod substr = CoolMethod.factory(
                new CoolIdentifier("substr"),
                substrParams,
                new CoolIdentifier("String")
        );
        ArrayList<CoolExpr> substrBody = new ArrayList<>(List.of(new CoolInstantiate("String")));
        substr.setBody(substrBody);

        ArrayList<CoolMethod> methods = new ArrayList<>(List.of(length, concat, substr));
        this.setMethods(methods);
    }
}
