grammar Cool;

prog:   class+;

class:   CLASS TYPE (INHERITS TYPE)* '{' feature* '};' ;

feature
    :   methodDefinition                                                    # methodDef
    |   attribute                                                           # attributeDef
    ;

paramList:          formal* (',' formal)*;
formal:             ID ':' TYPE SEMICOLON?;
attribute:          ID ':' TYPE ('<-' expr)?  SEMICOLON?;
methodDefinition:   ID '(' paramList? ')' ':' (TYPE | SELF_TYPE) '{' expr* '};';

expr   
    :   ID LARROW expr SEMICOLON?                                           # assign
    |   ID LPAREN (expr (',' expr)*)? RPAREN SEMICOLON?                     # methodDispatch
    |   expr '@' TYPE '.' ID LPAREN (expr (',' expr)*)? RPAREN SEMICOLON?   # atMethodDispatch
    |   expr '.' ID LPAREN (expr (',' expr)*)? RPAREN SEMICOLON?            # dotMethodDispatch
    |   IF expr THEN expr ELSE expr FI                                      # ifStatement
    |   WHILE expr LOOP expr+ POOL                                          # whileStatement
    |   LET attribute (',' attribute)* IN expr                              # letStatement
    |   CASE expr OF (formal DARROW expr SEMICOLON)+ ESAC                   # caseStatement 
    |   NEW TYPE                                                            # instantiate
    |   ISVOID TYPE                                                         # isVoid
    |   LCURLY expr+ RCURLY                                                 # codeBlock
    |   expr PLUS expr                                                      # add
    |   expr MINUS expr                                                     # subtract
    |   expr MULT expr                                                      # multiply
    |   expr DIV expr                                                       # divide
    |   COMPLE expr                                                         # complement
    |   expr LT expr                                                        # lt
    |   expr GT expr                                                        # gt
    |   expr LTE expr                                                       # lte
    |   expr GTE expr                                                       # gte
    |   expr EQUALS expr                                                    # isEqual
    |   'not' expr                                                          # not
    |   LPAREN expr RPAREN SEMICOLON?                                       # parenthesisExpr
    |   ID                                                                  # identifier
    |   INTEGER                                                             # integer
    |   STRING                                                              # string
    |   'self' SEMICOLON?                                                   # self
    |   'true' SEMICOLON?                                                   # true
    |   'false' SEMICOLON?                                                  # false
    ;

CLASS:      [Cc][Ll][Aa][Ss][Ss];
ELSE:       [Ee][Ll][Ss][Ee];
FI:         [Ff][Ii];
IF:         [Ii][Ff];
IN:         [Ii][Nn];
INHERITS:   [Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss];
ISVOID:     [Ii][Ss][Vv][Oo][Ii][Dd];
LET:        [Ll][Ee][Tt];
LOOP:       [Ll][Oo][Oo][Pp];
POOL:       [Pp][Oo][Oo][Ll];
THEN:       [Tt][Hh][Ee][Nn];
WHILE:      [Ww][Hh][Ii][Ll][Ee];
CASE:       [Cc][Aa][Ss][Ee];
ESAC:       [Ee][Ss][Aa][Cc];
NEW:        [Nn][Ee][Ww];
OF:         [Oo][Ff];
NOT:        [Nn][Oo][Tt];

PLUS:   '+';
MINUS:  '-';
MULT:   '*';
DIV:    '/';
LT:     '<';
LTE:    '<=';
GT:     '>';
GTE:    '>=';
EQUALS: '=';
COMPLE: '~';
LARROW: '<-';
DARROW: '=>';

SELF:           'self';
SELF_TYPE:      'SELF_TYPE';
TRUE:           'true';
FALSE:          'false';
TYPE:           CAPITAL (CAPITAL | LOWERCA | DIGITS)*;
ID:             LOWERCA (CAPITAL | LOWERCA | DIGITS | UNDERSC)*;
INTEGER:        DIGITS+;
STRING:         ('"' | '\'') .*? ('"' | '\'');

// skippables
WHITESPACE:     [ \t\r\n\f]+    -> skip;
SEMICOLON:      ';'             -> skip;
BLOCKCOMMENT:   '(*' .*?  '*)'  -> skip;
INLINECOMMENT:  '--' ~[\r\n]*   -> skip;
// should I even be skipping these?
LPAREN:         '('             -> skip;
RPAREN:         ')'             -> skip;
LCURLY:         '{'             -> skip;
RCURLY:         '}'             -> skip;

fragment CAPITAL: [A-Z];
fragment LOWERCA: [a-z];
fragment DIGITS:  [0-9];
fragment UNDERSC: '_';
