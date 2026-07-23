package com.enricojr.coollang.ast;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import com.enricojr.coollang.CoolBaseVisitor;
import com.enricojr.coollang.CoolParser.AddContext;
import com.enricojr.coollang.CoolParser.AssignContext;
import com.enricojr.coollang.CoolParser.AtMethodDispatchContext;
import com.enricojr.coollang.CoolParser.AttributeContext;
import com.enricojr.coollang.CoolParser.AttributeDefContext;
import com.enricojr.coollang.CoolParser.CaseStatementContext;
import com.enricojr.coollang.CoolParser.CodeBlockContext;
import com.enricojr.coollang.CoolParser.ComplementContext;
import com.enricojr.coollang.CoolParser.CoolClassContext;
import com.enricojr.coollang.CoolParser.DivideContext;
import com.enricojr.coollang.CoolParser.DotMethodDispatchContext;
import com.enricojr.coollang.CoolParser.ExprContext;
import com.enricojr.coollang.CoolParser.FalseContext;
import com.enricojr.coollang.CoolParser.FeatureContext;
import com.enricojr.coollang.CoolParser.FormalContext;
import com.enricojr.coollang.CoolParser.GtContext;
import com.enricojr.coollang.CoolParser.GteContext;
import com.enricojr.coollang.CoolParser.IdentifierContext;
import com.enricojr.coollang.CoolParser.IfStatementContext;
import com.enricojr.coollang.CoolParser.InstantiateContext;
import com.enricojr.coollang.CoolParser.IntegerContext;
import com.enricojr.coollang.CoolParser.IsEqualContext;
import com.enricojr.coollang.CoolParser.IsVoidContext;
import com.enricojr.coollang.CoolParser.LetStatementContext;
import com.enricojr.coollang.CoolParser.LtContext;
import com.enricojr.coollang.CoolParser.LteContext;
import com.enricojr.coollang.CoolParser.MethodDefContext;
import com.enricojr.coollang.CoolParser.MethodDefinitionContext;
import com.enricojr.coollang.CoolParser.MethodDispatchContext;
import com.enricojr.coollang.CoolParser.MultiplyContext;
import com.enricojr.coollang.CoolParser.NotContext;
import com.enricojr.coollang.CoolParser.ParamListContext;
import com.enricojr.coollang.CoolParser.ParenthesisExprContext;
import com.enricojr.coollang.CoolParser.ProgContext;
import com.enricojr.coollang.CoolParser.SelfContext;
import com.enricojr.coollang.CoolParser.StringContext;
import com.enricojr.coollang.CoolParser.SubtractContext;
import com.enricojr.coollang.CoolParser.TrueContext;
import com.enricojr.coollang.CoolParser.WhileStatementContext;
import com.enricojr.coollang.ast.expressions.CoolExpr;
import com.enricojr.coollang.ast.program.CoolAttribute;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolFormal;
import com.enricojr.coollang.ast.program.CoolMethod;
import com.enricojr.coollang.ast.program.CoolProgram;

public class AstBuilder extends CoolBaseVisitor<CoolBaseNode> {

    @Override
    public CoolBaseNode visitAdd(AddContext ctx) {
        // TODO Auto-generated method stub
        return super.visitAdd(ctx);
    }

    @Override
    public CoolBaseNode visitAssign(AssignContext ctx) {
        // TODO Auto-generated method stub
        return super.visitAssign(ctx);
    }

    @Override
    public CoolBaseNode visitAtMethodDispatch(AtMethodDispatchContext ctx) {
        // TODO Auto-generated method stub
        return super.visitAtMethodDispatch(ctx);
    }

    @Override
    public CoolBaseNode visitAttribute(AttributeContext ctx) {
        // TODO Auto-generated method stub
        return super.visitAttribute(ctx);
    }

    @Override
    public CoolBaseNode visitAttributeDef(AttributeDefContext ctx) {
        // attribute:          ID ':' TYPE ('<-' expr)?  SEMICOLON?;
        CoolAttribute ca = new CoolAttribute();
        AttributeContext ac = ctx.attribute();

        ca.setIdentifier(ac.ID().getText());
        ca.setTypeName(ac.TYPE().getText());

        return ca;
    }

    @Override
    public CoolBaseNode visitCaseStatement(CaseStatementContext ctx) {
        // TODO Auto-generated method stub
        return super.visitCaseStatement(ctx);
    }

    @Override
    public CoolBaseNode visitCodeBlock(CodeBlockContext ctx) {
        // TODO Auto-generated method stub
        return super.visitCodeBlock(ctx);
    }

    @Override
    public CoolBaseNode visitComplement(ComplementContext ctx) {
        // TODO Auto-generated method stub
        return super.visitComplement(ctx);
    }

