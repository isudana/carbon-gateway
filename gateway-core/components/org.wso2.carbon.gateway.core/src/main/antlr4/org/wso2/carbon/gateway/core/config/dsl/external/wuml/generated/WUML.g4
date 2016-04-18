/*
 * Copyright (c) 2005-2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

grammar WUML;

/* --- PARSER RULES --- */

// Definition of the script

script
    : ( handler | NEWLINE )* EOF;

/* Definition of the handler which contains the comments, @startuml,
statements, @enduml */

handler
    : (commentStatement NEWLINE+)?
        STARTUMLX NEWLINE+
          statementList
      ENDUMLX ;

// Definition of the statement list in the script
statementList
    : ( statement NEWLINE+ )* ;

// Definition of different types of statements
statement
    : titleStatement
    | participantStatement
    | mediatorStatement
    | routingStatement
    | parallelStatement
    | ifStatement
    | loopStatement
    | groupStatement
    | refStatement
    | commentStatement
    ;

// Definition of the high level name for this message flow
titleStatement
    : IDENTIFIER WS+ COLON WS+ INTEGRATIONFLOWX;

/* Definition of the participating components which represents a
lifeline in the visual representation */
participantStatement
    : integrationFlowDefStatement
    | inboundEndpointDefStatement
    | pipelineDefStatement
    | outboundEndpointDefStatement
    ;

// Definition for a IntegrationFlow
integrationFlowDefStatement
    : PARTICIPANT WS+ IDENTIFIER WS+ COLON WS+ integrationFlowDef;

// Definition for a Inbound Endpoint
inboundEndpointDefStatement
    : PARTICIPANT WS+ IDENTIFIER WS+ COLON WS+ inboundEndpointDef;

// Definition for a pipeline
pipelineDefStatement
    : PARTICIPANT WS+ IDENTIFIER WS+ COLON WS+ pipelineDef;

// Definition for an Outbound Endpoint
outboundEndpointDefStatement
    : PARTICIPANT WS+ IDENTIFIER WS+ COLON WS+ outboundEndpointDef;

// Definition of a mediator statement
mediatorStatement : mediatorDef;

// Integration Flow constructor statement
integrationFlowDef: INTEGRATIONFLOWX LPAREN STRINGX RPAREN;

// Inbound Endpoint constructor statement
//inboundEndpointDef: INBOUNDENDPOINTX LPAREN PROTOCOLDEF COMMA_SYMBOL PORTDEF
//                    COMMA_SYMBOL CONTEXTDEF RPAREN;
inboundEndpointDef: INBOUNDENDPOINTX LPAREN PROTOCOLDEF PARAMX* RPAREN;

// Pipeline constructor statement
pipelineDef: PIPELINEX LPAREN COMMENTSTRINGX RPAREN;

// Outbound Endpoint constructor statement
outboundEndpointDef: OUTBOUNDENDPOINTX LPAREN PROTOCOLDEF PARAMX* RPAREN;

// Mediator constructor statement
mediatorDef: IDENTIFIER ARGUMENTLISTDEF WS+ COLON WS+ MEDIATORX;


routingStatement
    : invokeFromSource
    | invokeFromTarget
    | invokeToSource
    | invokeToTarget
    ;

invokeFromSource: IDENTIFIER WS+ ARROW1X WS+ IDENTIFIER WS+
                  COMMENTX WS+ COMMENTSTRINGX;
invokeToTarget: IDENTIFIER WS+ ARROW2X WS+ IDENTIFIER WS+
                COMMENTX WS+ COMMENTSTRINGX;
invokeFromTarget: IDENTIFIER WS+ ARROW3X WS+ IDENTIFIER WS+
                  COMMENTX WS+ COMMENTSTRINGX;
invokeToSource: IDENTIFIER WS+ ARROW4X WS+ IDENTIFIER WS+
                COMMENTX WS+ COMMENTSTRINGX;


// Message routing statement
/*
routingStatement
    : genericRoutingStatement;

genericRoutingStatement: IDENTIFIER WS+ ARROW WS+ IDENTIFIER WS+ COMMENTX
                         WS+ STRINGX;
*/

// Definition of 'par' statement for parallel execution
parallelStatement
    : PAR NEWLINE?
      NEWLINE parMultiThenBlock
      END
    ;

parMultiThenBlock
    : statementList NEWLINE (parElseBlock)? ;


parElseBlock
    : (ELSE NEWLINE statementList)+ ;

// Definition of 'alt' statement for if condition
ifStatement
    : ALT WS WITH WS conditionStatement NEWLINE
      NEWLINE? ifMultiThenBlock
      END
    ;

