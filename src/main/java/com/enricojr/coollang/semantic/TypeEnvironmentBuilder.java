package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.builtins.CoolIOType;
import com.enricojr.coollang.ast.builtins.CoolIntegerType;
import com.enricojr.coollang.ast.builtins.CoolStringType;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

import java.util.HashMap;

public class TypeEnvironmentBuilder implements AstVisitor {
    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        camd.getLhs().accept(this);
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {

    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {
        cas.getExpression().accept(this);
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
        TypeEnvironment te = cc.getTypes();
        for (CoolAttribute ca : cc.getAttributes()) {
            CoolClass targetClass = te.getType(ca.getTypeName());
            te.addType(ca.getIdentifier(), targetClass);
        }

        for (CoolMethod cm : cc.getMethods()) {
            for (CoolFormal cf : cm.getParameters().getParameters()) {
                CoolClass type = te.getType(cf.getName());
                te.addType(cf.getName(), type);
            }

            TypeEnvironment te2 = new TypeEnvironment();
            te2.setParent(te);
            cm.setTypes(te2);

            cm.accept(this);
        }
    }

    @Override
    public void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd) {

    }

    @Override
    public void visitCoolExpr(CoolExpr ce) {
        ce.accept(this);
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
        TypeEnvironment types = cm.getTypes();
        CoolParamList cpl = cm.getParameters();

        for (CoolFormal cf : cpl.getParameters()) {
            types.addType(cf.getName(), types.getType(cf.getName()));
        }

        for (CoolExpr ce : cm.getExpressions()) {
            TypeEnvironment te2 = new TypeEnvironment();
            ce.setTypes(te2);
            ce.accept(this);
        }
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
        TypeEnvironment te = new TypeEnvironment();

        CoolIOType ioType = new CoolIOType();
        CoolIntegerType intType = new CoolIntegerType();
        CoolStringType strType = new CoolStringType();

        te.addType(ioType.getName(), ioType);
        te.addType(intType.getName(), ioType);
        te.addType(strType.getName(), ioType);

        for (CoolClass cc : cp.getClasses()) {
            te.addType(cc.getName(), cc);

            TypeEnvironment te2 = new TypeEnvironment();
            te2.setParent(te);
            cc.setTypes(te2);

            cc.accept(this);
        }
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {

    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {

    }
}
