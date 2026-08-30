package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.builtins.CoolIOType;
import com.enricojr.coollang.ast.builtins.CoolObjectType;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.semantic.exceptions.CoolParentUndefinedException;

import java.util.*;

public class NewClassTreeBuilder implements AstVisitor {
    private HashMap<CoolIdentifier, CoolClass> classList = new HashMap<>();

    public NewClassTreeBuilder() {
        CoolClass object = new CoolObjectType();
        CoolClass io = new CoolIOType();

        this.classList.put(object.getName(), object);
        this.classList.put(io.getName(), io);
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
        // TODO: cycle detection somehow without a tree
        LinkedList<CoolClass> stack = new LinkedList<>();
        CoolClass objectClass = new CoolObjectType();
        CoolClass ioClass = new CoolIOType();

        // need to work BACKWARDS from the class up to object / IO.
        // IO inherits from object implicitly
        if (cc.getParent() != null) {
            CoolClass next = cc.getParent();
            // class itself goes first
            stack.push(cc);
            while (true) {
                if (stack.contains(next)) {
                    String err = String.format(
                            "Cycle detected at `class %s inherits %s`",
                            cc.getName().getValue(),
                            cc.getParentName().getValue()
                    );
                    throw new RuntimeException(err);
                }
                // then the parent
                stack.push(next);
                // then we check for parent
                if (next.getParent() == null) {
                    break;
                } else {
                    next = next.getParent();
                }
            }
            stack.push(cc);
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
        /* TODO: Find a way to throw specific exceptions from here and not RuntimeException */
        HashSet<CoolIdentifier> bannedClasses = new HashSet<>(
                List.of(
                        new CoolIdentifier("String"),
                        new CoolIdentifier("Int"),
                        new CoolIdentifier("Bool")
                )
        );

        for (CoolClass cc : cp.getClasses()) {
            this.classList.put(cc.getName(), cc);
        }

        for (CoolClass cc : cp.getClasses()) {
            if (bannedClasses.contains(cc.getName())) {
                String err;
                if (cc.getName().getValue().equals("IO")) {
                    err = String.format("Class %s is not allowed to override IO.");
                } else {
                    err = String.format("Class %s is not allowed to override Int, Bool, or String.");
                }
                throw new RuntimeException(err);
            }

            if (bannedClasses.contains(cc.getParentName())) {
                String err = String.format("Class %s is not allowed to inherit from Int, Bool, or String");
                throw new RuntimeException(err);
            }

            if (cc.getParentName().equals(cc.getName()))  {
                String err = String.format(
                        "Class %s is not allowed to inherit from itself.", cc.getName().getValue()
                );
                throw new RuntimeException(err);
            }

            if (cc.getParentName() != null) {
                CoolClass parentClass = this.classList.get(cc.getParentName());
                if (parentClass == null) {
                    String err = String.format(
                            "Class %s inherits from nonexistent parent %s.",
                            cc.getName().getValue(),
                            cc.getParentName().getValue()
                    );

                    throw new RuntimeException(err);
                }
                cc.setParent(parentClass);
            }
        }

        // we do the cycle detection in here instead
        for (CoolClass cc : cp.getClasses()) {
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
