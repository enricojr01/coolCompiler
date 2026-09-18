package com.enricojr.coollang.semantic.classtree;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.builtins.*;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.semantic.exceptions.StackTraceException;

import java.util.*;

public class ClassTreeBuilder implements AstVisitor {
    private final HashMap<CoolIdentifier, CoolClass> classList = new HashMap<>();
    private LinkedList<CoolBaseNode> stack = new LinkedList<>();
    private String fileName;

    public ClassTreeBuilder(String fileName) {
        CoolClass object = new CoolObjectType();
        CoolClass io = new CoolIOType();

        this.classList.put(object.getName(), object);
        this.classList.put(io.getName(), io);
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
        String location = "ClassTreeBuilder - Cycle Detector";
        LinkedList<CoolClass> stack = new LinkedList<>();

        // Cycle detection is done with a stack - starting with the
        // class itself,
        // need to work BACKWARDS from the class up to object / IO.
        // IO inherits from object implicitly
        if (cc.getParent() != null) {
            CoolClass next = cc.getParent();
            // class itself goes first
            stack.push(cc);
            while (true) {
                // check to see if there's a parent to follow
                if (next.getParent() == null) {
                    // if getParent() == null, class inherits from object,
                    // and we don't need to check
                    break;
                } else {
                    // if getParent() == true we pull it in so that the
                    // `if` check that follows checks the new class, not the one
                    // we just pushed onto the stack
                    next = next.getParent();
                }
                // if the new class is already on the stack,
                if (stack.contains(next)) {
                    // throw an exception and halt compilation
                    String err = String.format(
                            "Cycle detected at `class %s inherits %s`",
                            cc.getName().getValue(),
                            cc.getParentName().getValue()
                    );
                    throw new StackTraceException(err, this.fileName, location, this.stack);
                }
                // otherwise push it onto the stack
                stack.push(next);
            }
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
        String location = "ClassTreeBuilder - Class Checker";
        HashSet<CoolIdentifier> bannedClasses = new HashSet<>(
                List.of(
                        new CoolIdentifier("String"),
                        new CoolIdentifier("Int"),
                        new CoolIdentifier("Bool")
                )
        );

        // we loop twice through CoolProgram
        // first loop builds up a list of classes
        for (CoolClass cc : cp.getClasses()) {
            this.classList.put(cc.getName(), cc);
        }

        // second loop enforces inheritance rules:
        // classes can't override IO, Int, Bool, or String
        // classes can't inherit from Int, Bool, or String,
        // classes can't inherit from themselves
        // at the end of the second loop, the parent field is set.
        for (CoolClass cc : cp.getClasses()) {
            this.stack.push(cc);
            if (!(cc instanceof CoolBuiltInType) && bannedClasses.contains(cc.getName())) {
                String err = String.format("Class %s is not allowed to override Int, Bool, or String.", cc.getNameString());

                // TODO: Create new subclass of RuntimeException and swap these out
                throw new StackTraceException(err, this.fileName, location, this.stack);
            }

            if (!(cc instanceof CoolBuiltInType) && cc.getName().getValue().equals("IO")) {
               String err = String.format("Class %s is not allowed to override IO.", cc.getNameString());
               throw new StackTraceException(err, this.fileName, location, this.stack);
            }

            if (!(cc instanceof CoolBuiltInType) && bannedClasses.contains(cc.getParentName())) {
                String err = String.format("Class %s is not allowed to inherit from Int, Bool, or String", cc.getNameString());
                throw new StackTraceException(err, this.fileName, location, this.stack);
            }

            if (cc.getParentName() != null && cc.getParentName().equals(cc.getName()))  {
                String err = String.format(
                        "Class %s is not allowed to inherit from itself.", cc.getNameString()
                );
                throw new StackTraceException(err, this.fileName, location, this.stack);
            }

            if (cc.getNameString().equals("SELF_TYPE")) {
                String err = String.format("Class %s is not allowed to be named SELF_TYPE.", cc.getNameString());
                throw new StackTraceException(err, this.fileName, location, this.stack);
            }

            if (cc.getParentName() != null && cc.getParentName().equals(new CoolIdentifier("SELF_TYPE"))) {
                String err = String.format("Class %s is not allowed to inherit from SELF_TYPE", cc.getNameString());
                throw new StackTraceException(err, this.fileName, location, this.stack);
            }

            if (cc.getParentName() != null && this.classList.get(cc.getParentName()) == null) {
                String err = String.format(
                        "Class %s inherits from nonexistent class %s.",
                        cc.getNameString(),
                        cc.getParentNameString()
                );

                throw new StackTraceException(err, this.fileName, location, this.stack);
            }
        }

        // empty it out before doing cycle detection;
        this.stack.clear();

        // we do the cycle detection in here instead
        for (CoolClass cc : cp.getClasses()) {
            this.stack.push(cc);
            cc.accept(this);
        }
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
