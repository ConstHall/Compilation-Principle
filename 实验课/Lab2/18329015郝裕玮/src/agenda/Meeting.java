package agenda;

/**
 * Meeting类：存储每个会议的相关信息
 */

public class Meeting {

    private Date startDate;

    private Date endDate;

    private String sponsor;

    private String participator;

    private String label;

    Meeting() {
        startDate = new Date("0000-00-00|00:00");
        endDate = new Date("0000-00-00|00:01");
        sponsor = "";
        participator = "";
        label = "";
    }

    Meeting(Date initStartDate, Date initEndDate, String initSponsor, String initParticipator, String initLabel) {
    	//若开始时间和结束时间有冲突则报错
        if (initEndDate.dateBefore(initStartDate) || initEndDate.dateEqual(initStartDate)) {
        	throw new Exception("结束时间早于开始时间！");
        }
        startDate = initStartDate;
        endDate = initEndDate;
        sponsor = initSponsor;
        participator = initParticipator;
        label = initLabel;
    }

    /**
	 * 获取开始时间
	 * @return 开始时间
	 */
    public Date getStartDate() {
        return startDate;
    }

    /**
	 * 设置开始时间（赋值）
     * @param 需要设置的开始时间newStartDate
	 */
    public void setStartDate(Date newStartDate) {
        startDate = newStartDate;
    }

    /**
	 * 获取结束时间
	 * @return 结束时间
	 */
    public Date getEndDate() {
        return endDate;
    }

    /**
	 * 设置结束时间（赋值）
     * @param 需要设置的结束时间newEndDate
	 */
    public void setEndDate(Date newEndDate) {
        endDate = newEndDate;
    }

    /**
	 * 获取会议主持人姓名
	 * @return 会议主持人姓名
	 */
    public String getSponsor() {
        return sponsor;
    }

    /**
	 * 设置会议主持人
     * @param 需要设置的会议主持人姓名newSponsor
	 */
    public void setSponsor(String newSponsor) {
        sponsor = newSponsor;
    }

    /**
	 * 获取参会人员姓名
	 * @return 参会人员姓名
	 */
    public String getParticipator() {
        return participator;
    }

    /**
	 * 设置参会人员
     * @param 需要设置的参会人员姓名newParticipator
	 */
    public void setParticipator(String newParticipator) {
        participator = newParticipator;
    }

    /**
	 * 获取会议标签
	 * @return 会议标签
	 */
    public String getLabel() {
        return label;
    }

    /**
	 * 设置会议标签
     * @param 需要设置的会议标签newLabel
	 */
    public void setLabel(String newLabel) {
        label = newLabel;
    }
}
