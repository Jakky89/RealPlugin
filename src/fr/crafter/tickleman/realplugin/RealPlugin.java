package fr.crafter.tickleman.realplugin;

import org.bukkit.plugin.java.JavaPlugin;


//###################################################################################### RealPlugin
public class RealPlugin extends JavaPlugin
{

	private RealLog log;


	public RealPlugin() {
		log = null;
	}

	//--------------------------------------------------------------------------------- getRealLog
	public RealLog getLog() {
		if (log == null) {
			log = new RealLog(this);
		}
		return log;
	}
	
	@Override
	public void onEnable() {
		log = new RealLog(this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
}
