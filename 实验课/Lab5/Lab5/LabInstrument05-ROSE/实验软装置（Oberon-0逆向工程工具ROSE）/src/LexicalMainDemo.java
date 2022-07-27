
import java.io.StringReader;

public class LexicalMainDemo {
    //Scanner使用测试方式
    public static void main(String[] args) throws Exception {
        //源码数据输入与正确结果（可以使用其他方式读取）
        String code="MODULE Sample;\n\tVAR a:INTEGER;\n\tPROCEDURE Test;\n\tEND Test;\nBEGIN\n\tRead(a);\nEND Sample.";
        StringReader reader=new StringReader(code);
        String answer="(#1) (#8)[Sample] (#5) (#6) (#8)[a] (#7) (#9) (#5) (#2) (#8)[Test] (#5) (#4) (#8)[Test] (#5) (#3) (#8)[Read] (#11) (#8)[a] (#12) (#5) (#4) (#8)[Sample] (#10) (#0)";
        
        //创建由JFlex生成的Scanner类实例
        SampleScanner scanner=new SampleScanner(reader);
        StringBuilder builder=new StringBuilder();
        while(!scanner.yyatEOF()){//获取Scanner输出的token
            java_cup.runtime.Symbol s=scanner.next_token();
            //记录token
            if(s.value!=null){
                builder.append("("+s.toString()+")["+(String)s.value+"]");
            }else{
                builder.append("("+s.toString()+")");
            }
            builder.append(" ");
        }
        //比较Scanner的输出和正确结果
        String result=builder.toString().trim();
        if(answer.equals(result)){
            System.out.println("check success");
        }else{
            System.out.println("check fail");
        }
    }
}
