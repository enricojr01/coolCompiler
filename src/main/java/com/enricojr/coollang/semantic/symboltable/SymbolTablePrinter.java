package com.enricojr.coollang.semantic.symboltable;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SymbolTablePrinter implements AstVisitor {
    private int indent = 0;
    private final String space = " ";
    private final int offset = 2;
    private final HashSet<CoolIdentifier> dontBother = new HashSet<>(
            List.of(
                    new CoolIdentifier("Int"),
                    new CoolIdentifier("String"),
                    new CoolIdentifier("Bool"),
                    new CoolIdentifier("IO")
            )
    );

    public void printSymbol(Map.Entry<CoolIdentifier, SymbolTableEntry> entry) {
        SymbolTableEntry ste = entry.getValue();
        String msg = String.format(
                "Symbol - %s : %s",
                entry.getKey().getValueString(),
                ste.getTypeString()
        );
        System.out.println(this.space.repeat(this.indent) + msg);
    }

    public void printMethodSymbol(Map.Entry<CoolIdentifier, MethodTableEntry> entry) {
        String msg = String.format("Method - %s", entry.getValue());
        System.out.println(this.space.repeat(this.indent) + msg);
    }

    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        this.indent += offset;
        camd.getLhs().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        this.indent += offset;
        ca.getInitExpression().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {
        this.indent += offset;
        cas.getExpression().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolBinaryOp(CoolBinaryOp cbo) {
        this.indent += offset;
        cbo.getLhs().accept(this);
        cbo.getRhs().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolBlock(CoolBlock cb) {
        this.indent += offset;
        for (CoolExpr ce : cb.getExpressions()) {
            ce.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolCase(CoolCase cca) {
        System.out.println(this.space.repeat(this.indent) + cca);
        this.indent += offset;
        SymbolTable st = cca.getSymbols();
        for (Map.Entry<CoolIdentifier, SymbolTableEntry> entry : st.getTypes().entrySet()) {
            this.printSymbol(entry);
        }
        for (CoolCaseBranch ccb : cca.getBranches()) {
            ccb.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {
        this.indent += offset;
        for (Map.Entry<CoolIdentifier, SymbolTableEntry> entry : ccb.getSymbols().getTypes().entrySet()) {
            this.printSymbol(entry);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolClass(CoolClass cc) {
        System.out.println(this.space.repeat(this.indent) + cc);
        SymbolTable symbols = cc.getSymbols();

        this.indent += offset;
        for (Map.Entry<CoolIdentifier, SymbolTableEntry> entry : symbols.getTypes().entrySet()) {
            this.printSymbol(entry);
        }

        for (Map.Entry<CoolIdentifier, MethodTableEntry> entry : symbols.getMethods().entrySet()) {
            this.printMethodSymbol(entry);
        }

        for (CoolMethod cm : cc.getMethods()) {
            cm.accept(this);
        }
        for (CoolClass child : cc.getChildren()) {
            child.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd) {
        this.indent += offset;
        CoolExpr ce = cdmd.getClassName();
        ce.accept(this);
        this.indent -= offset;
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
        this.indent += offset;
        CoolExpr predExpr = cif.getGuard();
        CoolExpr thenExpr = cif.getConsequent();
        CoolExpr elseExpr = cif.getAlternative();

        predExpr.accept(this);
        thenExpr.accept(this);
        elseExpr.accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {
    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {
        this.indent += offset;
        CoolExpr ce = civ.getExpression();
        ce.accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolLet(CoolLet cl) {
        System.out.println(this.space.repeat(this.indent) + cl);

        this.indent += offset;
        for (Map.Entry<CoolIdentifier, SymbolTableEntry> entry : cl.getSymbols().getTypes().entrySet()) {
            this.printSymbol(entry);
        }
        cl.getExpression().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        System.out.println(this.space.repeat(this.indent) + cm.getName().getValue() + " {");
        this.indent += offset;
        SymbolTable st = cm.getSymbols();

        for (Map.Entry<CoolIdentifier, SymbolTableEntry> entry : st.getTypes().entrySet()) {
            this.printSymbol(entry);
        }

        for (CoolExpr ce : cm.getBody()) {
            ce.accept(this);
        }

        this.indent -= offset;
        System.out.println(this.space.repeat(this.indent) + "}");
    }

    @Override
    public void visitCoolMethodDispatch(CoolMethodDispatch cmd) {
        System.out.println(this.space.repeat(this.indent) + cmd);
        this.indent += offset;
        for (CoolExpr ce : cmd.getArguments()) {
            ce.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolParamList(CoolParamList cpl) {
    }

    @Override
    public void visitCoolParenthesisExpr(CoolParenthesisExpr cpe) {
        this.indent += offset;
        cpe.getExpression().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolProgram(CoolProgram cp) {
        System.out.println(this.space.repeat(indent) + "CoolProgram:");

        this.indent += offset;
        for (Map.Entry<CoolIdentifier, SymbolTableEntry> entry : cp.getSymbols().getTypes().entrySet()) {
            this.printSymbol(entry);
        }

        cp.getRoot().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {
        this.indent += offset;
        cuo.getExpression().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {
        this.indent += offset;
        cw.getPredicate().accept(this);
        cw.getBody().accept(this);
        this.indent -= offset;
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
}
