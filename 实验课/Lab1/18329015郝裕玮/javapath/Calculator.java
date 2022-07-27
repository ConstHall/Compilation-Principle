package PersonalTax;

/**
 * Calculator类：根据TaxRule来计算用户应缴的个人所得税金额
 */
public class Calculator {
    /**
     * @param income   用户输入金额
     * @param taxrule 阶梯缴税规则
     * @return 用户应缴的个人所得税金额
     */
    public double taxCalculate(int income, TaxRule taxrule) {
        if (income < 0) {
            System.out.println("输入错误！输入金额必须≥0");
            return 0;
        }
        
        double tax = 0;
        int stair;
        //若未达到缴税标准则返回0
        if (income < taxrule.getTax_Start()) {
            return 0;
        }
        
        int exceedIncome = income - taxrule.getTax_Start();//计算需要缴税部分的总金额
        //根据阶梯金额和税率进行分级累加
        for (stair = 1; stair <= taxrule.getNum_Stair() - 1 && exceedIncome > taxrule.getMoney_Stair(stair); stair++) {
            tax += (taxrule.getMoney_Stair(stair) - taxrule.getMoney_Stair(stair - 1)) * taxrule.getRate_Stair(stair - 1);
        }
        tax += (exceedIncome - taxrule.getMoney_Stair(stair - 1)) * taxrule.getRate_Stair(stair - 1);
        
        return tax;
    }
}

