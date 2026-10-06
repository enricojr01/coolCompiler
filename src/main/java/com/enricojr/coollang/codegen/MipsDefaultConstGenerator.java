package com.enricojr.coollang.codegen;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.codegen.models.MipsBaseConst;
import com.enricojr.coollang.codegen.models.MipsIntConst;
import com.enricojr.coollang.codegen.models.MipsStrConst;

import java.util.HashMap;
import java.util.Map;

public class MipsDefaultConstGenerator implements AstVisitor {
    private HashMap<String, MipsBaseConst> builtins = new HashMap<>();
    private HashMap<String, Integer> tagTable;

    public MipsDefaultConstGenerator(HashMap<String, Integer> tags) {
        this.tagTable = tags;
    }

    public String getBuiltins() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, MipsBaseConst> e : builtins.entrySet()) {
            sb.append(e.getValue().toCode());
        }
        return sb.toString();
    }

    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {

    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {

    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {

    }

    @Override
    public void visitCoolBinaryOp(CoolBinaryOp cbo) {

    }

    @Override
    public void visitCoolBlock(CoolBlock cb) {

    }

    @Override
    public void visitCoolCase(CoolCase cca) {

    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {

    }

    @Override
    public void visitCoolClass(CoolClass cc) {

    }

    @Override
    public void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd) {

    }

    @Override
    public void visitCoolExpr(CoolExpr ce) {

    }

    @Override
    public void visitCoolFormal(CoolFormal cf) {

    }

    @Override
    public void visitCoolIf(CoolIf cif) {

    }

    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {

    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {

    }

    @Override
    public void visitCoolLet(CoolLet cl) {

    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {

    }

    @Override
    public void visitCoolMethodDispatch(CoolMethodDispatch cmd) {

    }

    @Override
    public void visitCoolParamList(CoolParamList cpl) {

    }

    @Override
    public void visitCoolParenthesisExpr(CoolParenthesisExpr cpe) {

    }

    @Override
    public void visitCoolProgram(CoolProgram cp) {
        MipsIntConst boolFalse = new MipsIntConst("bool_const0", this.tagTable.get("Bool"), 0);
        MipsIntConst boolTrue = new MipsIntConst("bool_const1", this.tagTable.get("Bool"), 1);
        MipsIntConst emptyStrSize = new MipsIntConst("str_empty_size", this.tagTable.get("Int"), 0);
        MipsStrConst emptyString = new MipsStrConst("str_empty", this.tagTable.get("String"), emptyStrSize, null);
        this.builtins.put("boolFalse", boolFalse);
        this.builtins.put("boolTrue", boolTrue);
        this.builtins.put("str_empty", emptyString);
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {

    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {

    }

    @Override
    public void visitCoolString(CoolString cs) {

    }

    @Override
    public void visitCoolBool(CoolBool cb) {

    }

    @Override
    public void visitCoolInteger(CoolInteger ci) {

    }

    @Override
    public void visitCoolSelf(CoolSelf cs) {

    }

    @Override
    public void visitCoolIdentifier(CoolIdentifier ci) {

    }
}
