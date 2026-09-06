package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.builtins.*;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SymbolTableBuilder implements AstVisitor {
    private HashSet<CoolIdentifier> dontBother = new HashSet<>(
            List.of(
                    new CoolIdentifier("String"),
                    new CoolIdentifier("Bool"),
                    new CoolIdentifier("Int")
            )
    );

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
        CoolClass typeClass =  st.getSymbolType(cf.getType());
        st.addSymbolType(cf.getName(), typeClass);

        SymbolTable st2 = new SymbolTable();
        st2.setParent(st);
        ce.setSymbols(st2);
        ce.accept(this);
    }

    @Override
    public void visitCoolClass(CoolClass cc) {
        SymbolTable st = cc.getSymbols();

        // NOTE: Object, String, Int, Bool, and IO classes don't have any attributes.
        // I've decided to check and guard vs skipping because I'm worried that skipping will break the
        // order in which they're processed.
        if (cc.getAttributes() != null && cc.getAttributes().size() >= 0) {
            for (CoolAttribute ca : cc.getAttributes()) {
                CoolClass typeClass = st.getSymbolType(ca.getTypeName());
                st.addSymbolType(ca.getIdentifier(), typeClass);
            }
        }

        CoolIdentifier ci = new CoolIdentifier("SELF_TYPE");
        st.addSymbolType(ci, cc);

        if (cc.getMethods() != null && cc.getMethods().size() > 0) {
            for (CoolMethod cm : cc.getMethods()) {
                SymbolTable parameters = new SymbolTable();
                CoolClass returnType = st.getSymbolType(cm.getReturnType());
                if (returnType == null) {
                    String msg = String.format(
                            "Couldn't find type for return type %s in method %s",
                            cm.getReturnType(),
                            cm
                    );
                    System.out.println("examining method: " + cm);
                    st.printSymbolTableChain();
                    throw new RuntimeException(msg);
                }

                for (CoolFormal cf : cm.getParameters().getParameters()) {
                    CoolIdentifier formalName = cf.getName();
                    CoolClass typeClass = st.getSymbolType(cf.getType());
                    parameters.addSymbolType(formalName, typeClass);
                }

                // add to main symbol table
                st.addMethod(cm.getName(), parameters, returnType, cm);
                cm.setSymbols(new SymbolTable(st));
                cm.accept(this);
            }
        }

        if (cc.getChildren() != null && cc.getChildren().size() > 0) {
            for (CoolClass child : cc.getChildren()) {
                // yes, this overwrites the parent we set in the initial pass through the classes
                // but that's how it goes I guess
                child.getSymbols().setParent(st);
                child.accept(this);
            }
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
            CoolClass cc = st.getSymbolType(ca.getTypeName());
            st.addSymbolType(ca.getIdentifier(), cc);
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
            CoolClass cc = st.getSymbolType(cf.getType());
            st.addSymbolType(cf.getName(), cc);
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
        cp.getRoot().getSymbols().setParent(st);

        // NOTE: ClassTreeAnalyzer is now responsible for adding the initial builtins to the
        // list of classes, so this line is now all that's needed to populate the initial symbol table
        for (CoolClass cc : cp.getClasses()) {
            st.addSymbolType(cc.getName(), cc);
        }
        st.addSymbolType(cp.getRoot().getName(), cp.getRoot());

        // all symbol tables need to exist on the classes first
        // before proper linking can take place
        for (CoolClass cc : cp.getClasses()) {
            SymbolTable st2 = new SymbolTable(st);
            st2.setParent(st);
            cc.setSymbols(st2);
        }

        cp.getRoot().accept(this);
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
