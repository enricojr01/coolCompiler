package com.enricojr.coollang.codegen;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.codegen.ast.consts.MipsIntConst;
import com.enricojr.coollang.codegen.ast.consts.MipsStrConst;
import com.enricojr.coollang.codegen.ast.MipsSymbolTable;

import java.util.HashMap;

public class MipsDefaultConstGenerator implements AstVisitor {
    private MipsSymbolTable symbolTable;
    private HashMap<String, Integer> tagTable;
    private String fileName;

    public MipsDefaultConstGenerator(HashMap<String, Integer> tags, MipsSymbolTable symbolTable, String fileName) {
        this.symbolTable = symbolTable;
        this.tagTable = tags;
        this.fileName = fileName;
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

        MipsIntConst intZero = new MipsIntConst("int_zero", this.tagTable.get("Int"), 0);

        MipsIntConst fileNameSize = new MipsIntConst("filename_size", this.tagTable.get("Int"), this.fileName.length());
        MipsStrConst fileName = new MipsStrConst("filename", this.tagTable.get("String"), fileNameSize, this.fileName);

        this.symbolTable.putSymbol("bool_const0", boolFalse);
        this.symbolTable.putSymbol("bool_true", boolTrue);
        this.symbolTable.putSymbol("str_empty", emptyString);
        this.symbolTable.putSymbol("int_zero", intZero);
        this.symbolTable.putSymbol("filename", fileName);
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
