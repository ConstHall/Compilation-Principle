package agenda;

import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

/**
 * UI类：用户交互界面
 * 
 */
public class UI {
	//服务类Service实例化
    private Service service;

    public UI() {
        service = new Service();
    }

    /**
	 * 打印帮助信息指南
	 */ 
    private void printHelp() {
        String[] helpInfo = new String[] {
            "------------------------------------------------------------------\n",
            "欢迎来到议程管理系统！\n",
            "以下为各种操作命令的输入格式，请严格按照标准进行输入（英文形式）:",
            "\n------------------------------------------------------------------\n",
            "register [userName] [password]",
            "add      [userName] [password] [other] [start] [end] [title]",
            "query    [userName] [password] [start] [end]",
            "delete   [userName] [password] [meetingId]",
            "clear    [userName] [password]",
            "batch    [fileName]",
            "help",
            "quit",
            "\n------------------------------------------------------------------\n",
            "请注意，若你删除某个会议，则该会议在组织者和参会者的会议列表中均会被删除！\n",
            "会议时间的输入格式为：2022-03-26|15:30",
            "\n------------------------------------------------------------------\n",
        };
        //遍历打印帮助信息表的每条信息
        for (String s:helpInfo)
            System.out.println(s);
    }
    
    /**
	 * 循环接收命令并输出帮助信息指南
     * @throws IOException 
	 */ 
    public void loop() throws IOException {
        Scanner scanner = new Scanner(System.in);
        //打印帮助信息
        printHelp();
        //循环接收用户输入的命令
        while (true) {
            System.out.print("$ ");
            String command= scanner.nextLine();
            if ( commandProcess(command) == -1)
                break;
        }
    }
    
    /**
	 * 对输入命令进行处理，调用对应的函数
     * @param 用户输入命令command
     * @throws IOException 
	 */ 
    private int commandProcess(String command) throws IOException {
        int[] commandLength = new int[] {3, 7, 5, 4, 3, 2, 1, 1};
        String[] commandType = new String[] {"register", "add", "query", "delete", "clear", "batch", "help", "quit"};
        //根据空格对指令进行拆分
        String [] commandList = command.split("\\s+");
        boolean flag = false;
        //若输入命令的长度（单词数量）不匹配该命令的长度，则报错
        try {
            for (int i = 0; i < 8; i++) {
                if (commandList[0].equals(commandType[i])) {
                    if (commandList.length != commandLength[i])
                        throw new Exception("Command Format Error");
                    flag = true;
                }
            }
            //若输入命令不符合任何一种命令，则报错
            if (!flag)
                throw new Exception("Command Format Error");
            //根据输入命令的第一个单词去调用不同的功能函数（为防止以外，先将第一个单词全部转成小写）
            switch (commandList[0].toLowerCase()) {
                case "register":
                    register(commandList);
                    break;
                case "add":
                    add(commandList);                    break;
                case "query":
                    query(commandList);
                    break;
                case "delete":
                    delete(commandList);
                    break;
                case "clear":
                    clear(commandList);
                    break;
                case "batch":
                    batch(commandList);
                    break;
                case "help":
                    printHelp();
                    break;
                case "quit":
                    return -1;
                default:
            }
        } catch (Exception e1) {
            System.out.println(e1.getException());
        } 
        return 0;
    }

    /**
	 * 注册函数register
     * @param 根据空格进行拆分后的指令数组command
	 */ 
    private void register(String[] command) {
    	//将用户名和密码传给注册函数
        service.userRegister(command[1], command[2]);
        System.out.println("注册成功！");
    }

    /**
	 * 议程添加函数add
     * @param 根据空格进行拆分后的指令数组command
	 */ 
    private void add(String[] command) {
    	//存储会议开始时间和结束时间
        Date start = new Date(command[4]);
        Date end = new Date(command[5]);
        //将会议组织者，密码，会议参与者，会议开始结束时间和会议标题传给会议添加函数addMeeting
        service.addMeeting(command[1], command[2], command[3],
                           start, end, command[6]);
        System.out.println("会议添加成功！");
    }

    /**
	 * 会议查询函数query
     * @param 根据空格进行拆分后的指令数组command
	 */ 
    private void query(String[] command) {
    	//存储会议开始时间和结束时间
        Date start = new Date(command[3]);
        Date end = new Date(command[4]);
        //将会议组织者，密码，会议开始结束时间传给会议查询函数queryMeeting
        ArrayList<Meeting> queryList = service.queryMeeting(command[1], command[2], start, end);
        System.out.println("该用户的的议程时间表如下所示：");
        for (Meeting meeting : queryList) {
            System.out.printf("开始时间： %s, 结束时间： %s, 会议主持人： %s, 会议参与者： %s, 会议标签： %s\n\n",
            meeting.getStartDate().DateToString(), meeting.getEndDate().DateToString(), meeting.getSponsor(), meeting.getParticipator(), meeting.getLabel());
        }
    }

    /**
	 * 会议删除函数delete
     * @param 根据空格进行拆分后的指令数组command
	 */ 
    private void delete(String[] command) {
    	//将会议组织者，密码，会议标签传给会议查询函数deleteMeeting
        service.deleteMeeting(command[1], command[2], command[3]);
        System.out.println("该会议已被删除！");
    }

    /**
	 * 会议清空函数clear
     * @param 根据空格进行拆分后的指令数组command
	 */ 
    private void clear(String[] command) {
    	//将用户名，密码传给会议清空函数clearMeeting
        service.clearMeeting(command[1], command[2]);
        System.out.println("该用户所有会议已被清空！");
    }

    /**
	 * 脚本函数batch
     * @param 根据空格进行拆分后的指令数组command
     * @throws IOException 
	 */ 
    private void batch(String[] command) throws IOException {
    	//将文件名传给脚本函数batchFile
        ArrayList<String> commandArray = service.batchFile(command[1]);
        System.out.println("------------------------------------------------------------------\n");
        for (String strCommand : commandArray) {
            System.out.printf("$ %s\n", strCommand);
            if ( commandProcess(strCommand) == -1)
                break;
        }
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("脚本执行完毕！");
    }
}
