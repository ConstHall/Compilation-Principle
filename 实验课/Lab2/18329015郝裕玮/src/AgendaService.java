/**
 * AgendaService类：议程管理系统主程序
 */
import java.io.IOException;

import agenda.*;

public class AgendaService {
	
    public static void main(String[] args) throws IOException {
        UI AgendaUI = new UI();
        AgendaUI.loop();
    }
    
}
