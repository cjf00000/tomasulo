package tomasulo;

public class Logger {
	
	public static void Info(String message)
	{
		System.out.println("Info: " + message);
	}
	
	public static void Debug(String message)
	{
		System.out.println("Debug: " + message);
	}
	
	public static void Fatal(String message)
	{
		System.out.println("Fatal: " + message);
		
		System.exit(1);
	}
}
