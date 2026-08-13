package com.enricojr.coollang.ast;

// TODO: are there like aliases or something I can use to cut this down to size?
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
import com.enricojr.coollang.CoolParser.CaseBranchContext;
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
import com.enricojr.coollang.ast.constants.CoolBool;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.constants.CoolInteger;
import com.enricojr.coollang.ast.constants.CoolSelf;
import com.enricojr.coollang.ast.constants.CoolString;
import com.enricojr.coollang.ast.constants.CoolType;
import com.enricojr.coollang.ast.expressions.CoolAssign;
import com.enricojr.coollang.ast.expressions.CoolAtMethodDispatch;
import com.enricojr.coollang.ast.expressions.CoolBinaryOp;
import com.enricojr.coollang.ast.expressions.CoolBlock;
import com.enricojr.coollang.ast.expressions.CoolCase;
import com.enricojr.coollang.ast.expressions.CoolDotMethodDispatch;
import com.enricojr.coollang.ast.expressions.CoolExpr;
import com.enricojr.coollang.ast.expressions.CoolIf;
import com.enricojr.coollang.ast.expressions.CoolInstantiate;
import com.enricojr.coollang.ast.expressions.CoolIsVoid;
import com.enricojr.coollang.ast.expressions.CoolLet;
import com.enricojr.coollang.ast.expressions.CoolParenthesisExpr;
import com.enricojr.coollang.ast.expressions.CoolUnaryOp;
import com.enricojr.coollang.ast.expressions.CoolWhile;
import com.enricojr.coollang.ast.program.CoolAttribute;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolFormal;
import com.enricojr.coollang.ast.program.CoolMethod;
import com.enricojr.coollang.ast.program.CoolParamList;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.semantic.exceptions.InvalidCoolClassNameException;

public class AstBuilder extends CoolBaseVisitor<CoolBaseNode> {

    @Override
    public CoolBaseNode visitAdd(AddContext ctx) {
        // expr '+' expr
        CoolBinaryOp add = new CoolBinaryOp();
        List<ExprContext> lhsrhs = ctx.expr();
        CoolExpr lhs = this.visitExpression(lhsrhs.get(0));
        CoolExpr rhs = this.visitExpression(lhsrhs.get(1));

        add.setOp(CoolBinaryOp.OPERATOR.ADD);
        add.setRhs(rhs);
        add.setLhs(lhs);

        return add;
    }

    @Override
    public CoolBaseNode visitAssign(AssignContext ctx) {
        // ID LARROW expr SEMICOLON?
        CoolAssign ca = new CoolAssign();
        CoolIdentifier id = new CoolIdentifier(ctx.ID().getText());
        CoolExpr ce = this.visitExpression(ctx.expr());

        ca.setName(id);
        ca.setExpression(ce);

        return ca;
    }

    @Override
    public CoolBaseNode visitAtMethodDispatch(AtMethodDispatchContext ctx) {
        // expr '@' TYPE '.' ID '(' (expr (',' expr)*)? ')' SEMICOLON?
        CoolAtMethodDispatch camd = new CoolAtMethodDispatch();
        List<ExprContext> expressions = ctx.expr();
        CoolExpr lhs = this.visitExpression(expressions.get(0));
        CoolType type = new CoolType(ctx.TYPE().getText());
        CoolIdentifier methodName = new CoolIdentifier(ctx.ID().getText());

        ArrayList<CoolExpr> params = new ArrayList<>();

        camd.setLhs(lhs);
        camd.setIdentifier(methodName);
        camd.setType(type);
        camd.setArguments(params);

        return camd;
    }

    @Override
    public CoolBaseNode visitAttribute(AttributeContext ctx) {
        // TODO: consider that labelling the subrule in the grammar file may have been wrong.
        // ID ':' TYPE ('<-' expr)?  SEMICOLON?;
        CoolAttribute ca = new CoolAttribute();
        CoolExpr expr = this.visitExpression(ctx.expr());

        CoolIdentifier id = new CoolIdentifier(ctx.ID().getText());
        CoolIdentifier type = new CoolIdentifier(ctx.TYPE().getText());

        ca.setIdentifier(id);
        ca.setTypeName(type);
        // Should I have to check null here? Did I make a mistake designing this?
        if (expr != null) {
            ca.setValue(expr);
        }

        return ca;
    }

