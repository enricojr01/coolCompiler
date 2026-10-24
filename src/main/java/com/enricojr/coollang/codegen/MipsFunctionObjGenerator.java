package com.enricojr.coollang.codegen;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.codegen.models.MipsFunctionObj;
import com.enricojr.coollang.codegen.models.MipsSymbolTable;

public class MipsFunctionObjGenerator implements AstVisitor {
    private String currentClass = "";
    private MipsSymbolTable symbolTable;

    public MipsFunctionObjGenerator(MipsSymbolTable symbols) {
        this.symbolTable = symbols;
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
        StringBuilder sb = new StringBuilder();
        this.currentClass = cc.getNameString();

        MipsFunctionObj classInit = new MipsFunctionObj(cc.getNameString() + "_init");
        // generate _init function passing in each attribute
        // if it has an init expression.
        // _init function has to invoke the constructors for each
        // parent
        classInit.addCommand("addiu $sp $sp -12");
        classInit.addCommand("sw $fp 12($sp)");
        classInit.addCommand("sw $s0 8($sp)");
        classInit.addCommand("sw $ra 4($sp)");
        classInit.addCommand("addiu $fp $sp 4");
        classInit.addCommand("move $s0 $a0");

        String parentInit = cc.getParentNameString() + "_init";
        classInit.addCommand("jal " + parentInit);
        classInit.addCommand("move $a0 $s0");

        for (CoolAttribute ca : cc.getAttributes()) {
            // TODO
        }

        // TODO: if there's a case expression in the attributes then this goes in one of the labels / branches.
        classInit.addCommand("lw $fp 12($sp)");
        classInit.addCommand("lw $s0 8($sp)");
        classInit.addCommand("lw $ra 4($sp)");
        classInit.addCommand("addiu $sp $sp 12");
        classInit.addCommand("jr $ra");

        // generate the code for the individual methods
        this.symbolTable.putSymbol(classInit.getLabel(), classInit);

        for (CoolMethod cm : cc.getMethods()) {
            cm.accept(this);
        }

        for (CoolClass child : cc.getChildren()) {
            child.accept(this);
            // TODO
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
