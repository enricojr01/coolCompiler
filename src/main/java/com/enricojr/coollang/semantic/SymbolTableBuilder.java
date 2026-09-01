package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.builtins.*;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

import java.util.ArrayList;

public class SymbolTableBuilder implements AstVisitor {
    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        CoolExpr ce = camd.getLhs();

        // NOTE: Is it really right to be "prop drilling" a symbol table down into the tree like this?
        SymbolTable st1 = camd.getSymbols();
        SymbolTable st2 = new SymbolTable();
        st2.setParent(st1);
        ce.setSymbols(st2);

        ce.accept(this);
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        CoolExpr ce = ca.getValue();

        SymbolTable st1 = ca.getSymbols();
        SymbolTable st2 = new SymbolTable();
        st2.setParent(st1);
        ce.setSymbols(st2);

        ce.accept(this);
    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {
        CoolExpr ce = cas.getExpression();

        SymbolTable st1 = cas.getSymbols();
        SymbolTable st2 = new SymbolTable();
        st2.setParent(st1);
        ce.setSymbols(st2);

        ce.accept(this);
    }

    @Override
    public void visitCoolBinaryOp(CoolBinaryOp cbo) {
        CoolExpr lhs = cbo.getLhs();
        CoolExpr rhs = cbo.getRhs();

        SymbolTable st1 = cbo.getSymbols();
        SymbolTable st2 = new SymbolTable();
        st2.setParent(st1);
        lhs.setSymbols(st2);

        SymbolTable st3 = new SymbolTable();
        st3.setParent(st1);
        lhs.setSymbols(st3);

        rhs.accept(this);
        lhs.accept(this);
    }

    @Override
    public void visitCoolBlock(CoolBlock cb) {
        ArrayList<CoolExpr> exprs = cb.getExpressions();
        SymbolTable st1 = cb.getSymbols();
        for (CoolExpr ce : exprs) {
            SymbolTable st2 = new SymbolTable();
            st2.setParent(st1);
            ce.setSymbols(st2);
            ce.accept(this);
        }
    }