    @Override
    public CoolBaseNode visitCoolClass(CoolClassContext ctx) {
        // coolClass:   CLASS TYPE (INHERITS TYPE)? '{' feature* '};';
        CoolClass cc = new CoolClass();
        List<TerminalNode> types = ctx.TYPE();

        if (types.size() == 2) {
            String parentName = types.get(1).getSymbol().getText();
            cc.setParentName(parentName);
        }

        String className = types.get(0).getSymbol().getText();
        cc.setName(className);

        ArrayList<CoolAttribute> cas = new ArrayList<>();
        ArrayList<CoolMethod> cms = new ArrayList<>();
        List<FeatureContext> features = ctx.feature();
        for (FeatureContext fc : features) {
            if (fc instanceof AttributeDefContext) {
                CoolAttribute ca = (CoolAttribute) this.visitAttributeDef((AttributeDefContext) fc);
                cas.add(ca);
            }

            if (fc instanceof MethodDefContext) {
                CoolMethod cm = (CoolMethod) this.visitMethodDef((MethodDefContext) fc);
                cms.add(cm);


                // TODO: straighten out terminology so its consistent (identifiers vs names)
                cm.setName(mdc.ID().getText());
                cm.setParams(methodParams);
            }
            
        }

        return cc;
    }

    @Override
    public CoolBaseNode visitDivide(DivideContext ctx) {
        // TODO Auto-generated method stub
        return super.visitDivide(ctx);
    }

    @Override
    public CoolBaseNode visitDotMethodDispatch(DotMethodDispatchContext ctx) {
        // TODO Auto-generated method stub
        return super.visitDotMethodDispatch(ctx);
    }

    @Override
    public CoolBaseNode visitFalse(FalseContext ctx) {
        // TODO Auto-generated method stub
        return super.visitFalse(ctx);
    }

    @Override
    public CoolBaseNode visitFormal(FormalContext ctx) {
        // TODO Auto-generated method stub
        return super.visitFormal(ctx);
    }

    @Override
    public CoolBaseNode visitGt(GtContext ctx) {
        // TODO Auto-generated method stub
        return super.visitGt(ctx);
    }

    @Override
    public CoolBaseNode visitGte(GteContext ctx) {
        // TODO Auto-generated method stub
        return super.visitGte(ctx);
    }

    @Override
    public CoolBaseNode visitIdentifier(IdentifierContext ctx) {
        // TODO Auto-generated method stub
        return super.visitIdentifier(ctx);
    }

    @Override
    public CoolBaseNode visitIfStatement(IfStatementContext ctx) {
        // TODO Auto-generated method stub
        return super.visitIfStatement(ctx);
    }

    @Override
    public CoolBaseNode visitInstantiate(InstantiateContext ctx) {
        // TODO Auto-generated method stub
        return super.visitInstantiate(ctx);
    }

    @Override
    public CoolBaseNode visitInteger(IntegerContext ctx) {
        // TODO Auto-generated method stub
        return super.visitInteger(ctx);
    }

    @Override
    public CoolBaseNode visitIsEqual(IsEqualContext ctx) {
        // TODO Auto-generated method stub
        return super.visitIsEqual(ctx);
    }

    @Override
    public CoolBaseNode visitIsVoid(IsVoidContext ctx) {
        // TODO Auto-generated method stub
        return super.visitIsVoid(ctx);
    }

    @Override
    public CoolBaseNode visitLetStatement(LetStatementContext ctx) {
        // TODO Auto-generated method stub
        return super.visitLetStatement(ctx);
    }

    @Override
    public CoolBaseNode visitLt(LtContext ctx) {
        // TODO Auto-generated method stub
        return super.visitLt(ctx);
    }

    @Override
    public CoolBaseNode visitLte(LteContext ctx) {
        // TODO Auto-generated method stub
        return super.visitLte(ctx);
    }

