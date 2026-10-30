package com.enricojr.coollang.codegen;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.codegen.models.MipsFunctionObj;
import com.enricojr.coollang.codegen.models.MipsIntConst;
import com.enricojr.coollang.codegen.models.MipsStrConst;
import com.enricojr.coollang.codegen.models.MipsSymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MipsFunctionObjGenerator implements AstVisitor {
    private String currentClass = "";
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

        // TODO: need to figure out some way to get the mips code out
//        for (CoolAttribute ca : cc.getAttributes()) {
//            if (ca.getInitExpression() != null) {
//                ca.accept(this);
//            }
//        }

        // TODO: if there's a case expression in the attributes then this goes in one of the labels / branches.
        classInit.addCommand("lw $fp 12($sp)");
        classInit.addCommand("lw $s0 8($sp)");
        classInit.addCommand("lw $ra 4($sp)");
        classInit.addCommand("addiu $sp $sp 12");
        classInit.addCommand("jr $ra");

        // generate the code for the individual methods
        this.symbolTable.putSymbol(classInit.getLabel(), classInit);

        for (CoolMethod cm : cc.getMethods()) {
            this.currentClass = cc.getNameString();
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
        StringBuilder sb = new StringBuilder();
        // remember that regular method dispatch is shorthand for dot method dispatch where
        // the calling class is "self".

        // put self into $s0 to set it aside so we can do stuff with the stack
        sb.append("move $s0 $a0\n");

        CoolIdentifier methodName = cmd.getIdentifier();

        // remember that last argument gets pushed first
        ArrayList<CoolExpr> args = cmd.getArguments();
        args = (ArrayList<CoolExpr>) args.reversed();

        // we need a reference to the size so we can pop the frame correctly later
        int argsLength = args.size();

        for (CoolExpr arg : args) {
            // generate the code for the argument
            arg.accept(this);

            // push argument onto the stack
            sb.append(this.stack.pop());

            sb.append("sw $a0 0($sp)\n");
            sb.append("addiu $sp $sp -4\n");
        }

        String branchLabel = "label" + this.labelCounter;
        sb.append("move $a0 $s0\n");
        sb.append("bne $a0 $zero ").append(branchLabel).append("\n");
        this.labelCounter += 1;

        sb.append(branchLabel).append(":\n");
        // load dispatch table
        // resolve correct method slot
        // jalr to method slot
        // cleanup
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
        // setup
        String contents = cs.getValue();
        String label = "str_const" + this.counter;
        MipsIntConst strSize = new MipsIntConst(label + "_size", this.tagTable.get("Int"), contents.length());

        // create MipsStrConst
        MipsStrConst strConst = new MipsStrConst(label, this.tagTable.get("String"), strSize, contents);

        // add it to the Symbol Table
        this.symbolTable.putSymbol(label, strConst);

        // create instruction to add its label to $a0
        String inst = "la $a0 " + label + "\n";

        // push that instruction onto the stack so the rest of the visitor can access it.
        this.stack.push(inst);

        // increment counter for the next const
        this.counter += 1;
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
