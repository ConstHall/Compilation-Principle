package agenda;

import java.util.ArrayList;

import java.io.*;

/**
 * Service类：提供会议的增删改查功能
 */

public class Service {
    
    private ArrayList<User> userList;

    Service() {
        userList = new ArrayList<>();
    }

    /**
	 * 用户注册
     * @param 用户名userName和密码userPassword
	 */
    public void userRegister(String userName, String userPassword) {
    	//遍历用户名表查看该用户名是否已被注册过
        for (User user : userList) {
            if (user.getName().equals(userName)) {
            	throw new Exception("该用户名已被注册！");
            } 
        }

        userList.add(new User(userName, userPassword));
    }

    /**
	 * 用户登录
     * @param 用户名userName和密码userPassword
	 */
    public void userLogIn(String userName, String userPassword) {
    	//遍历用户名表查看该用户名是否存在
        for (User user : userList) {
            if (user.getName().equals(userName)) {
            	//检查用户名和密码是否匹配
                if (user.checkPassword(userPassword)) {
                	return;
                }
                //不匹配则报错
                else {
                	throw new Exception("用户名和密码不匹配！");
                }       
            }
        }
        throw new Exception("用户不存在！");
    }

    /**
	 * 增加新会议
     * @param 会议主持人用户名userName
     * @param 会议主持人密码userPassword
     * @param 参会人员用户名otherName
     * @param 开始时间start
     * @param 结束时间end
     * @param 会议标签label
	 */
    public void addMeeting(String userName, String userPassword, String otherName, Date start, Date end, String label) {
    	//先登录，检查主持人用户名和密码是否正确
        userLogIn(userName, userPassword);
        
        User sponsor = null;
        User participator = null;
        
        //遍历用户列表查看参会人员姓名是否存在
        for (User user : userList) {
            if (user.getName().equals(otherName)) {
                participator = user;
                break;
            }
        }
        
        if (participator == null) {
        	throw new Exception("参会人员未注册！");
        }
            
        
        //遍历用户列表查看会议主持人姓名是否存在  
        for (User user : userList) {
            if (user.getName().equals(userName)) {
                sponsor = user;
                break;
            }
        }

        if (sponsor == null) {
        	throw new Exception("会议主持人未注册！");
        }
        
        //初始化会议Meeting类
        Meeting newMeeting = new Meeting(start, end, userName, otherName, label);
        //检查参会人员和主持人的会议时间安排是否与新会议冲突
        sponsor.checkMeeting(newMeeting);
        participator.checkMeeting(newMeeting);
        //不冲突则将新会议添加到主持人和参会人员的议程表中
        sponsor.addMeeting(newMeeting);
        participator.addMeeting(newMeeting);
    }

    /**
	 * 查询某人在某一时间段里的所有会议
     * @param 用户名userName
     * @param 密码userPassword
     * @param 开始时间start
     * @param 结束时间end
     * @return 该用户在该时间段内的所有会议
	 */
    public ArrayList<Meeting> queryMeeting(String userName, String userPassword, Date start, Date end) {
    	//先登录，检查主持人用户名和密码是否正确
        userLogIn(userName, userPassword);
        //遍历用户列表，找到该用户
        for (User user : userList) {
        	//找到后查询在该时间范围内的所有会议
            if (user.getName().equals(userName)) {
            	return user.queryMeetingByDate(start, end);
            }
        }

        return null;
    }

     /**
	 * 根据会议标签删除对应会议
     * @param 用户名userName
     * @param 密码userPassword
     * @param 会议标签label
	 */   
    public void deleteMeeting(String userName, String userPassword, String label) {
    	//先登录，检查用户名和密码是否正确
        userLogIn(userName, userPassword);
        //遍历用户列表，找到该用户
        for (User user : userList) {
        	//找到后开始删除会议
            if (user.getName().equals(userName)) {
            	//根据标签定位该会议
                Meeting meetingToDelete = user.getMeeting(label);
                if (meetingToDelete == null) {
                	throw new Exception("该会议不存在！");
                }
                    
                String otherName;
                //找到该会议的主持人进而定位参会人员
                if (meetingToDelete.getSponsor().equals(userName)) {
                	otherName = meetingToDelete.getParticipator();
                }
                    
                else {
                	otherName = meetingToDelete.getSponsor();
                }
                //在主持人和参会人员的会议列表中均删除该会议 
                for (User other : userList) {
                    if (other.getName().equals(otherName)) {
                        other.deleteMeeting(label);
                        user.deleteMeeting(label);
                    }
                }

            }
        }

    }

    /**
	 * 清空某个用户参与的所有会议
     * @param 用户名userName
     * @param 密码userPassword
	 */
    public void clearMeeting(String userName, String userPassword) {
    	//先登录，检查用户名和密码是否正确
        userLogIn(userName, userPassword);
        //遍历用户列表，找到该用户
        for (User user : userList) {
        	//找到后开始删除所有会议
            if (user.getName().equals(userName)) {
                ArrayList<Meeting> allMeetings = user.getMeetingList();
                ArrayList<Meeting> cloneMeetings = new ArrayList<Meeting>();
                for (Meeting meeting: allMeetings) {
                	cloneMeetings.add(meeting);
                }
                //删除所有会议
                for (Meeting meeting : cloneMeetings) {
                	deleteMeeting(userName, userPassword, meeting.getLabel());
                }  
            }
        }
    }

    /**
	 * 执行脚本文件
     * @param 文件路径path.
     * @return 文件内容（执行的每条命令）
     * @throws IOException 
	 */
    public ArrayList<String> batchFile(String path) throws IOException {
        File file = new File(path);
        ArrayList<String> commandList = new ArrayList<>();
        try {
            BufferedReader temp = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
            String command = null;
            //按行读取文件
            while ((command = temp.readLine()) != null) {
                commandList.add(command);
            }
            temp.close();
        } catch (Exception error) {
            error.printStackTrace();
        }
        return commandList;
    }
}