    @Override
    public CoolBaseNode visitMethodDef(MethodDefContext ctx) {
        // methodDefinition:   ID '(' paramList? ')' ':' (TYPE | SELF_TYPE) '{' expr* '};';
        // paramList:          formal* (',' formal)*;
        // formal:             ID ':' TYPE SEMICOLON?;
        CoolMethod cm = new CoolMethod();
        MethodDefinitionContext mdc = ctx.methodDefinition();
        ParamListContext plc = mdc.paramList();
        ArrayList<CoolFormal> methodParams = new ArrayList<>();

        for (FormalContext formalCon : plc.formal()) {
            CoolFormal cf = (CoolFormal) this.visitFormal(formalCon);
            methodParams.add(cf);
        }

        ArrayList<CoolExpr> cexp = new ArrayList<>();

        for (ExprContext exc : mdc.expr())  {
            if (exc instanceof AssignContext) {}
            else if (exc instanceof MethodDispatchContext) {}
            else if (exc instanceof AtMethodDispatchContext) {}
            else if (exc instanceof DotMethodDispatchContext) {}
            else if (exc instanceof IfStatementContext) {}
            else if (exc instanceof WhileStatementContext) {}
            else if (exc instanceof LetStatementContext) {}
            else if (exc instanceof CaseStatementContext) {}
            else if (exc instanceof InstantiateContext) {}
            else if (exc instanceof IsVoidContext) {}
            else if (exc instanceof CodeBlockContext) {}
            else if (exc instanceof AddContext) {}
            else if (exc instanceof SubtractContext) {}
            else if (exc instanceof MultiplyContext) {}
            else if (exc instanceof DivideContext) {}
            else if (exc instanceof ComplementContext) {}
            else if (exc instanceof LtContext) {}
            else if (exc instanceof LteContext) {}
            else if (exc instanceof GtContext) {}
            else if (exc instanceof GteContext) {}
            else if (exc instanceof IsEqualContext) {}
            else if (exc instanceof NotContext) {}
            else if (exc instanceof ParenthesisExprContext) {}
            else if (exc instanceof IdentifierContext) {}
            else if (exc instanceof IntegerContext) {}
            else if (exc instanceof StringContext) {}
            else if (exc instanceof SelfContext) {}
            else if (exc instanceof TrueContext) {}
            else if (exc instanceof FalseContext) {}
            else {}
        }

        cm.setParams(methodParams);
        cm.setName(mdc.ID().getText());

    }

    @Override
    public CoolBaseNode visitMethodDefinition(MethodDefinitionContext ctx) {
        // TODO Auto-generated method stub
        return super.visitMethodDefinition(ctx);
    }

    @Override
    public CoolBaseNode visitMethodDispatch(MethodDispatchContext ctx) {
        // TODO Auto-generated method stub
        return super.visitMethodDispatch(ctx);
    }

    @Override
    public CoolBaseNode visitMultiply(MultiplyContext ctx) {
        // TODO Auto-generated method stub
        return super.visitMultiply(ctx);
    }

    @Override
    public CoolBaseNode visitNot(NotContext ctx) {
        // TODO Auto-generated method stub
        return super.visitNot(ctx);
    }

    @Override
    public CoolBaseNode visitParamList(ParamListContext ctx) {
        // TODO Auto-generated method stub
        return super.visitParamList(ctx);
    }

    @Override
    public CoolBaseNode visitParenthesisExpr(ParenthesisExprContext ctx) {
        // TODO Auto-generated method stub
        return super.visitParenthesisExpr(ctx);
    }

    @Override
    public CoolBaseNode visitProg(ProgContext ctx) {
        // prog: CoolClass+;
        CoolProgram coolProg = new CoolProgram();

        List<CoolClassContext> classes = ctx.coolClass();
        ArrayList<CoolClass> coolClasses = new ArrayList<>();

        for (CoolClassContext c : classes) {
            CoolClass ccn = (CoolClass) this.visitCoolClass(c);
            coolClasses.add(ccn);
        }

        coolProg.setClasses(coolClasses);
        return coolProg;
    }

    @Override
    public CoolBaseNode visitSelf(SelfContext ctx) {
        // TODO Auto-generated method stub
        return super.visitSelf(ctx);
    }

    @Override
    public CoolBaseNode visitString(StringContext ctx) {
        // TODO Auto-generated method stub
        return super.visitString(ctx);
    }

    @Override
    public CoolBaseNode visitSubtract(SubtractContext ctx) {
        // TODO Auto-generated method stub
        return super.visitSubtract(ctx);
    }

    @Override
    public CoolBaseNode visitTrue(TrueContext ctx) {
        // TODO Auto-generated method stub
        return super.visitTrue(ctx);
    }

    @Override
    public CoolBaseNode visitWhileStatement(WhileStatementContext ctx) {
        // TODO Auto-generated method stub
        return super.visitWhileStatement(ctx);
    }

    @Override
    public CoolBaseNode visit(ParseTree tree) {
        // TODO Auto-generated method stub
        return super.visit(tree);
    }

    @Override
    public CoolBaseNode visitChildren(RuleNode node) {
        // TODO Auto-generated method stub
        return super.visitChildren(node);
    }

    @Override
    public CoolBaseNode visitErrorNode(ErrorNode node) {
        // TODO Auto-generated method stub
        return super.visitErrorNode(node);
    }

    @Override
    public CoolBaseNode visitTerminal(TerminalNode node) {
        // TODO Auto-generated method stub
        return super.visitTerminal(node);
    }
    
}
