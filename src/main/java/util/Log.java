package util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
	
	private static Logger logger;
	private static FileHandler fh;  

	public static void init() {
			try {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
			Calendar cal = Calendar.getInstance();
			String fileName = "log " + dateFormat.format(cal.getTime()) + ".txt";
			
			File f = new File("logs");
			if(!f.exists())
				f.mkdir();
			
			fh = new FileHandler("logs/" + fileName);
			
			logger = Logger.getLogger("MyLog"); 
			logger.addHandler(fh);  
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  
	        
	        //Comment next line to make logger put into file everything from level INFO
	        //Now it will log only errors (severe)
	        logger.setLevel(Level.WARNING);
	        
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	} 
	
	public static void info(String text)
	{
		if (logger == null)
			Log.init();
		
		logger.info(text);
	}
	
	public static void warning(String text)
	{
		if (logger == null)
			Log.init();
		
		logger.warning(text);
	}
	
	public static void fine(String text)
	{
		if (logger == null)
			Log.init();
		
		logger.fine(text);
	}
	
	public static void severe(String text)
	{
		if (logger == null)
			Log.init();
		
		logger.severe(text);
	}

	public static void setLevel(Level level)
	{
		if (logger == null)
			Log.init();
		
		logger.setLevel(level);
	}
	
	public static void clearLogs()
	{
		if (logger == null)
			Log.init();
		
		for(File f: (new File("logs")).listFiles())
		    if(f.getName().startsWith("log"))
		        f.delete();
	}
	
}
