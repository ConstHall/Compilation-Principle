package PersonalTax;

import java.util.Scanner;

/**
 * UI类：用户交互界面
 */
public class UI {
    /**
     * main函数:展示缴税规则以及该程序具有的功能
     */
    public static void main(String[] args) {
        TaxRule taxrule = new TaxRule();//创建缴税规则TaxRule的对象实例taxrule，便于后续调用

        System.out.println("\n欢迎使用个人所得税计算器！\n");
        System.out.println("-------------------------------------\n");

        printTable(taxrule);//打印阶梯缴税规则
        
        System.out.println("-------------------------------------\n");
        
        chooseFunction(inputNum() , taxrule);//输入数字，选择相关功能
    }

    /**
     * 打印缴税规则表
     * @param taxrule 阶梯缴税规则
     */
    static void printTable(TaxRule taxrule) {
        int stair = taxrule.getNum_Stair();//缴税分级阶梯层数
        int[] stairMoney = taxrule.getMoney_Stair();//缴税阶梯分层金额
        double[] taxRate = taxrule.getRate_Stair();//缴税阶梯分层税率
        //打印各级缴税金额及税率
        System.out.printf("当前个人所得税起征点为 %d 元，超出部分缴税规则如下：\n\n", taxrule.getTax_Start());
        System.out.printf("不超过 %d 元的部分，阶梯税率为 %.3f%%\n", stairMoney[1],  (taxRate[0] * 100));
        for (int i = 1; i <= stair - 2; i++) {
            System.out.printf("超过  %d 元至 %d 元的部分，阶梯税率为 %.3f%%\n", stairMoney[i], stairMoney[i + 1],  (taxRate[i] * 100));
        }
        System.out.printf("超过  %d 元的部分，阶梯税率为 %.3f%%\n\n", stairMoney[stair - 1],  (taxRate[stair - 1] * 100));
    }

    /**
     * 获取用户输入
     * @return 用户输入(1-4)
     */
    static int inputNum() {
        System.out.println("本程序所有功能如下所示：\n");
        System.out.println("1：计算个人所得税\n2：修改个人所得税起征点\n3：修改阶梯税率\n4：退出程序\n");
        
        Scanner input = new Scanner(System.in);//获取输入
        
        System.out.println("请输入数字1-4来选择您需要的功能：");
        
        return input.nextInt();//返回输入数字
    }

    /**
     * 根据用户输入来调用相关功能
     * @param number 用户输入的数字
     * @param taxrule 阶梯税率表
     */
    static void chooseFunction(int number, TaxRule taxrule) {
        Scanner input = new Scanner(System.in);
        
        while (number != 4) {
            switch (number) {
                case 1:
                    //根据个人收入计算缴税总额
                    System.out.println("\n-------------------------------------\n");
                    System.out.println("请输入您的收入：");
                    
                    Calculator calculator = new Calculator();//创建计算器Calculator的对象实例calculator，便于后续调用
                    
                    double tax = calculator.taxCalculate(input.nextInt(), taxrule);//根据输入工资计算缴税总额
                    
                    System.out.printf("您应缴纳的个人所得税金额为 %.3f 元\n\n",tax);
                    
                    System.out.println("-------------------------------------\n");
                    break;
                    
                case 2:
                    //修改个人所得税起征点
                    System.out.println("--------------------------------------\n");
                    System.out.printf("当前个人所得税起征点为 %d 元，请输入新的个人所得税起征点：",taxrule.getTax_Start());
                    
                    taxrule.setTaxStart(input.nextInt());
                    
                    System.out.print("\n修改成功！\n\n");
                    
                    printTable(taxrule);
                    
                    System.out.println("-------------------------------------\n");
                    break;
                    
                case 3:
                    //修改阶梯分层金额和对应税率
                    System.out.println("\n-------------------------------------\n");
                    System.out.println("请按从小到大的顺序输入阶梯分层金额（第1个数字必须为0，各数字间用空格区分，输入回车结束本次输入）：\n");
                    System.out.println("示范输入：0 500 1000 1500 2000\n");
                    
                    input.nextLine();
                    String[] inputArray = input.nextLine().split(" ");
                    while (!inputArray[0].equals("0")) {
                        System.out.println("第1个数字必须为0，请重新输入!");
                        inputArray = input.nextLine().split(" ");
                    }
                    //更新金额数组
                    int[] new_stairMoney = new int[inputArray.length];
                    int temp=inputArray.length;
                    for (int i = 0; i <= inputArray.length - 1; i++) {
                        new_stairMoney[i] = Integer.parseInt(inputArray[i]);
                    }
                    taxrule.setStairNum(inputArray.length);
                    taxrule.setStairMoney(new_stairMoney);
                    
                    System.out.println("\n请输入阶梯税率的小数形式（各数字间用空格区分，输入回车结束本次输入）：\n");
                    System.out.println("示范输入：0.05 0.1 0.15 0.2 0.25\n");
                    System.out.println("请注意，输入的阶梯税率个数应与之前输入的阶梯金额个数保持一致！\n");
                    
                    inputArray = input.nextLine().split(" ");
                    while (inputArray.length != temp) {
                        System.out.println("阶梯税率数量不匹配，请核对后重新输入：");
                        inputArray = input.nextLine().split(" ");
                    }
                    //更新税率数组
                    double[] newTaxRate = new double[inputArray.length];
                    for (int i = 0; i <= inputArray.length - 1; i++) {
                        newTaxRate[i] = Double.parseDouble(inputArray[i]);
                    }
                    taxrule.setTaxRate(newTaxRate);
                    System.out.print("\n修改成功！\n\n");
                    printTable(taxrule);
                    System.out.println("-------------------------------------\n");
                    break;
                //错误输入应对办法 
                default:
                    System.out.println("-------------------------------------\n");
                    System.out.println("输入错误！请输入1-4选择对应功能\n");
                    System.out.println("-------------------------------------\n");
            }
            number = inputNum();
        }
        System.out.println("\n谢谢您的使用，我们下次再见！\n");
    }
}

