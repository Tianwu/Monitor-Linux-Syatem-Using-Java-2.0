import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;

/*
 * function:get the info<BufferedReader> by using Linux command
 */
public class LinuxCommand {
	private Process pro = null;
	private String command = null;
	
	public LinuxCommand() {
	}
	
	public LinuxCommand(String command) {
		this.command = command;
	}
	
	public BufferedReader getInfoReader() {
		
		try {
			Runtime r = Runtime.getRuntime();
			pro = r.exec(command);
			return new BufferedReader(new InputStreamReader(pro.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
	}
	public BufferedReader getInfoReader2() {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(command)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
	}

	public void processDestroy() {
		if(pro != null)
			pro.destroy();
	}
}
