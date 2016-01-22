package com.accusoft.tests.ocs.common.utils;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.accusoft.tests.ocs.common.utils.OsUtilities.Kernel32;
import com.accusoft.tests.ocs.common.utils.OsUtilities.ShellExecutor;
import com.sun.jna.Platform;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class PrizmUtils {
	

    public static final Logger LOGGER = Logger.getLogger(PrizmUtils.class);

    private static String getStopPrizmCommand(){
        if (OsUtilities.isLinux()) return "/usr/share/prizm/scripts/pccis.sh stop";
        return "net stop Prizm";
    }

    private static String getStartPrizmCommand(){
        if (OsUtilities.isLinux()) return "/usr/share/prizm/scripts/pccis.sh start";
        return "net start Prizm";
    }

    public static void stopPrizm() {
        ShellExecutor.runCommand(getStopPrizmCommand());
        String stopProxyServer = "/usr/share/prizm/scripts/proxyserver.sh stop";
        if (OsUtilities.isLinux()) ShellExecutor.runCommand(stopProxyServer);
        LOGGER.info("Prizm services stopped successfully");
    }

    public static void startPrizm() {
        ShellExecutor.runCommand(getStartPrizmCommand());
        String startProxyServer = "/usr/share/prizm/scripts/proxyserver.sh start";
        if (OsUtilities.isLinux()) ShellExecutor.runCommand(startProxyServer);
        LOGGER.info("Prizm services started successfully");
    }

    public static void copyConfigFile(String testConfigFilePath, String configFileName){
        String targetFilePath = "";
        if (OsUtilities.isWindows()) targetFilePath = System.getProperty("prizm.path.windows");
        if (OsUtilities.isLinux()) targetFilePath = System.getProperty("prizm.path.linux");

        File source = new File(testConfigFilePath);
        File target = new File(targetFilePath+configFileName);

        try {
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            LOGGER.debug("Copying config file from "+testConfigFilePath+" to "+targetFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	/**
	 * Start vector conversion service.
	 */
	public static Process startVectorConversionService() {

		System.out.println("Starting Vector Conversion Service");

		Process p = null;

		String vcsFolderPath = OsUtilities.getResourcePath("vcs");
		String vcsCmdPath = OsUtilities.getResourcePath("vcs/VectorConversionService.exe");
		String vcsCfgPath = OsUtilities.getResourcePath("vcs/VectorConversionService.Windows.config");

		String cmdFileContent = "\"" + vcsCmdPath + "\" -r console -c " + "\""
				+ vcsCfgPath + "\"";

		File file = new File(vcsFolderPath + File.separator + "start.bat");

		try {

			if (file.exists()) {
				file.delete();
			}

			file.createNewFile();

			FileUtils.writeStringToFile(file, cmdFileContent);

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			p = Runtime.getRuntime().exec(
					"cmd /C \"" + file.getAbsolutePath() + "\"");
			
		} catch (ExecuteException e) {
			// ignore
		} catch (IOException e) {
			System.out.println("VectorConversionService is terminated "
					+ e.getMessage());
		}

		System.out.println("Vector Conversion Service STARTED");

		return p;
	}

	/**
	 * Stop vector conversion service.
	 */
	public static void stopVectorConversionService(Process vcsProcess) {

		try {
			
			int pid = getPid(vcsProcess);
			
			 
			 Runtime.getRuntime().exec(
					 "taskkill /F /T /PID " + pid);
			 
		} catch (Exception ex) {
			// ignore;
		}

		vcsProcess = null;

		System.out.println("Vector Conversion Service STOPPED");
	}
	
	public static int getPid(Process p) {
		Field f;

		if (Platform.isWindows()) {
			try {
				f = p.getClass().getDeclaredField("handle");
				f.setAccessible(true);
				int pid = Kernel32.INSTANCE.GetProcessId((Long) f.get(p));
				return pid;
			} catch (Exception ex) {
				LOGGER.error(ex);
			}
		} else if (Platform.isLinux()) {
			try {
				f = p.getClass().getDeclaredField("pid");
				f.setAccessible(true);
				int pid = (Integer) f.get(p);
				return pid;
			} catch (Exception ex) {
				LOGGER.error(ex);
			}
		} else {
		}
		return 0;
	}
}