conditionStatement
    : conditionDef;

conditionDef: CONDITIONX LPAREN SOURCEDEF PARAMX* RPAREN;

ifMultiThenBlock
    : statementList NEWLINE (ifElseBlock)? ;


ifElseBlock
    : (ELSE NEWLINE statementList)+ ;

// Definition of group statement
groupStatement
    : GROUP WS IDENTIFIER NEWLINE
      NEWLINE? statementList
      END
    ;

// Definition of loop statement
loopStatement
    : LOOP WS expression NEWLINE
      NEWLINE? statementList
      END
    ;
// Definition of reference statement
refStatement
    : REF WS IDENTIFIER NEWLINE?;


// Definition of internal comment statement
commentStatement
    : COMMENTST
    | HASHCOMMENTST
    | DOUBLESLASHCOMMENTST;


expression
    : EXPRESSIONX;


/* --- LEXER rules --- */

/* LEXER: keyword rules */

COMMENTST
    :  '/*' .*? '*/'
    ;

HASHCOMMENTST
    : '#' COMMENTPARAMS
    ;

DOUBLESLASHCOMMENTST
    : '//' COMMENTPARAMS
    ;

//ROUTINGSTATEMENTX: ROUTINGSTATEMENT;

SOURCEDEF: SOURCE LPAREN CONFIGPARAMS RPAREN;

//PATTERNDEF: PATTERN LPAREN STRINGX RPAREN;

PROTOCOLDEF: PROTOCOL LPAREN STRINGX RPAREN;

//PORTDEF: PORT LPAREN NUMBER RPAREN;
PARAMX: COMMA_SYMBOL IDENTIFIER LPAREN DOUBLEQUOTES ANY_STRING DOUBLEQUOTES RPAREN;

CONTEXTDEF: CONTEXT LPAREN URLSTRINGX RPAREN;

HOSTDEF: HOST LPAREN URLSTRINGX RPAREN;

CONFIGSDEF: CONFIGS LPAREN (CONFIGPARAMS COMMA_SYMBOL)* (CONFIGPARAMS)* RPAREN;

ARGUMENTLISTDEF: LPAREN (CONFIGPARAMS COMMA_SYMBOL)* (CONFIGPARAMS)* RPAREN;

EXPRESSIONX: EXPRESSION;

CONDITIONX: CONDITION;

TIMEOUTDEF: TIMEOUT LPAREN NUMBER RPAREN;

INTEGRATIONFLOWX: INTEGRATIONFLOW;

INBOUNDENDPOINTX: INBOUNDENDPOINT;

PIPELINEX: PIPELINE;

MEDIATORX: MEDIATOR;

OUTBOUNDENDPOINTX: OUTBOUNDENDPOINT;

PROCESS_MESSAGEX: PROCESS_MESSAGE;

ASX: AS;

COMMENTX: COMMENT;

COMMENTSTRINGX: COMMENTSTRING;

STRINGX: STRING;

URLSTRINGX: URLSTRING;

ARROW1X: ARROW1;
ARROW2X: ARROW2;
ARROW3X: ARROW3;
ARROW4X: ARROW4;



// LEXER: Keywords

STARTUMLX: STARTUML;
ENDUMLX: ENDUML;
PARTICIPANT: P A R T I C I P A N T;
PAR: P A R;
ALT: A L T;
REF: R E F;
END: E N D;
ELSE: E L S E;
LOOP: L O O P;
GROUP: G R O U P;
WITH : W I T H ;


// LEXER: symbol rules

AMP_SYMBOL : '&' ;
AMPAMP_SYMBOL : '&&' ;
CARET_SYMBOL : '^' ;
COMMA_SYMBOL : ',' ;
COMMENT_SYMBOL : '--' ;
CONTINUATION_SYMBOL : '\\' | '\u00AC' ;
EQ_SYMBOL : '=' ;
GE_SYMBOL : '>=' | '\u2265' ;
GT_SYMBOL : '>' ;
LE_SYMBOL : '<=' | '\u2264' ;
LT_SYMBOL : '<' ;
MINUS_SYMBOL : '-' ;
NE_SYMBOL : '<>' | '\u2260';
PLUS_SYMBOL : '+' ;
STAR_SYMBOL : '*' ;
SLASH_SYMBOL : '/' ;
UNDERSCORE : '-';
COLON: ':';
ARROW: '->';
fragment ARROW1: '=>+';
fragment ARROW2: '=>>+';
fragment ARROW3: '=>>-';
fragment ARROW4: '=>-';
SINGLEQUOTES: '\'';

