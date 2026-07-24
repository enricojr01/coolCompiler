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
import com.enricojr.coollang.ast.constants.CoolInteger;
import com.enricojr.coollang.ast.constants.CoolString;
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
import com.enricojr.coollang.ast.expressions.CoolMethodDispatch;
import com.enricojr.coollang.ast.expressions.CoolParenthesisExpr;
import com.enricojr.coollang.ast.expressions.CoolUnaryOp;
import com.enricojr.coollang.ast.expressions.CoolWhile;
import com.enricojr.coollang.ast.program.CoolAttribute;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolFormal;
import com.enricojr.coollang.ast.program.CoolMethod;
import com.enricojr.coollang.ast.program.CoolProgram;

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
        String name = ctx.ID().getText(); 
        CoolExpr ce = this.visitExpression(ctx.expr());

        ca.setName(name);
        ca.setExpression(ce);

        return ca;
    }

    @Override
    public CoolBaseNode visitAtMethodDispatch(AtMethodDispatchContext ctx) {
        // expr '@' TYPE '.' ID '(' (expr (',' expr)*)? ')' SEMICOLON?
        CoolAtMethodDispatch camd = new CoolAtMethodDispatch();
        List<ExprContext> expressions = ctx.expr();
        CoolExpr lhs = this.visitExpression(expressions.get(0));
        String type = ctx.TYPE().getText();
        String functionName = ctx.ID().getText();

        ArrayList<CoolExpr> params = new ArrayList<>();

        camd.setLhs(lhs);
        camd.setIdentifier(functionName);
        camd.setType(type);
        camd.setArguemnts(params);

        return camd;
    }

    @Override
    public CoolBaseNode visitAttribute(AttributeContext ctx) {
        // TODO: consider that labelling the subrule in the grammar file may have been wrong.
        // ID ':' TYPE ('<-' expr)?  SEMICOLON?;
        CoolAttribute ca = new CoolAttribute();
        CoolExpr expr = this.visitExpression(ctx.expr());

        ca.setIdentifier(ctx.ID().getText());
        ca.setTypeName(ctx.TYPE().getText());
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
        /* 
            TODO: double check that the formal and attribute really should be separate I may have gotten the distinction between them wrong.
        */
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
            }
        }
        cc.setAttributes(cas);
        cc.setMethods(cms);

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

    public CoolExpr visitExpression(ExprContext exc) {
        CoolExpr expr = null;
        if (exc instanceof AssignContext) {
            expr = (CoolAssign) this.visitAssign((AssignContext) exc);
        } else if (exc instanceof MethodDispatchContext) {
            expr = (CoolMethodDispatch) this.visitMethodDispatch((MethodDispatchContext) exc);
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
            // TODO: change this! should be a CoolIdentiifer? 
            expr = (CoolString) this.visitIdentifier((IdentifierContext) exc);
        } else if (exc instanceof IntegerContext) {
            expr = (CoolInteger) this.visitInteger((IntegerContext) exc);
        } else if (exc instanceof StringContext) {
            expr = (CoolString) this.visitString((StringContext) exc);
        } else if (exc instanceof SelfContext) {
            expr = null;
        } else if (exc instanceof TrueContext) {
            expr = null;
        } else if (exc instanceof FalseContext) {
            expr = null;
        }
        else {
            // throw new WTFException;
        }

        return expr;
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
        }

        cm.setParams(methodParams);
        cm.setName(mdc.ID().getText());

        return null;
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
