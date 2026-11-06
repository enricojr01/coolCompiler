package com.enricojr.coollang.codegen;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.builtins.CoolBuiltInType;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.codegen.ast.MipsDefaultInitEntry;
import com.enricojr.coollang.codegen.ast.consts.MipsBaseConst;
import com.enricojr.coollang.semantic.symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;

public class MipsAttribPositionCalculator implements AstVisitor {
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
        System.out.println("Class: " + cc.getNameString());
        SymbolTable current = cc.getSymbols();
        ArrayList<CoolAttribute> attribs = cc.getOrderedAttributeChain();
        ArrayList<CoolMethod> methods = cc.getOrderedMethodList();
        HashMap<CoolAttribute, MipsDefaultInitEntry> attribMap = new HashMap<>();
        HashMap<String, Integer> methodMap = new HashMap<>();

        for (int i = 0; i < attribs.size(); i++) {
            CoolAttribute ca = attribs.get(i);
            CoolClass resolvedType = current.getSymbolType(ca.getTypeName());
            if (resolvedType instanceof CoolBuiltInType cbit) {
                String defaultValue = cbit.defaultInit();
                MipsDefaultInitEntry mdie = new MipsDefaultInitEntry(i, defaultValue);
                attribMap.put(ca, mdie);
            }
        }

        for (int i = 0; i < methods.size(); i++) {
            CoolMethod cm = methods.get(i);
            methodMap.put(cm.getNameString(), i);
        }

        // generate an entire protObj from this thing
        System.out.println(attribMap);
        System.out.println(methodMap);
        System.out.println();

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
