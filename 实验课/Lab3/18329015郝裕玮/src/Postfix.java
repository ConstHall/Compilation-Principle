import java.io.*;

/**
 * Parser类用于语法分析
 */
class Parser {

    /** 下一个要分析的字符 */
    static int lookahead;

    /** 表达式中出现错误的位置（下标） */
    int errorPos;

    /** 表达式中出现错误的数量 */
    int errorCnt;

    /** 判断输入是否结束的标志位 */
    boolean endFlag;

    /** 用于输出错误信息 */
    private PrintWriter errorPrint;

    /**
	 * Parser类的构造函数
     * @throws IOException
	 */
    public Parser() throws IOException {
        errorPos = 0;
        errorCnt = 0;
        endFlag = true;
        errorPrint = new PrintWriter(System.out);
        readnext();
    }

    /**
	 * expr函数用于调用相关函数并输出最终信息
     * @throws IOException
	 */    
    void expr() throws IOException {
        term();
        rest();
        //若表达式存在错误则输出
        if (errorCnt > 0) {
            //输出错误数量
            System.out.println("\n\n表达式错误总数为：" + errorCnt + "\n");
            //清空缓冲区，将其所有内容均进行输出
            errorPrint.flush();
            //关闭流
            errorPrint.close();
        } 
        else {
            System.out.println("\n表达式无错误！");
        }
    }

    /**
	 * rest函数用于将加减符号（运算符）写入输出结果列表中
     * @throws IOException
	 */
    void rest() throws IOException {
        //执行循环直到表达式结束
        while (endFlag != false) {
            //匹配+号则下一步调用term函数检验数字，并将+号写入输出结果列表
            if (lookahead == '+') {
                match('+');
                term();
                System.out.write('+');
            }
            //匹配-号则下一步调用term函数检验数字，并将-号写入输出结果列表 
            else if (lookahead == '-') {
                match('-');
                term();
                System.out.write('-');
            }
            //若当前运算符不是+-符号而是数字（运算量），则说明缺少运算符
            else if (char_valid() == true) {
                errorCnt++;
                errorPrint.write("在位置" + (errorPos - 1) + "和位置" + errorPos + "之间出现语法错误：缺少运算符\n\n");
                readnext();
            }
            //若当前运算符是加减以外的符号并且也不是数字（运算量），则说明出现了未定义字符
            else {
                errorCnt++;
                errorPrint.write("在位置" + errorPos + "处出现词法错误：出现未定义字符\n\n");
                readnext();
            }
        }
    }

    /**
	 * term函数用于将数字（运算量）写入输出结果列表中
     * @throws IOException
	 */
    void term() throws IOException {
        //执行循环直到表达式结束
        while (endFlag != false) {
            //若表达式当前位置是数字则将其写入结果列表并与lookahead进行匹配
            if (Character.isDigit((char)lookahead)) {
                System.out.write((char)lookahead);
                match(lookahead);
                break;
            } 
            //若仍为+-运算符则说明当前位置缺少数字作为运算量
            else if (lookahead == '+' || lookahead == '-') {
                errorCnt++;
                errorPrint.write("在位置" + (errorPos - 1) + "和位置" + errorPos + "之间出现语法错误：缺少运算量\n\n");
                readnext();
            }
            //若既不是数字（运算量）也不是+-（运算符）则说明出现了未定义字符
            else {
                errorCnt++;
                errorPrint.write("在位置" + errorPos + "处出现词法错误：出现未定义字符\n\n");
                readnext();
            }
            //针对表达式末尾缺少运算量的特例
            if (endFlag == false) {
                errorCnt++;
                errorPrint.write("在位置" + (errorPos - 1) + "和位置" + errorPos + "之间出现语法错误：缺少运算量\n\n");
            }
        }
        
    }

    /**
	 * match函数用于检测当前lookahead与传入字符是否一致
     * @param t 传入字符
     * @throws IOException
	 */
    void match(int t) throws IOException {
        if (lookahead == t) readnext();
        else if (char_valid()) {
            errorCnt++;
            readnext();
        } 
        else {
            errorCnt++;
            readnext();
        }
    }

    /**
	 * char_valid函数用于检测当前字符是否为数字或+-运算符
     * @throws IOException
	 */
    boolean char_valid() {
        //如果是数字或+-运算符则返回true
        if (Character.isDigit((char)lookahead) == true || lookahead == '+' || lookahead == '-'){
            return true;
        }
        else{
            return false;
        }
    }

    /**
	 * readnext函数用于读取表达式中下一个字符
     * @throws IOException
	 */    
    void readnext() throws IOException {
        //若已经到达表达式末尾则直接return
        if (endFlag == false){
            return;
        }

        //读取下一个字符时自动跳过读取过程中遇到的空格直至读到非空格的字符 
        do {
            lookahead = System.in.read();
            errorPos++;
        } while (lookahead == ' ');

        //读到回车符或制表符时将标志位endFlag设为false
        if (lookahead == '\r' || lookahead == '\n'){
            endFlag = false;
        }        
    }
}

/**
 * Postfix类：主类，生成Parser类实例对象
 */
public class Postfix {
    public static void main(String[] args) throws IOException {
        System.out.println(
            "Input an infix expression and output its postfix notation:"
        );
        new Parser().expr();
        System.out.println("\nEnd of program.");
    }
}