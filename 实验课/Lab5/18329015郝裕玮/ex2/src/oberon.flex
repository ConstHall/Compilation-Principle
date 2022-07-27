//用户代码
import java.io.*;
import exceptions.*;

//选项与声明
%%
//定义生成词法分析器Java文件的文件名
%class OberonScanner
//使生成的类是public
%public
//设置扫描函数的返回类型
%type String
//使扫描函数声明抛出异常
%yylexthrow LexicalException
//其中用户代码部分直接被复制到扫描函数中，并且在每次文件结束时执行。这个用户代码应该返回表示文件结束的值
%eofval{
	return "EOF";
%eofval}
//支持字符集，防止溢出
%unicode
//行计数器，yyline记录当前行数
%line 
//列计数器，yycolumn记录当前列数
%column
//不区分字符大小写，符合oberon-0的词法规则
%ignorecase

//获取当前行数或列数
%{
    int get_line(){	return yyline;}
    int get_column(){ return yycolumn;}
%}

//正则表达式
Keyword = "INTEGER" | "BOOLEAN" | "CONST" | "TYPE" | "VAR" | "RECORD" | "ARRAY" | "Read" | "Write" | "WriteLn"
ReservedWord = "MODULE" | "PROCEDURE" | "OF" | "BEGIN" | "END" | "IF" | "THEN" | "ELSE" | "ELSIF" |"WHILE" | "DO"
Decimal = [1-9][0-9]*
Octal = 0[0-7]*
Number = {Decimal} | {Octal}
Operator = "+" | "-" | "*" | "DIV" | "MOD" | "&" | " OR " | "~" | "=" | "#" | ">" | " >=" | "<" | "<=" | ":=" | "[" | "]" | "." | "(" | ")" | ":"
Identifier = [:jletter:][:jletterdigit:]*
Punctuation = ";" | ","
Comment = "(*" [^*] ~"*)" | "(*" "*"+ ")"
WhiteSpace = " " | \r | \n | \r\n | [ \t\f]
//异常情况的正则表达式
WrongInteger = {Number} + {Identifier} +
WrongOctal = 0[0-7]*[9|8][0-9]*
WrongComment = "(*" ([^\*]|"*"+[^\)])*|([^\(]|"("+[^\*])*"*)"

//词法规则
%%
<YYINITIAL>{
	{Keyword}							{return "Keyword";}
    {ReservedWord}						{return "ReservedWord";}
	{Number}							{
											if(yylength() > 12){
                                                throw new IllegalIntegerRangeException();
                                            }
											else{
                                                return "Number";
                                            }
												
										}
    {Operator}							{return "Operator";}
	{Identifier}						{
											if(yylength() > 24){
                                                throw new IllegalIdentifierLengthException();
                                            }
											else{
                                                return "Identifier";
                                            }
                                        }
    {Punctuation}						{return "Punctuation";}
	{Comment}							{return "Comment";}
	{WhiteSpace}						{}
	{WrongInteger}					    {throw new IllegalIntegerException();}
	{WrongOctal}						{throw new IllegalOctalException();}
	{WrongComment}					    {throw new MismatchedCommentException();}
	.                              		{throw new IllegalSymbolException(); } 
}