    @Override
    public CoolBaseNode visitAttributeDef(AttributeDefContext ctx) {
        // ID ':' TYPE ('<-' expr)?  SEMICOLON?;
        AttributeContext ac = ctx.attribute();
        return this.visitAttribute(ac);
    }

    @Override
    public CoolBaseNode visitCaseStatement(CaseStatementContext ctx) {
        // CASE expr OF (formal DARROW expr SEMICOLON)+ ESAC
        CoolCase cc = new CoolCase();
        List<CaseBranchContext> expressions = ctx.caseBranch();
        ExprContext predicate = ctx.expr();

        ArrayList<CoolCase.Branch> branches = new ArrayList<>();
        for (CaseBranchContext cbc : expressions) {
            CoolFormal formal = (CoolFormal) this.visitFormal(cbc.formal());
            CoolExpr expression = this.visitExpression(cbc.expr());
            CoolCase.Branch b = cc.createBranch(formal, expression);
            branches.add(b);
        }

        cc.setBranches(branches);
        cc.setPredicate(this.visitExpression(predicate));

        return cc;
    }

    @Override
    public CoolBaseNode visitCodeBlock(CodeBlockContext ctx) {
        CoolBlock cb = new CoolBlock();
        List<ExprContext> contexts = ctx.expr();
        ArrayList<CoolExpr> expressions = new ArrayList<>();
        for (ExprContext e : contexts) {
            CoolExpr expression = this.visitExpression(e);
            expressions.add(expression);
        }

        cb.setBody(expressions);

        return cb;
    }

    @Override
    public CoolBaseNode visitComplement(ComplementContext ctx) {
        CoolUnaryOp cuo = new CoolUnaryOp();
        CoolExpr expression = this.visitExpression(ctx.expr());

        cuo.setOp(CoolUnaryOp.OPERATOR.COMPLEMENT);
        cuo.setExpression(expression);

        return cuo;
    }

