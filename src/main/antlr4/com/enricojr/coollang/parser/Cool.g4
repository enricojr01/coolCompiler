grammar Cool;

prog:   coolClass+;

coolClass:   CLASS TYPE (INHERITS TYPE)? '{' feature* '}'';';

feature
    :   attribute                                                           # attributeDef
    |   methodDefinition                                                    # methodDef
    ;

paramList:          formal* (',' formal)*;
formal:             ID ':' TYPE ';'?;
attribute:          ID ':' TYPE ('<-' expr)?  ';'?;
methodDefinition:   ID '(' paramList? ')' ':' (TYPE | SELF_TYPE) '{' expr* '}'';';

expr   
    :   ID '(' (expr (',' expr)*)? ')'                           # methodDispatch
    |   expr '.' ID '(' (expr (',' expr)*)? ')'                # dotMethodDispatch
    |   expr '@' TYPE '.' ID '(' (expr (',' expr)*)? ')'         # atMethodDispatch
    |   IF expr THEN expr ELSE expr FI                           # ifStatement
    |   WHILE expr LOOP expr+ POOL                               # whileStatement
    |   LET attribute (',' attribute)* IN expr                   # letStatement
    |   CASE expr OF (formal DARROW expr (';'))+ ESAC              # caseStatement 
    |   NEW TYPE                                                 # instantiate
    |   ISVOID (TYPE | SELF_TYPE | SELF_KW)                      # isVoid
    |   '{' (expr ';')+ '}'                             # codeBlock
    |   '(' expr ')'                                             # parenthesisExpr
    |   expr MULT expr                                           # multiply
    |   expr DIV expr                                            # divide
    |   expr PLUS expr                                           # add
    |   expr MINUS expr                                          # subtract
    |   COMPLE expr                                              # complement
    |   expr LT expr                                             # lt
    |   expr LTE expr                                            # lte
    |   expr EQUALS expr                                         # isEqual
    |   expr GT expr                                             # gt
    |   expr GTE expr                                            # gte
    |   NOT expr                                                 # not
    |   ID LARROW expr                                           # assign
    |   ID                                                       # identifier
    |   INTEGER                                                  # integer
    |   STRING                                                   # string
    |   SELF_KW                                                  # self
    |   'true'                                                   # true
    |   'false'                                                  # false
    ;

CLASS:          [Cc][Ll][Aa][Ss][Ss];
ELSE:           [Ee][Ll][Ss][Ee];
FI:             [Ff][Ii];
IF:             [Ii][Ff];
IN:             [Ii][Nn];
INHERITS:       [Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss];
ISVOID:         [Ii][Ss][Vv][Oo][Ii][Dd];
LET:            [Ll][Ee][Tt];
LOOP:           [Ll][Oo][Oo][Pp];
POOL:           [Pp][Oo][Oo][Ll];
THEN:           [Tt][Hh][Ee][Nn];
WHILE:          [Ww][Hh][Ii][Ll][Ee];
CASE:           [Cc][Aa][Ss][Ee];
ESAC:           [Ee][Ss][Aa][Cc];
NEW:            [Nn][Ee][Ww];
OF:             [Oo][Ff];
NOT:            [Nn][Oo][Tt];
SELF_KW:        [Ss][Ee][Ll][Ff];

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

SELF_TYPE:      'SELF_TYPE';
TRUE:           'true';
FALSE:          'false';
TYPE:           CAPITAL (CAPITAL | LOWERCA | DIGITS)*;
ID:             LOWERCA (CAPITAL | LOWERCA | DIGITS | UNDERSC)*;
INTEGER:        DIGITS+;
STRING:         '"' ('\\"' | .)*? '"';

// skippables
// NOTE TO SELF: You should NOT skip paren / curly brackets / square brackets.
WHITESPACE:     [ \t\r\n\f]+        -> skip;
BLOCKCOMMENT:   '(*' .*?  '*)'      -> skip;
INLINECOMMENT:  '--' .*? NEWLINE    -> skip;

fragment CAPITAL: [A-Z];
fragment LOWERCA: [a-z];
fragment DIGITS:  [0-9];
fragment UNDERSC: '_';
fragment NEWLINE: '\r'? '\n';
fragment SEMICOLON: ';';
