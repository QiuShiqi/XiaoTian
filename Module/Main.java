package Module;

import Module.Form.*;

public class Main {

	public static boolean debugMode = false;

	public static void main(String[] args) throws Exception {

		//运行状态
		if(args.length > 1){
			switch (Integer.parseInt(args[1])) {
				case 0:
					Main.debugMode = true;
					break;
			}
		}
		
		//程序入口
		if(args.length > 0){
			//运行类型
			switch (Integer.parseInt(args[0])) {
				case 0:
					new ServerLoginForm();
					break;
				case 1:
					new ClientLoginForm();
					break;
				default:
					System.out.println("调用程序入口失败！");
			}
		}

	}

}