    @Override
    public CoolBaseNode visitCoolClass(CoolClassContext ctx) {
        // coolClass:   CLASS TYPE (INHERITS TYPE)? '{' feature* '};';
        CoolClass cc = new CoolClass();
        CoolIdentifier className = new CoolIdentifier(ctx.TYPE(0).getText());
        CoolIdentifier parentName = null;

        if (ctx.TYPE(1) != null) {
            parentName = new CoolIdentifier(ctx.TYPE(1).getText());
        }

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
            }
        }

        cc.setName(className);
        if (parentName != null) {
            cc.setParentName(parentName);
        }
        cc.setAttributes(cas);
        cc.setMethods(cms);

        return cc;
    }

    @Override
    public CoolBaseNode visitDivide(DivideContext ctx) {
        CoolBinaryOp cbo = new CoolBinaryOp();
        List<ExprContext> expressions = ctx.expr();
        CoolExpr lhs = this.visitExpression(expressions.get(0));
        CoolExpr rhs = this.visitExpression(expressions.get(1));

        cbo.setLhs(lhs);
        cbo.setOp(CoolBinaryOp.OPERATOR.DIV);
        cbo.setRhs(rhs);

        return cbo;
    }

    @Override
    public CoolBaseNode visitDotMethodDispatch(DotMethodDispatchContext ctx) {
        List<ExprContext> expressions = ctx.expr();
        List<ExprContext> paramListContext = null;

        // this first check is probably not needed I just like to be thorough.
        if (expressions.size() == 1) {
            paramListContext = null;
        } else if (expressions.size() == 2) {
            paramListContext = new ArrayList<>();
            paramListContext.add(expressions.get(1));
        } else if (expressions.size() > 2) {
            paramListContext = expressions.subList(1, expressions.size() - 1);
        }

        ExprContext lhsContext = expressions.get(0);
        CoolIdentifier name = new CoolIdentifier(ctx.ID().getText());
        ArrayList<CoolExpr> arguments = new ArrayList<>();
        CoolExpr lhs = this.visitExpression(lhsContext);

        if (paramListContext != null) {
            for (ExprContext ec : paramListContext) {
                CoolExpr ex = this.visitExpression(ec);
                arguments.add(ex);
            }
        }

        CoolDotMethodDispatch cdmd = new CoolDotMethodDispatch();
        cdmd.setName(name);
        cdmd.setLhs(lhs);
        cdmd.setArguments(arguments);

        return cdmd;
    }

    public CoolExpr visitExpression(ExprContext exc) {
        CoolExpr expr = null;
        if (exc instanceof AssignContext) {
            expr = (CoolAssign) this.visitAssign((AssignContext) exc);
        } else if (exc instanceof MethodDispatchContext) {
            expr = (CoolDotMethodDispatch) this.visitMethodDispatch((MethodDispatchContext) exc);
        } else if (exc instanceof AtMethodDispatchContext) {
            expr = (CoolAtMethodDispatch) this.visitAtMethodDispatch((AtMethodDispatchContext) exc);
        } else if (exc instanceof DotMethodDispatchContext) {
            expr = (
                (CoolDotMethodDispatch) 
                this.visitDotMethodDispatch((DotMethodDispatchContext) exc)
            );
        } else if (exc instanceof IfStatementContext) {
            expr = (CoolIf) this.visitIfStatement((IfStatementContext) exc);
        } else if (exc instanceof WhileStatementContext) {
            expr = (CoolWhile) this.visitWhileStatement((WhileStatementContext) exc);
        } else if (exc instanceof LetStatementContext) {
            expr = (CoolLet) this.visitLetStatement((LetStatementContext) exc);
        } else if (exc instanceof CaseStatementContext) {
            expr = (CoolCase) this.visitCaseStatement((CaseStatementContext) exc);
        } else if (exc instanceof InstantiateContext) {
            expr = (CoolInstantiate) this.visitInstantiate((InstantiateContext) exc);
        } else if (exc instanceof IsVoidContext) {
            expr = (CoolIsVoid) this.visitIsVoid((IsVoidContext) exc);
        } else if (exc instanceof CodeBlockContext) {
            expr = (CoolBlock) this.visitCodeBlock((CodeBlockContext) exc);
        } else if (exc instanceof AddContext) {
            expr = (CoolBinaryOp) this.visitAdd((AddContext) exc);
        } else if (exc instanceof SubtractContext) {
            expr = (CoolBinaryOp) this.visitSubtract((SubtractContext) exc);
        } else if (exc instanceof MultiplyContext) {
            expr = (CoolBinaryOp) this.visitMultiply((MultiplyContext) exc);
        } else if (exc instanceof DivideContext) {
            expr = (CoolBinaryOp) this.visitDivide((DivideContext) exc);
        } else if (exc instanceof ComplementContext) {
            expr = (CoolUnaryOp) this.visitComplement((ComplementContext) exc);
        } else if (exc instanceof LtContext) {
            expr = (CoolBinaryOp) this.visitLt((LtContext) exc); 
        } else if (exc instanceof LteContext) {
            expr = (CoolBinaryOp) this.visitLte((LteContext) exc);
        } else if (exc instanceof GtContext) {
            expr = (CoolBinaryOp) this.visitGt((GtContext) exc);
        } else if (exc instanceof GteContext) {
            expr = (CoolBinaryOp) this.visitGte((GteContext) exc);
        } else if (exc instanceof IsEqualContext) {
            expr = (CoolBinaryOp) this.visitIsEqual((IsEqualContext) exc);
        } else if (exc instanceof NotContext) {
            expr = (CoolBinaryOp) this.visitNot((NotContext) exc);
        } else if (exc instanceof ParenthesisExprContext) {
            expr = (CoolParenthesisExpr) this.visitParenthesisExpr((ParenthesisExprContext) exc) ;
        } else if (exc instanceof IdentifierContext) {
            expr = (CoolIdentifier) this.visitIdentifier((IdentifierContext) exc);
        } else if (exc instanceof IntegerContext) {
            expr = (CoolInteger) this.visitInteger((IntegerContext) exc);
        } else if (exc instanceof StringContext) {
            expr = (CoolString) this.visitString((StringContext) exc);
        } else if (exc instanceof SelfContext) {
            expr = (CoolSelf) this.visitSelf((SelfContext) exc) ;
        } else if (exc instanceof TrueContext) {
            expr = new CoolBool(true);
        } else if (exc instanceof FalseContext) {
            expr = new CoolBool(false);
        }
        else {
            // throw new WTFException;
        }

        return expr;
    }

    @Override
    public CoolBaseNode visitFalse(FalseContext ctx) {
        return new CoolBool(false);
    }

    @Override
    public CoolBaseNode visitFormal(FormalContext ctx) {
        CoolFormal cf = new CoolFormal();
        CoolIdentifier name = new CoolIdentifier(ctx.ID().getText());
        CoolIdentifier type = new CoolIdentifier(ctx.TYPE().getText());
        cf.setName(name);
        cf.setType(type);

        return cf;
    }

    @Override
    public CoolBaseNode visitGt(GtContext ctx) {
        CoolBinaryOp cbo = new CoolBinaryOp();
        CoolExpr lhs = this.visitExpression(ctx.expr(0));
        CoolExpr rhs = this.visitExpression(ctx.expr(1));

        cbo.setLhs(lhs);
        cbo.setRhs(rhs);
        cbo.setOp(CoolBinaryOp.OPERATOR.GT);

        return cbo;
    }

    @Override
    public CoolBaseNode visitGte(GteContext ctx) {
        CoolBinaryOp cbo = new CoolBinaryOp();
        CoolExpr lhs = this.visitExpression(ctx.expr(0));
        CoolExpr rhs = this.visitExpression(ctx.expr(1));

        cbo.setLhs(lhs);
        cbo.setRhs(rhs);
        cbo.setOp(CoolBinaryOp.OPERATOR.GTE);

        return cbo;
    }

    @Override
    public CoolBaseNode visitIdentifier(IdentifierContext ctx) {
        // TODO: Go back and rewrite all instances of ctx.ID() to call this instead
        CoolIdentifier ci = new CoolIdentifier(ctx.getText());
        return ci;
    }

    @Override
    public CoolBaseNode visitIfStatement(IfStatementContext ctx) {
        CoolIf cif = new CoolIf();

        CoolExpr predicate = this.visitExpression(ctx.expr(0));
        CoolExpr thenExpr = this.visitExpression(ctx.expr(1));
        CoolExpr elseExpr = this.visitExpression(ctx.expr(2));

        cif.setPredicate(predicate);
        cif.setThenExpr(thenExpr);
        cif.setElseExpr(elseExpr);

        return cif;
    }

    @Override
    public CoolBaseNode visitInstantiate(InstantiateContext ctx) {
        CoolInstantiate cn = new CoolInstantiate();
        cn.setIdentifier(new CoolIdentifier(ctx.getText()));

        return cn;
    }

    @Override
    public CoolBaseNode visitInteger(IntegerContext ctx) {
        return new CoolInteger(Integer.parseInt(ctx.INTEGER().getText()));
    }

    @Override
    public CoolBaseNode visitIsEqual(IsEqualContext ctx) {
        CoolBinaryOp cbo = new CoolBinaryOp();
        CoolExpr lhs = this.visitExpression(ctx.expr(0));
        CoolExpr rhs = this.visitExpression(ctx.expr(1));

        cbo.setLhs(lhs);
        cbo.setOp(CoolBinaryOp.OPERATOR.EQ);
        cbo.setRhs(rhs);

        return cbo;
    }

    @Override
    public CoolBaseNode visitIsVoid(IsVoidContext ctx) {
        CoolIsVoid civ = new CoolIsVoid();
        civ.setType(new CoolIdentifier(ctx.TYPE().getText()));

        return civ;
    }

    @Override
    public CoolBaseNode visitLetStatement(LetStatementContext ctx) {
        CoolLet cl = new CoolLet();
        List<AttributeContext> attribs = ctx.attribute();
        ExprContext ec = ctx.expr();

        ArrayList<CoolAttribute> attributes = new ArrayList<>();
        for (AttributeContext ac : attribs) {
            CoolAttribute ca = (CoolAttribute) this.visitAttribute(ac);
            attributes.add(ca);
        }
        CoolExpr ce = this.visitExpression(ec);

        cl.setAttributes(attributes);
        cl.setExpression(ce);

        return cl;
    }

    @Override
    public CoolBaseNode visitLt(LtContext ctx) {
        CoolBinaryOp cbo = new CoolBinaryOp();
        CoolExpr rhs = this.visitExpression(ctx.expr(0));
        CoolExpr lhs = this.visitExpression(ctx.expr(1));

        cbo.setRhs(rhs);
        cbo.setOp(CoolBinaryOp.OPERATOR.LT);
        cbo.setLhs(lhs);

        return cbo;
    }

    @Override
    public CoolBaseNode visitLte(LteContext ctx) {
        CoolBinaryOp cbo = new CoolBinaryOp();
        CoolExpr rhs = this.visitExpression(ctx.expr(0));
        CoolExpr lhs = this.visitExpression(ctx.expr(1));

        cbo.setRhs(rhs);
        cbo.setOp(CoolBinaryOp.OPERATOR.LTE);
        cbo.setLhs(lhs);

        return cbo;
    }

    @Override
    public CoolBaseNode visitMethodDef(MethodDefContext ctx) {
        // methodDefinition:   ID '(' paramList? ')' ':' (TYPE | SELF_TYPE) '{' expr* '};';
        // paramList:          formal* (',' formal)*;
        // formal:             ID ':' TYPE SEMICOLON?;
        return this.visitMethodDefinition(ctx.methodDefinition());
    }

    @Override
    public CoolBaseNode visitMethodDefinition(MethodDefinitionContext ctx) {
        CoolMethod cm = new CoolMethod();
        ParamListContext plc = ctx.paramList();
        CoolParamList cpl = (CoolParamList) this.visitParamList(plc);
        CoolIdentifier name = new CoolIdentifier(ctx.ID().getText());
        CoolIdentifier returnType = null;

        // NOTE: it's either SELF_TYPE or TYPE never both.
        // NOTE: maybe consider not using exceptions as flow control like this
        try {
            ctx.SELF_TYPE().getText();
            returnType = new CoolSelf();
        } catch (NullPointerException e) {
            returnType = new CoolIdentifier(ctx.TYPE().getText());
        }

        ArrayList<CoolExpr> expressions = new ArrayList<>();
        for (ExprContext exc : ctx.expr())  {
            CoolExpr ce = this.visitExpression(exc);
            expressions.add(ce);
        }

        cm.setParameters(cpl);
        cm.setReturnType(returnType);
        cm.setName(name);
        cm.setExpressions(expressions);

        return cm;
    }

    @Override
    public CoolBaseNode visitMethodDispatch(MethodDispatchContext ctx) {
        // ID '(' (expr (',' expr)*)? ')' SEMICOLON?
        CoolDotMethodDispatch cmd = new CoolDotMethodDispatch();
        CoolIdentifier cid = new CoolIdentifier(ctx.ID().getText());
        List<ExprContext> argumentContexts = ctx.expr();
        ArrayList<CoolExpr> argumentList = new ArrayList<>();

        for (ExprContext ec : argumentContexts) {
            CoolExpr ce = this.visitExpression(ec);
            argumentList.add(ce);
        }

        cmd.setLhs(new CoolSelf());
        cmd.setName(cid);
        cmd.setArguments(argumentList);

        return cmd;
    }

    @Override
    public CoolBaseNode visitMultiply(MultiplyContext ctx) {
        CoolBinaryOp cbo = new CoolBinaryOp();
        CoolExpr rhs = this.visitExpression(ctx.expr(0));
        CoolExpr lhs = this.visitExpression(ctx.expr(1));
        cbo.setRhs(rhs);
        cbo.setOp(CoolBinaryOp.OPERATOR.MUL);
        cbo.setLhs(lhs);

        return cbo;
    }

    @Override
    public CoolBaseNode visitNot(NotContext ctx) {
        CoolUnaryOp cuo = new CoolUnaryOp();
        CoolExpr expression = this.visitExpression(ctx.expr());

        cuo.setOp(CoolUnaryOp.OPERATOR.NOT);
        cuo.setExpression(expression);

        return expression;
    }

    @Override
    public CoolBaseNode visitParamList(ParamListContext ctx) {
        CoolParamList cpl = new CoolParamList();
        List<FormalContext> formalContexts = ctx.formal();

        ArrayList<CoolFormal> parameters = new ArrayList<>();
        for (FormalContext fc : formalContexts) {
            CoolFormal formal = (CoolFormal) this.visitFormal(fc);
            parameters.add(formal);
        }

        cpl.setParameters(parameters);

        return cpl;
    }

    @Override
    public CoolBaseNode visitParenthesisExpr(ParenthesisExprContext ctx) {
        CoolParenthesisExpr cpe = new CoolParenthesisExpr();
        CoolExpr expression = this.visitExpression(ctx.expr());

        cpe.setExpression(expression);

        return cpe;
    }

    @Override
    public CoolBaseNode visitProg(ProgContext ctx) {
        // prog: CoolClass+;
        System.out.println("Visiting prog");
        CoolProgram coolProg = new CoolProgram();

        List<CoolClassContext> classes = ctx.coolClass();
        ArrayList<CoolClass> coolClasses = new ArrayList<>();

        for (CoolClassContext c : classes) {
            CoolClass ccn = (CoolClass) this.visitCoolClass(c);
            coolClasses.add(ccn);
        }

        // coolProg.setChildren(coolClasses);
        coolProg.setClasses(coolClasses);
        return coolProg;
    }

    @Override
    public CoolBaseNode visitSelf(SelfContext ctx) {
        CoolSelf cs = new CoolSelf();
        return cs;
    }

    @Override
    public CoolBaseNode visitString(StringContext ctx) {
        CoolString cs = new CoolString();
        cs.setValue(ctx.getText());
        return cs;
    }

    @Override
    public CoolBaseNode visitSubtract(SubtractContext ctx) {
        CoolBinaryOp cbo = new CoolBinaryOp();
        CoolExpr rhs = this.visitExpression(ctx.expr(0));
        CoolExpr lhs = this.visitExpression(ctx.expr(1));

        cbo.setOp(CoolBinaryOp.OPERATOR.SUB);
        cbo.setLhs(lhs);
        cbo.setRhs(rhs);

        return cbo;
    }

    @Override
    public CoolBaseNode visitTrue(TrueContext ctx) {
        CoolBool cb = new CoolBool();
        cb.setValue(true);
        return cb;
    }

    @Override
    public CoolBaseNode visitWhileStatement(WhileStatementContext ctx) {
        CoolWhile cw = new CoolWhile();
        CoolExpr predicate = this.visitExpression(ctx.expr(0));
        CoolExpr body = this.visitExpression(ctx.expr(1));
        cw.setPredicate(predicate);
        cw.setBody(body);

        return cw;
    }

    @Override
    public CoolBaseNode visit(ParseTree tree) {
        throw new RuntimeException("This function not implemented!");
    }

    @Override
    public CoolBaseNode visitChildren(RuleNode node) {
        throw new RuntimeException("This function not implemented!");
    }

    @Override
    public CoolBaseNode visitErrorNode(ErrorNode node) {
        throw new RuntimeException("This function not implemented!");
    }

    @Override
    public CoolBaseNode visitTerminal(TerminalNode node) {
        throw new RuntimeException("This function not implemented!");
    }
}
