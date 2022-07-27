package PersonalTax;

/**
 * TaxRule类：存储阶梯缴税规则表的内容以及阶梯金额和阶梯税率的修改函数
 */
public class TaxRule {
    private int taxStart;//个人所得税起征点
    private int stairNum;//阶梯缴税制度的分层数量
    private int[] stairMoney;//各阶梯之间的分界点金额
    private double[] taxRate;//阶梯税率

    /**
     * 构造函数，默认个人所得税起征点为 1600 元
     * 超出部分的默认缴税规则如下：
     * 不超过500元的部分，阶梯税率为 5%
     * 超过 500 元至 2000 元的部分，阶梯税率为 10%
     * 超过 2000 元至 5000 元的部分，阶梯税率为 15%
     * 超过 5000 元至 20000 元的部分，阶梯税率为 20%
     * 超过 20000 元的部分，阶梯税率为 25%
     */
    public TaxRule() {
        taxStart = 1600;
        stairNum = 5;
        stairMoney = new int[]{0, 500, 2000, 5000, 20000};
        taxRate = new double[]{0.05, 0.1, 0.15, 0.2, 0.25};
    }

    /**
     * 获取个人所得税起征点
     * @return 个人所得税起征点 taxStart
     */
    public int getTax_Start() {
        return taxStart;
    }

    /**
     * 获取阶梯缴税制度的分层数量
     *
     * @return 分层数量 stairNum
     */
    public int getNum_Stair() {
        return stairNum;
    }

    /**
     * 各阶梯之间的分界点金额
     * @return 各阶梯之间的分界点金额所组成的int数组
     */
    public int[] getMoney_Stair() {
        return stairMoney;
    }

    /**
     * 获取某个临界点金额
     * @param i 获取第i个临界点金额
     * @return 第i个临界点金额 stairMoney[i]
     */
    public int getMoney_Stair(int i) {
        return stairMoney[i];
    }

    /**
     * 获取阶梯税率
     * @return 阶梯税率数组(类型为double)
     */
    public double[] getRate_Stair() {
        return taxRate;
    }

    /**
     * 获取某一级阶梯税率
     * @param i 获取第i级阶梯税率
     * @return 第i级阶梯税率 taxRate[i]
     */
    public double getRate_Stair(int i) {
        return taxRate[i];
    }

    /**
     * 修改阶梯数量
     * @param new_stairNum 新的阶梯数量
     */
    public void setStairNum(int new_stairNum) {
        stairNum = new_stairNum;
    }

    /**
     * 修改个人所得税起征点
     * @param newStart 新的个人所得税起征点
     */
    public void setTaxStart(int newStart) {
        if (newStart < 0) {
            System.out.println("--------------------------------------");
            System.out.println("修改失败！起征点金额应≥0");
            System.out.println("--------------------------------------");
            return;
        }
        taxStart = newStart;
    }

    /**
     * 修改各阶梯之间的分界点金额
     * @param newStairMoney 新的分界点金额数组
     */
    public void setStairMoney(int[] newStairMoney) {
        for (int j : newStairMoney) {
            if (j < 0) {
                System.out.println("--------------------------------------");
                System.out.println("修改失败！分界点金额应≥0");
                System.out.println("--------------------------------------");
                return;
            }
        }
        stairMoney = newStairMoney;
    }

    /**
     * 修改阶梯税率
     *
     * @param newRate 新的阶梯税率
     */
    public void setTaxRate(double[] newRate) {
        for (double v : newRate) {
            if (v < 0 || v > 1) {
                System.out.println("--------------------------------------");
                System.out.println("修改失败！税率合法区间为[0-1]");
                System.out.println("--------------------------------------");
                return;
            }
        }
        taxRate = newRate;
    }
}

