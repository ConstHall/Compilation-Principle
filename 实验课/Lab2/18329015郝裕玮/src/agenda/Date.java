package agenda;

/**
 * Date类：存储会议时间并包括日期的大小比较和日期输入格式规范检查
 */

public class Date {

	//年
    private int year;

    //月
    private int month;

    //日
    private int day;

    //小时
    private int hour;

    //分钟
    private int minute;

    Date() {
        year = month = day = hour = minute = 0;
    }

    Date(int y, int m, int d, int h, int mi) {
        year = y;   month = m;  day = d;
        hour = h;   minute = mi;
    }
    
    /**
	 * 输入的时间字符串转换为各变量赋值
     * @param 用户输入的日期strDate
	 */ 
    Date(String strDate) {
    	//检查日期输入格式是否正确
        if (!checkDate(strDate)) {
        	throw new Exception("日期格式错误！");
        }
        //对字符串进行分割转换
        //例：2022-03-26|17:30
        year = Integer.parseInt(strDate.substring(0, 4));
        month = Integer.parseInt(strDate.substring(5, 7));
        day = Integer.parseInt(strDate.substring(8, 10));
        hour = Integer.parseInt(strDate.substring(11, 13));
        minute = Integer.parseInt(strDate.substring(14, 16));
    }

    /**
	 * 获取年份
	 * @return 年份
	 */
    public int getYear() {
        return year;
    }

    /**
	 * 设置年份（赋值）
     * @param 需要设置的年份值newYear
	 */
    public void setYear(int newYear) {
        year = newYear;
    }
    
    /**
	 * 获取月份
	 * @return 月份
	 */
    public int getMonth() {
        return month;
    }

    /**
	 * 设置月份（赋值）
     * @param 需要设置的月份值newMonth
	 */
    public void setMonth(int newMonth) {
        month = newMonth;
    }

    /**
	 * 获取日期的天
	 * @return 日期的天
	 */
    public int getDay() {
        return day;
    }

    /**
	 * 设置日期的天（赋值）
     * @param 需要设置的日期的天的值newDay
	 */
    public void setDay(int newDay) {
        day = newDay;
    }
    
    /**
	 * 获取小时
	 * @return 小时
	 */
    public int getHour() {
        return hour;
    }

    /**
	 * 设置小时（赋值）
     * @param 需要设置的小时值年份值newHour
	 */
    public void setHour(int newHour) {
        hour = newHour;
    }

    /**
	 * 获取分钟
	 * @return 分钟
	 */
    public int getMinute() {
        return minute;
    }

    /**
	 * 设置分钟（赋值）
     * @param 需要设置的分钟值newMinute
	 */    
    public void setMinute(int newMinute) {
        minute = newMinute;
    }
    
    /**
	 * 比较两个日期的早晚关系（早）
     * @param 需要拿来对比的日期otherDate
     * @return 判断传入参数otherDate是否比调用该函数的Date实例更早，早则返回True
	 */    
    public boolean dateBefore(Date otherDate) {
    	if(year < otherDate.year) {
    		return true;
    	}
    	else if(year == otherDate.year && month < otherDate.month) {
    		return true;
    	}
    	else if(year == otherDate.year && month == otherDate.month && day < otherDate.day) {
    		return true;
    	}
    	else if(year == otherDate.year && month == otherDate.month && day == otherDate.day && hour < otherDate.hour) {
    		return true;
    	}
    	else if(year == otherDate.year && month == otherDate.month && day == otherDate.day && hour == otherDate.hour && minute < otherDate.minute) {
    		return true;
    	}
    	else {
    		return false;
    	}
//        return ( year < otherDate.year || month < otherDate.month || day < otherDate.day 
//                 || hour < otherDate.hour || ( hour == otherDate.hour && minute < otherDate.minute ) );
    }
    
    /**
	 * 比较两个日期的早晚关系（晚）
     * @param 需要拿来对比的日期otherDate
     * @return 判断传入参数otherDate是否比调用该函数的Date实例更晚，晚则返回True
	 */    
    public boolean dateAfter(Date otherDate) {
    	if(year > otherDate.year) {
    		return true;
    	}
    	else if(year == otherDate.year && month > otherDate.month) {
    		return true;
    	}
    	else if(year == otherDate.year && month == otherDate.month && day > otherDate.day) {
    		return true;
    	}
    	else if(year == otherDate.year && month == otherDate.month && day == otherDate.day && hour > otherDate.hour) {
    		return true;
    	}
    	else if(year == otherDate.year && month == otherDate.month && day == otherDate.day && hour == otherDate.hour && minute > otherDate.minute) {
    		return true;
    	}
    	else {
    		return false;
    	}
//        return ( year > otherDate.year || month > otherDate.month || day > otherDate.day 
//                 || hour > otherDate.hour || ( hour == otherDate.hour && minute > otherDate.minute ) );
    }

    /**
	 * 比较两个日期是否相等
     * @param 需要拿来对比的日期otherDate
     * @return 判断传入参数otherDate是否与调用该函数的Date实例相等，相等则返回True
	 */    
    public boolean dateEqual(Date otherDate) {
        return ( year == otherDate.year && month == otherDate.month && day == otherDate.day
                 && hour == otherDate.hour && minute == otherDate.minute );
    }
    
    /**
     * 将日期转换为可打印的格式化字符串
     * @return 格式化字符串判断传入参数otherDate是否与调用该函数的Date实例相等，相等则返回True
     */
    public String DateToString() {
        return  String.format("%04d", year) + '-' + String.format("%02d", month) + '-' + String.format("%02d", day) 
        + '|' + String.format("%02d", hour) + ':' + String.format("%02d", minute) ;
    }

    /**
     * 检查日期格式是否正确规范正确
     * @param 需要被检查的日期strDate
     * @return 是否正确（True or False）
     */
    static boolean checkDate(String strDate) {
        if (strDate.length() != 16) {
        	return false;
        }
        for (int i = 0; i < 16; i++) {
            if (i == 4 || i == 7) {
                if (strDate.charAt(i) != '-') {
                	return false;
                }
            } 
            else if (i == 10) {
                if (strDate.charAt(i) != '|') {
                	return false;
                }
                    
            } 
            else if (i == 13) {
                if (strDate.charAt(i) != ':') {
                	return false;
                }
            } 
            else if (!Character.isDigit(strDate.charAt(i))) {
            	return false;
            }     
        }
        return true;
    }
};