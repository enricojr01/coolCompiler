package com.enricojr.coollang.codegen;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.codegen.ast.MipsFunctionObj;
import com.enricojr.coollang.codegen.ast.consts.MipsIntConst;
import com.enricojr.coollang.codegen.ast.consts.MipsStrConst;
import com.enricojr.coollang.codegen.ast.MipsSymbolTable;

import java.util.HashMap;
import java.util.LinkedList;

public class MipsFunctionObjGenerator implements AstVisitor {
    private CoolClass currentClass;
    private CoolMethod currentMethod;
    private MipsSymbolTable symbolTable;
    private HashMap<String, Integer> tagTable;
    private LinkedList<String> stack = new LinkedList<>();
    private int counter = 1;
    private int labelCounter = 1;

    public MipsFunctionObjGenerator(MipsSymbolTable symbols, HashMap<String, Integer> tags) {
        this.symbolTable = symbols;
        this.tagTable = tags;
    }

    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {

    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        // at this point this will only be called if the initExpression != null
        // so no need to check here.
        CoolExpr ce = ca.getInitExpression();
        ce.accept(this);
    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {

    }

    @Override
    public void visitCoolBinaryOp(CoolBinaryOp cbo) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Binary Operation: ").append(cbo);
        CoolExpr lh = cbo.getLhs();
        lh.accept(this);
        sb.append(this.stack.pop());
        // note that it's always 12($a0) because $a0 is an Int _object_
        // and the actual value we need to add is in the third slot
        sb.append("lw $t1 12($a0)\n");

        CoolExpr rh = cbo.getRhs();
        rh.accept(this);
        sb.append(this.stack.pop());
        sb.append("lw $t2 12($a0)\n");

        // creates an Int for the result and stores pointer to it in $a0
        sb.append("jal Object.copy\n");

        CoolBinaryOp.OPERATOR op = cbo.getOp();
        switch(op) {
            case CoolBinaryOp.OPERATOR.ADD -> {
                sb.append("add $t1 $t1 $t2\n");
            }
            case CoolBinaryOp.OPERATOR.SUB -> {
                sb.append("sub $t1 $t1 $t2\n");
            }
            case CoolBinaryOp.OPERATOR.MUL -> {
                sb.append("mul $t1 $t1 $t2\n");
            }
            // NOTE: keep an eye on this one
            case CoolBinaryOp.OPERATOR.DIV -> {
                sb.append("div $t1 $t1 $t2\n");
            }
        }

        sb.append("sw $t1 12($a0)\n");
        this.stack.push(sb.toString());
    }

    @Override
    public void visitCoolBlock(CoolBlock cb) {
        for (CoolExpr ce : cb.getExpressions()) {
            ce.accept(this);
        }
    }

    @Override
    public void visitCoolCase(CoolCase cca) {

    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {

    }

    @Override
    public void visitCoolClass(CoolClass cc) {
        this.currentClass = cc;
        MipsFunctionObj classInit = new MipsFunctionObj(cc.getNameString() + "_init");
        // generate _init function passing in each attribute
        // if it has an init expression.
        // _init function has to invoke the constructor for the parent
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
            if (ca.getInitExpression() != null) {
                ca.accept(this);
                classInit.addCommand(this.stack.pop());
            }
        }

        // NOTE: it appears that my earlier assumption may have been false, the way the ASM is
        // structured kinda misled me into thinking that this would be moved into a branch or something
        // when in fact it can be pasted at the end of a function / class and would work fine.
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
        // note that this is the method _definition_ and not the dispatch
        this.currentMethod = cm;

        MipsFunctionObj func = new MipsFunctionObj(this.currentClass + "." + cm.getNameString());

        func.addCommand("addiu $sp $sp -12");
        func.addCommand("sw $fp 12($sp)");
        func.addCommand("sw $s0 8($sp)");
        func.addCommand("sw $ra 4($sp)");
        func.addCommand("addiu $fp $sp 4");

        for (CoolExpr expr : cm.getBody()) {
            expr.accept(this);
            func.addCommand(this.stack.pop());
            func.addCommand("addiu $sp $sp -4");
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
        CoolExpr ce = cpe.getExpression();
        ce.accept(this);
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
        StringBuilder sb = new StringBuilder();
        String contents = cs.getValue();
        String label = "str_const" + this.counter;
        MipsIntConst strSize = new MipsIntConst(label + "_size", this.tagTable.get("Int"), contents.length());

        // create MipsStrConst
        MipsStrConst strConst = new MipsStrConst(label, this.tagTable.get("String"), strSize, contents);

        // add it to the Symbol Table
        this.symbolTable.putSymbol(label, strConst);

        // create instruction to add its label to $a0
        sb.append("# string literal: ").append(cs).append("\n");
        sb.append("la $a0 ").append(label).append("\n");

        // push that instruction onto the stack so the rest of the visitor can access it.
        this.stack.push(sb.toString());

        // increment counter for the next const
        this.counter += 1;
    }

    @Override
    public void visitCoolBool(CoolBool cb) {
        StringBuilder sb = new StringBuilder();
        boolean value = cb.getValue();
        String inst = "";

        // This may be incorrect - I'm not sure.
        sb.append("# Bool constant: ").append(cb);
        if (value) {
            sb.append("la $a0 bool_const1\n");
        } else {
            sb.append("la $a0 bool_const0\n");
        }

        this.stack.push(inst);
    }

    @Override
    public void visitCoolInteger(CoolInteger ci) {
        StringBuilder sb = new StringBuilder();
        int contents = ci.getValue();
        String label = "int_const" + this.counter;
        MipsIntConst intConst = new MipsIntConst(label, this.tagTable.get("Int"), contents);

        this.symbolTable.putSymbol(label, intConst);
        sb.append("# integer literal:").append(ci);
        sb.append("la $a0 ").append("label").append("\n");

        this.stack.push(sb.toString());
        this.counter += 1;
    }

    @Override
    public void visitCoolSelf(CoolSelf cs) {
    }

    @Override
    public void visitCoolIdentifier(CoolIdentifier ci) {
        // cannot access local vars from other classes so these are always local
        // just need to figure out if they're local to the method or to the thing
    }
}
