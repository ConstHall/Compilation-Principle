package agenda;

import java.util.ArrayList;

import java.util.Collections;

import java.util.Comparator;

/**
 * User类：存储用户信息并包含相关会议的增删改查功能
 */
public class User {
    
    private String name;

    private String password;

    private ArrayList<Meeting> meetingList;

    User() {
        name = password = "";
        meetingList = new ArrayList<>();
    }

    User(String userName, String userPassword) {
        name = userName;    
        password = userPassword;
        meetingList = new ArrayList<>();
    }
    
    /**
	 * 获取用户名
	 * @return 用户名
	 */
    public String getName() {
        return name;
    }

    /**
	 * 设置用户名（赋值）
     * @param 需要设置的用户名newName
	 */ 
    public void setName(String newName) {
        name = newName;
    }
    
    /**
	 *  检查密码是否正确
     *  @param 输入密码userPassword
	 *  @return whether the input password is equal to User's password.
	 */
    public boolean checkPassword(String userPassword) {
        return password.equals(userPassword);
    }

    /**
	 * 设置密码
     * @param 新密码newPassword
	 */    
    public void setPassword(String newPassword) {
        password = newPassword;
    }

    /**
	 * 获取用户会议列表
	 * @return 会议列表
	 */ 
    public ArrayList<Meeting> getMeetingList() {
        return meetingList;
    }

    /**
	 * 检查会议是否冲突
	 * @param 需要检测的会议newMeeting
	 */      
    public void checkMeeting(Meeting newMeeting) {
        for (Meeting meeting : meetingList) {
            if (! (meeting.getStartDate().dateAfter(newMeeting.getEndDate()) || meeting.getEndDate().dateBefore(newMeeting.getStartDate()) )) {
                throw new Exception("会议时间冲突！");
            } 
            else if (meeting.getLabel().equals(newMeeting.getLabel())) {
                throw new Exception("会议时间冲突！");
            }
        }
    }

    /**
	 * 添加会议
	 * @param 需要添加的会议newMeeting
	 */     
    public void addMeeting(Meeting newMeeting) {
        meetingList.add(newMeeting);
    }

    /**
	 * 查询时间范围内的会议
	 * @param 开始时间start
     * @param 结束时间end
     * @return 时间范围内的所有会议
	 */ 
    public ArrayList<Meeting> queryMeetingByDate(Date startDate, Date endDate) {
        ArrayList<Meeting> queryList = new ArrayList<>();
        for (Meeting meeting : meetingList) {
            if (! (startDate.dateAfter(meeting.getEndDate()) || endDate.dateBefore(meeting.getStartDate())) ) {
            	queryList.add(meeting);
            }     
        }
        if (queryList.isEmpty()) {
        	return queryList;
        }
        
        //根据时间顺序对结果输出进行排序
        Collections.sort(queryList, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                if (o1.getStartDate().dateBefore(o2.getStartDate())) {
                	return 1;
                }
                return 0;
            }
        } );
        return queryList;
    }

    /**
	 * 根据会议标签查询会议
     * @param 会议标签label
     * @return 会议标签对应的会议
	 */ 
    public Meeting getMeeting(String label) {
        for (Meeting meeting : meetingList) {
            if (meeting.getLabel().equals(label)) {
                return meeting;
            }
        }
        return null;
    }

    /**
	 * 根据会议标签删除会议
     * @param 会议标签label
	 */     
    public void deleteMeeting(String label) {
        for (Meeting meeting : meetingList) {
            if (meeting.getLabel().equals(label)) {
                meetingList.remove(meeting);
                return;
            }
        }
        throw new Exception("需要删除的会议不存在！");
    }
}