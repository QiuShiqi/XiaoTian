package Module;

import Module.Base.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.io.*;

import javax.swing.JOptionPane;

public class BaseModule {

	public static String projectName = "XiaoTian聊天工具";
	public static String version = "0.1 Alpha";
	public static DatabaseInfo databaseInfo = null;

	public static boolean Config() {
		String configFile;
		if(Main.debugMode){
			configFile = (new File("")).getAbsolutePath() + "\\src\\Config.ini";
		}else{
			configFile = (new File("")).getAbsolutePath() + "\\Config.ini";
		}
		
		if ((new File(configFile)).exists()) {
			try {
				FileInputStream file = new FileInputStream(configFile);
				Properties ini = new Properties();
				ini.load(file);

				String host = ini.get("host").toString();
				int port = Integer.parseInt(ini.get("port").toString());
				String userName = ini.get("username").toString();
				String password = ini.get("password").toString();
				BaseModule.databaseInfo = new DatabaseInfo(host, port, userName, password);

			} catch (Exception e) {
				return false;
			}

			return true;
		} else {
			return false;
		}

	}

	public static int stringToTime(String time) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		try {
			Date date = simpleDateFormat.parse(time);
			String tmp = String.valueOf(date.getTime());

			return Integer.parseInt(tmp.substring(0, 10));
		} catch (Exception e) {

		}

		return -1;
	}

	public static void messageBox(String content, int type) {
		String tmp = null;

		switch (type) {
		case -1:
			tmp = "错误";
			type++; // 0
			break;
		case 0:
			tmp = "警告";
			type += 2; // 2
			break;
		case 1:
			tmp = "提示";
			break;
		}

		JOptionPane.showMessageDialog(null, content, tmp + " - " + BaseModule.projectName, type); // JOptionPane.ERROR_MESSAGE
	}
	
	public static String imageLoader(String src){
		return "Images/" + src;
	}

	public static int getTime(){
		SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return BaseModule.stringToTime(tmp.format(new java.util.Date()));
	}
}
