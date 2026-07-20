grammar Cool;

prog:   class+;

class:   CLASS TYPE (INHERITS TYPE)* '{' feature* '};' ;

feature
    :   methodDefinition                                                # methodDef
    |   attribute                                                       # attributeDef
    ;

paramList:          formal* (',' formal)*;
formal:             ID ':' TYPE SEMICOLON?;
attribute:          ID ':' TYPE ('<-' expr)?  SEMICOLON?;
methodDefinition:   ID '(' paramList? ')' ':' (TYPE | SELF_TYPE) '{' expr* '};';

expr   
    :   ID '<-' expr ';'?                                               # assign
    |   ID '(' (expr (',' expr)*)? ')' ';'?                             # methodDispatch
    |   expr '@' TYPE '.' ID '(' (expr (',' expr)*)? ')' SEMICOLON?     # atMethodDispatch
    |   expr '.' ID '(' (expr (',' expr)*)? ')' SEMICOLON?              # dotMethodDispatch
    |   'if' expr 'then' expr 'else' expr 'fi'                          # ifStatement
    |   'while' expr 'loop' expr+ 'pool'                                # whileStatement
    |   'let' attribute (',' attribute)* 'in' expr                      # letStatement
    |   'case' expr 'of' (formal '=>' expr ';')+ 'esac'                 # caseStatement 
    |   'new' TYPE                                                      # instantiate
    |   'isVoid' TYPE                                                   # isVoid
    |   '{' expr+ '}'                                                   # codeBlock
    |   expr '+' expr                                                   # add
    |   expr '-' expr                                                   # subtract
    |   expr '*' expr                                                   # multiply
    |   expr '/' expr                                                   # divide
    |   '~' expr                                                        # complement
    |   expr '<' expr                                                   # lt
    |   expr '>' expr                                                   # gt
    |   expr '<=' expr                                                  # lte
    |   expr '>=' expr                                                  # gte
    |   expr '=' expr                                                   # isEqual
    |   'not' expr                                                      # not
    |   '(' expr ')' SEMICOLON?                                         # parenthesisExpr
    |   ID                                                              # identifier
    |   INTEGER                                                         # integer
    |   STRING                                                          # string
    |   'self' SEMICOLON?                                               # self
    |   'true' SEMICOLON?                                               # true
    |   'false' SEMICOLON?                                              # false
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

SELF:           'self';
SELF_TYPE:      'SELF_TYPE';
TRUE:           'true';
FALSE:          'false';
TYPE:           CAPITAL (CAPITAL | LOWERCA | DIGITS)+;
ID:             LOWERCA (CAPITAL | LOWERCA | DIGITS | UNDERSC)+;
INTEGER:        DIGITS+;
STRING:         ('"' | '\'') .*? ('"' | '\'');

// skippables
WHITESPACE:     [ \t\r\n\f]+    -> skip;
SEMICOLON:      ';'             -> skip;
BLOCKCOMMENT:   '(*' .*?  '*)'  -> skip;
INLINECOMMENT:  '--' ~[\r\n]*   -> skip;

fragment CAPITAL: [A-Z];
fragment LOWERCA: [a-z];
fragment DIGITS:  [0-9];
fragment UNDERSC: '_';
