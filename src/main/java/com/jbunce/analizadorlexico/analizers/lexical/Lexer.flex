package com.jbunce.analizadorlexico.analizers.lexical;
import com.jbunce.analizadorlexico.analizers.Tokens;

%%
%public
%class Lexer
%type Tokens

L=[a-zA-Z]
D=[0-9]+
space=[\t \r]+

%{
    public String lexeme;
%}

%%

displayData  { lexeme = yytext(); return Tokens.DISPLAY_DATA; }

{space} { /* ignore */ }
"//".* { /* ignore */ }

"\n" { return Tokens.NEW_LINE; }
"{" { lexeme = yytext(); return Tokens.OPEN_BRACE; }
"}" { lexeme = yytext(); return Tokens.CLOSE_BRACE; }
"(" { lexeme = yytext(); return Tokens.OPEN_PARENTHESIS; }
")" { lexeme = yytext(); return Tokens.CLOSE_PARENTHESIS; }
";" { lexeme = yytext(); return Tokens.SEMICOLON; }
"\"" { lexeme = yytext(); return Tokens.QUOTE; }
{L} { lexeme = yytext(); return Tokens.NON_SPECIFIC; }
 . { return Tokens.ERROR; }