// LEXER: miscellaneaous

LPAREN : '(' ;
RPAREN : ')' ;

NEWLINE
    : ( '\r\n' | '\n' | '\r' ) ;

WS
    : ' ';

IDENTIFIER
    : ( 'a'..'z' | 'A'..'Z' ) ( 'a'..'z' | 'A'..'Z' | DIGIT | '_')+ ;

ANY_STRING: ( 'a'..'z' | 'A'..'Z' | DIGIT | '_' | '\\' | '/' | '.'|':')+ ;

NUMBER
    : ( '0' | '1'..'9' DIGIT*) ('.' DIGIT+ )? ;

URL: ([a-zA-Z/\?&] | COLON | [0-9])+;

CONTINUATION
    : CONTINUATION_SYMBOL ~[\r\n]* NEWLINE -> skip ;

WHITESPACE
    : [ \t]+ -> skip ;

// LEXER: fragments to evaluate only within statements

//fragment ROUTINGSTATEMENT: IDENTIFIER WS+ ARROW WS+ IDENTIFIER WS+ COMMENTX
//                           WS+ STRINGX;
fragment STRING: DOUBLEQUOTES IDENTIFIER DOUBLEQUOTES;
fragment URLSTRING: DOUBLEQUOTES URL DOUBLEQUOTES;
fragment COMMENTSTRING: DOUBLEQUOTES COMMENTPARAMS DOUBLEQUOTES;
fragment EXPRESSION: LPAREN CONFIGPARAMS RPAREN;
fragment STARTUML: '@startuml';
fragment ENDUML: '@enduml';
fragment DOUBLEQUOTES: '"';
fragment POSTSCIPRT
    : ( 'a'..'z' | 'A'..'Z' | DIGIT | '_')*;
fragment CONFIGPARAMS: (WS | [a-zA-Z\?] | COLON | [0-9] | '$' | '.' | '@' |
                        SINGLEQUOTES | DOUBLEQUOTES | '{' | '}' | AMP_SYMBOL |
                        AMPAMP_SYMBOL | CARET_SYMBOL | COMMA_SYMBOL |
                        COMMENT_SYMBOL | CONTINUATION_SYMBOL | EQ_SYMBOL |
                        GE_SYMBOL | GT_SYMBOL | LE_SYMBOL | LT_SYMBOL |
                        MINUS_SYMBOL | NE_SYMBOL | PLUS_SYMBOL | STAR_SYMBOL |
                        SLASH_SYMBOL )+;
fragment COMMENTPARAMS: (WS | [a-zA-Z\?] | COLON | [0-9] | '$' | '.' | '@' |
                        SINGLEQUOTES | '{' | '}' | AMP_SYMBOL |
                        AMPAMP_SYMBOL | CARET_SYMBOL | COMMA_SYMBOL |
                        COMMENT_SYMBOL | CONTINUATION_SYMBOL | EQ_SYMBOL |
                        GE_SYMBOL | GT_SYMBOL | LE_SYMBOL | LT_SYMBOL |
                        MINUS_SYMBOL | NE_SYMBOL | PLUS_SYMBOL | STAR_SYMBOL |
                        SLASH_SYMBOL | '_')+;
fragment DIGIT : '0'..'9' ;
fragment INTEGRATIONFLOW: I N T E G R A T I O N F L O W;
fragment INBOUNDENDPOINT: I N B O U N D E N D P O I N T;
fragment HTTP: H T T P;
fragment PIPELINE: P I P E L I N E;
fragment PROCESSMESSAGE: P R O C E S S M E S S A G E;
fragment OUTBOUNDENDPOINT: O U T B O U N D E N D P O I N T ;
fragment MEDIATOR: M E D I A T O R;
fragment PROTOCOL: P R O T O C O L;
fragment PORT: P O R T;
fragment ENDPOINT: E N D P O I N T;
fragment CONTEXT: C O N T E X T;
fragment TIMEOUT: T I M E O U T;
fragment HOST: H O S T;
fragment CONFIGS: C O N F I G S;
fragment CONDITION: C O N D I T I O N;
fragment SOURCE: S O U R C E;
fragment PATTERN: P A T T E R N;
fragment PROCESS_MESSAGE: 'process_message';
fragment AS: A S;
fragment COMMENT: C O M M E N T;
fragment CALL: C A L L;
fragment FILTER: F I L T E R;
fragment RESPOND: R E S P O N D;
fragment LOG: L O G;
fragment ENRICH: E N R I C H;
fragment TRANSFORM: T R A N S F O R M;

// case insensitive lexer matching
fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');