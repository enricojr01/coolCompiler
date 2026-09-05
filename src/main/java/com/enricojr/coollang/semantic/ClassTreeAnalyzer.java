package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.builtins.*;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

import java.util.*;

public class ClassTreeAnalyzer implements AstVisitor {
    private HashMap<CoolIdentifier, CoolClass> classList = new HashMap<>();

    public ClassTreeAnalyzer() {
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
        LinkedList<CoolClass> stack = new LinkedList<>();
//        CoolClass objectClass = new CoolObjectType();
//        CoolClass ioClass = new CoolIOType();

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
                    throw new RuntimeException(err);
                }
                // otherwise push it onto the stack
                stack.push(next);
            }

            // if execution reaches here, and no error has occurred, then everything should be fine.
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

        ArrayList<CoolClass> builtins = new ArrayList<>(
                List.of(
                        new CoolIOType(cp.getRoot()),
                        new CoolObjectType(cp.getRoot()),
                        new CoolIntegerType(cp.getRoot()),
                        new CoolStringType(cp.getRoot()),
                        new CoolBooleanType(cp.getRoot())
                )
        );
        cp.getClasses().addAll(builtins);

        // we loop twice through CoolProgram
        // first loop builds up a list of classes
        for (CoolClass cc : cp.getClasses()) {
            this.classList.put(cc.getName(), cc);
        }

        cp.setRoot(new CoolObjectType());

        // second loop enforces inheritance rules:
        // classes can't override IO, Int, Bool, or String
        // classes can't inherit from Int, Bool, or String,
        // classes can't inherit from themselves
        // at the end of the second loop, the parent field is set.
        for (CoolClass cc : cp.getClasses()) {
            if (!(cc instanceof CoolBuiltInType) && bannedClasses.contains(cc.getName())) {
                String err = String.format("Class %s is not allowed to override Int, Bool, or String.", cc.getName().getValue());
                throw new RuntimeException(err);
            }

            if (!(cc instanceof CoolBuiltInType) && cc.getName().getValue().equals("IO")) {
               String err = String.format("Class %s is not allowed to override IO.", cc.getName().getValue());
               throw new RuntimeException(err);
            }

            if (!(cc instanceof CoolBuiltInType) && bannedClasses.contains(cc.getParentName())) {
                String err = String.format("Class %s is not allowed to inherit from Int, Bool, or String");
                throw new RuntimeException(err);
            }

            if (cc.getParentName() != null && cc.getParentName().equals(cc.getName()))  {
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
                parentClass.addChild(cc);
            } else {
                cc.setParentName(cp.getRoot().getName());
                cc.setParent(cp.getRoot());
                cp.getRoot().addChild(cc);
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