    @Override
    public void visitCoolCase(CoolCase cca) {
        // This symbol table is empty so that the chain of tables
        // preserved - lookups that start in the branches should basically
        // skip over this one as it travels up the chain.
        SymbolTable st = cca.getSymbols();

        CoolExpr pred = cca.getPredicate();
        SymbolTable st2 = new SymbolTable();
        st2.setParent(st);
        pred.accept(this);

        for (CoolCaseBranch ccb : cca.getBranches()) {
            SymbolTable st3 = new SymbolTable();
            st3.setParent(st);
            ccb.setSymbols(st3);
            ccb.accept(this);
        }
    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {
        SymbolTable st = ccb.getSymbols();

        CoolFormal cf = ccb.getFormal();
        CoolExpr ce = ccb.getExpression();
        // NOTE: I will probably need the Type of the formal later, but I don't know if its a good idea to store
        //       just the formal vs the entire branch, so I'll err on the side of caution and shove the whole
        //       branch in there
        st.addSymbol(cf.getName(), ccb);
        st.addType(cf.getName(), st.getType(cf.getType()));

        SymbolTable st2 = new SymbolTable();
        st2.setParent(st);
        ce.setSymbols(st2);
        ce.accept(this);
    }

    @Override
    public void visitCoolClass(CoolClass cc) {
        SymbolTable st = cc.getSymbols();

        for (CoolAttribute ca : cc.getAttributes()) {
            st.addSymbol(ca.getIdentifier(), ca);
            st.addType(ca.getIdentifier(), st.getType(ca.getTypeName()));
        }

        CoolIdentifier ci = new CoolIdentifier("SELF_TYPE");
        st.addType(ci, cc);

        for (CoolMethod cm : cc.getMethods()) {
            st.addSymbol(cm.getName(), cm);
            st.addType(cm.getName(), st.getType(cm.getReturnType()));

            SymbolTable st2 = new SymbolTable();
            st2.setParent(st);
            cm.setSymbols(st2);

            cm.accept(this);
        }

    }

    @Override
    public void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd) {
        CoolExpr lhs = cdmd.getLhs();
        SymbolTable st1 = cdmd.getSymbols();
        SymbolTable st2 = new SymbolTable();

        st2.setParent(st1);
        lhs.setSymbols(st2);

        lhs.accept(this);
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
        SymbolTable st = cif.getSymbols();

        CoolExpr predExpr = cif.getPredicate();
        SymbolTable st2 = new SymbolTable();
        st2.setParent(st);
        predExpr.setSymbols(st2);
        predExpr.accept(this);

        CoolExpr thenExpr = cif.getThenExpr();
        SymbolTable st3 = new SymbolTable();
        st3.setParent(st);
        predExpr.setSymbols(st3);
        thenExpr.accept(this);

        CoolExpr elseExpr = cif.getElseExpr();
        SymbolTable st4 = new SymbolTable();
        st4.setParent(st);
        elseExpr.setSymbols(st4);
        elseExpr.accept(this);
    }


    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {
    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {
        CoolExpr ce = civ.getExpression();

        SymbolTable st1 = civ.getSymbols();
        SymbolTable st2 = new SymbolTable();
        st2.setParent(st1);
        ce.setSymbols(st2);

        ce.accept(this);
    }

    @Override
    public void visitCoolLet(CoolLet cl) {
        SymbolTable st = cl.getSymbols();
        CoolExpr ce = cl.getExpression();
        cl.setSymbols(st);

        for (CoolAttribute ca : cl.getAttributes()) {
            st.addSymbol(ca.getIdentifier(), ca);
            st.addType(ca.getIdentifier(), st.getType(ca.getTypeName()));
        }

        SymbolTable st2 = new SymbolTable();
        st2.setParent(st);
        ce.setSymbols(st2);

        ce.accept(this);
    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        SymbolTable st = cm.getSymbols();
        CoolParamList cpl = cm.getParameters();

        for (CoolFormal cf : cpl.getParameters()) {
            st.addSymbol(cf.getName(), cf);
            st.addType(cf.getName(), st.getType(cf.getType()));
        }

        for (CoolExpr ce : cm.getExpressions()) {
            SymbolTable st2 = new SymbolTable();
            st2.setParent(st);
            ce.setSymbols(st2);
            ce.accept(this);
        }
    }

    @Override
    public void visitCoolMethodDispatch(CoolMethodDispatch cmd) {
        SymbolTable st = cmd.getSymbols();
        for (CoolExpr ce : cmd.getArguments()) {
            SymbolTable st2 = new SymbolTable();
            st2.setParent(st);
            ce.setSymbols(st2);
            ce.accept(this);
        }
    }

    @Override
    public void visitCoolParamList(CoolParamList cpl) {
    }

    @Override
    public void visitCoolParenthesisExpr(CoolParenthesisExpr cpe) {
        CoolExpr ce = cpe.getExpression();
        SymbolTable st = cpe.getSymbols();
        SymbolTable st2 = new SymbolTable();
        st2.setParent(st);

        ce.setSymbols(st2);
        ce.accept(this);
    }

    @Override
    public void visitCoolProgram(CoolProgram cp) {
        SymbolTable st = new SymbolTable();
        cp.setSymbols(st);

        // IO, Int, String, and Bool need to be added to the top-level
        // symbol table
        CoolClass objType = new CoolObjectType();
        CoolClass ioType = new CoolIOType();
        CoolClass intType = new CoolIntegerType();
        CoolClass strType = new CoolStringType();
        CoolClass boolType = new CoolBooleanType();

        st.addType(objType.getName(), objType);
        st.addType(ioType.getName(), ioType);
        st.addType(intType.getName(), intType);
        st.addType(strType.getName(), strType);
        st.addType(boolType.getName(), boolType);

        for (CoolClass cc : cp.getClasses() ) {
            st.addSymbol(cc.getName(), cc);
            st.addType(cc.getName(), cc);
        }

        for (CoolClass cc : cp.getClasses()) {
            SymbolTable st2 = new SymbolTable();
            st2.setParent(st);
            cc.setSymbols(st2);
            cc.accept(this);
        }
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {
        CoolExpr ce = cuo.getExpression();
        SymbolTable st1 = cuo.getSymbols();
        SymbolTable st2 = new SymbolTable();
        st2.setParent(st1);
        ce.setSymbols(st2);
        ce.accept(this);
    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {
        SymbolTable st1 = cw.getSymbols();
        CoolExpr pred = cw.getPredicate();
        SymbolTable st2 = new SymbolTable();
        st2.setParent(st1);
        pred.setSymbols(st2);
        pred.accept(this);

        CoolExpr body = cw.getBody();
        SymbolTable st3 = new SymbolTable();
        st3.setParent(st1);
        body.setSymbols(st3);
        body.accept(this);
    }
}
