package com.enricojr.coollang.codegen;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.codegen.models.MipsIntConst;
import com.enricojr.coollang.codegen.models.MipsStrConst;
import com.enricojr.coollang.codegen.models.MipsSymbolTable;

import java.util.HashMap;

public class MipsClassNameConstGenerator implements AstVisitor {
    private MipsSymbolTable symbolTable;
    private HashMap<String, Integer> classTagTable;

    public MipsClassNameConstGenerator(HashMap<String, Integer> classTags, MipsSymbolTable symbolTable) {
        this.classTagTable = classTags;
        this.symbolTable = symbolTable;
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
        String label = cc.getNameString() + "_name";
        String sizeLabel = label + "_size";

        int intTag = this.classTagTable.get("Int");
        int classTag = this.classTagTable.get(cc.getNameString());

        MipsIntConst sizeConst = new MipsIntConst(sizeLabel, intTag, label.length());
        MipsStrConst classNameConst = new MipsStrConst(label, classTag, sizeConst, label);

        this.symbolTable.putSymbol(label, classNameConst);

        for (CoolClass child : cc.getChildren()) {
            child.accept(this);
        }
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
        CoolClass root = cp.getRoot();

        root.accept(this);
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
