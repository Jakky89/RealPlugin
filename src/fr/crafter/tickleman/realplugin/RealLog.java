package fr.crafter.tickleman.realplugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;


//############################################################################################# Log
public class RealLog
{

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy- HH:mm:ss");

	private Logger           globalLog;
	private File           	 logFile;
	private RealPlugin       plugin;
	private boolean          debugMode;
	private boolean          pluginLog;

	//------------------------------------------------------------------------------------------- Log
	public RealLog(RealPlugin thePlugin, boolean enablePluginLog, boolean enableDebugMode)
	{
		this.plugin = thePlugin;
		this.globalLog = Logger.getLogger("Minecraft");
		this.logFile = new File(plugin.getDataFolder().getPath() + File.separator + plugin.getDescription().getName().toLowerCase() + ".log");
		setDebugMode(enableDebugMode);
		setPluginLog(enablePluginLog);
	}

	//------------------------------------------------------------------------------------------- Log
	public RealLog(RealPlugin thePlugin, boolean enableDebugMode)
	{
		this(thePlugin, true, enableDebugMode);
	}

	//------------------------------------------------------------------------------------------- Log
	public RealLog(RealPlugin thePlugin)
	{
		this(thePlugin, true, false);
	}

	//------------------------------------------------------------------------------------------ date
	private String date()
	{
		return this.dateFormat.format(new Date());
	}

	//----------------------------------------------------------------------------------------- debug
	public void debug(String text)
	{
		if (this.debugMode) {
			this.log("DEBUG", text);
		}
	}

	//----------------------------------------------------------------------------------------- debug
	public void debug(String text, boolean global)
	{
		if (this.debugMode) {
			this.log("DEBUG", text, global);
		}
	}

	//----------------------------------------------------------------------------------------- error
	public void error(String text)
	{
		this.log("ERROR", text);
	}

	//----------------------------------------------------------------------------------------- error
	public void error(String text, boolean global)
	{
		this.log("ERROR", text, global);
	}

	//------------------------------------------------------------------------------------------ info
	public void info(String text)
	{
		this.log("INFO", text);
	}

	//------------------------------------------------------------------------------------------ info
	public void info(String text, boolean global)
	{
		this.log("INFO", text, global);
	}

	//------------------------------------------------------------------------------------------- log
	private void log(String mark, String text)
	{
		if (this.pluginLog) {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(this.logFile, true));
				writer.write(date() + " [" + mark + "] " + text);
				writer.newLine();
				writer.flush();
			} catch (Exception e) {
				this.globalLog.severe("[" + this.plugin.getDescription().getName() + "] Could not write into log file " + logFile + " : [" + mark + "] " + text);
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (Exception e) {}
				}
			}
		} else {
			this.log(mark, text, true);
		}
	}

	//------------------------------------------------------------------------------------------- log
	private void log(String mark, String text, boolean global)
	{
		if (this.pluginLog) {
			this.log(mark, text);
		}
		if (global || !this.pluginLog) {
			if (this.globalLog != null) {
				if (mark.equals("INFO")) {
					this.globalLog.info("[" + plugin.getDescription().getName() + "] " + text);
				} else if (mark.equals("WARNING")) {
					this.globalLog.warning("[" + plugin.getDescription().getName() + "] " + text);
				} else if (mark.equals("SEVERE")) {
					this.globalLog.severe("[" + plugin.getDescription().getName() + "] " + text);
				} else if (mark.equals("ERROR")) {
					this.globalLog.info("[ERROR] [" + plugin.getDescription().getName() + "] " + text);
				} else if (mark.equals("DEBUG")) {
					this.globalLog.info("[DEBUG] [" + plugin.getDescription().getName() + "] " + text);
				}
			}
		}
	}

	//---------------------------------------------------------------------------------- setDebugMode
	public void setDebugMode(boolean debugMode)
	{
		this.debugMode = debugMode;
		if (this.globalLog != null) {
			if (this.debugMode) {
				this.globalLog.info("Enabled DEBUG mode for " + plugin.getDescription().getName());
			} else {
				this.globalLog.info("Disabled DEBUG mode for " + plugin.getDescription().getName());
			}
		}
	}

	//---------------------------------------------------------------------------------- setPluginLog
	public void setPluginLog(boolean pluginLog)
	{
		this.pluginLog = pluginLog;
		if (this.globalLog != null) {
			if (this.pluginLog) {
				this.globalLog.info("Enabled PLUGIN LOG for " + plugin.getDescription().getName());
			} else {
				this.globalLog.info("Disabled PLUGIN LOG for " + plugin.getDescription().getName());
			}
		}
	}

	//---------------------------------------------------------------------------------------- severe
	public void severe(String text)
	{
		this.log("SEVERE", text, true);
	}

	//---------------------------------------------------------------------------------------- severe
	public void severe(String text, boolean global)
	{
		this.log("SEVERE", text, global);
	}

	//--------------------------------------------------------------------------------------- warning
	public void warning(String text)
	{
		this.log("WARNING", text);
	}

	//--------------------------------------------------------------------------------------- warning
	public void warning(String text, boolean global)
	{
		this.log("WARNING", text, global);
	}

}
