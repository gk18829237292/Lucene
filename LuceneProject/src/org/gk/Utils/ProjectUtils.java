package org.gk.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ProjectUtils {
	
	public static boolean debug = true;
	
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY年MM月DD日,HH:mm:ss");
	
	/**
	 * debug Info
	 * @param info
	 */
	public static void logInfo(String info){
		if(debug)
			System.out.println(info);
	}
}
