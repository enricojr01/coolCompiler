package com.enricojr.coollang.ast;

import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

public interface AstVisitor {
    void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd);
    void visitCoolAttribute(CoolAttribute ca);
    void visitCoolAssign(CoolAssign cas);
    void visitCoolBinaryOp(CoolBinaryOp cbo);
    void visitCoolBlock(CoolBlock cb);
    void visitCoolCase(CoolCase cca);
    void visitCoolCaseBranch(CoolCaseBranch ccb);
    void visitCoolClass(CoolClass cc);
    void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd);
    void visitCoolExpr(CoolExpr ce);
    void visitCoolFormal(CoolFormal cf);
    void visitCoolIf(CoolIf cif);
    void visitCoolInstantiate(CoolInstantiate ci);
    void visitCoolIsVoid(CoolIsVoid civ);
    void visitCoolLet(CoolLet cl);
    void visitCoolMethod(CoolMethod cm);
    void visitCoolMethodDispatch(CoolMethodDispatch cmd);
    void visitCoolParamList(CoolParamList cpl);
    void visitCoolParenthesisExpr(CoolParenthesisExpr cpe);
    void visitCoolProgram(CoolProgram cp);
    void visitCoolUnaryOp(CoolUnaryOp cuo);
    void visitCoolWhile(CoolWhile cw);
}
