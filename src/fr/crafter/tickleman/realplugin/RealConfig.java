package fr.crafter.tickleman.realplugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;


//###################################################################################### RealConfig
public class RealConfig
{
	private String configFilePath;
	private RealPlugin plugin;
	
	private Map<String, String> configComments;
	private Map<String, String> configParams;

	private void initBase(RealPlugin thePlugin, String theFileName) {
		this.plugin = thePlugin;
		this.configParams = new HashMap<String, String>();
		this.configComments = new HashMap<String, String>();
		this.configFilePath = this.plugin.getDataFolder().getPath() + File.separator + theFileName + ".txt";
	}
	
	//---------------------------------------------------------------------------------------- Config
	public RealConfig(RealPlugin thePlugin)
	{
		this.initBase(thePlugin, "config");
		this.load();
	}
	
	//---------------------------------------------------------------------------------------- Config
	public RealConfig(RealPlugin thePlugin, RealConfig theParentConfig)
	{
		this.initBase(thePlugin, "config");
		if (theParentConfig != null && !theParentConfig.getConfigParams().isEmpty()) {
			this.configParams.putAll(theParentConfig.getConfigParams());
			if (!theParentConfig.getConfigComments().isEmpty()) {
				this.configComments.putAll(theParentConfig.getConfigComments());
			}
		}
		this.load();
	}
	
	//---------------------------------------------------------------------------------------- Config
	public RealConfig(RealPlugin thePlugin, String theFileName)
	{
		this.initBase(thePlugin, theFileName);
		this.load();
	}
	
	//---------------------------------------------------------------------------------------- Config
	public RealConfig(RealPlugin thePlugin, String theFileName, RealConfig theParentConfig)
	{
		this.initBase(thePlugin, theFileName);
		if (theParentConfig != null && !theParentConfig.getConfigParams().isEmpty()) {
			this.configParams.putAll(theParentConfig.getConfigParams());
			if (!theParentConfig.getConfigComments().isEmpty()) {
				this.configComments.putAll(theParentConfig.getConfigComments());
			}
		}
		this.load();
	}

	public Map<String, String> getConfigParams() {
		return this.configParams;
	}
	
	public Map<String, String> getConfigComments() {
		return this.configComments;
	}

	//------------------------------------------------------------------------------------------ load
	public boolean load()
	{
		File configFile = new File(this.configFilePath);
		if (configFile.exists()) {
			if (configFile.canRead()) {
				BufferedReader reader = null;
				String key = "";
				String value = null;
				try {
					reader = new BufferedReader(new FileReader(configFile));
					String fline;
					String cline = "";
					while ((fline = reader.readLine()) != null) {
						fline = fline.trim().replaceAll("(\\r|\\n)", "");
						if (!fline.isEmpty() && fline.charAt(0) != '#') {
							int assignPos = fline.indexOf("=");
							if (assignPos > 0) {
								key = fline.substring(0, assignPos).trim();
								value = fline.substring(assignPos+1).trim();
								this.configParams.put(key, value);
								if (!cline.isEmpty()) {
									this.configComments.put(key, cline);
									cline = "";
								}
							}
						} else {
							if (cline.isEmpty()) {
								cline = fline;
							} else {
								cline += "\n" + fline;
							}
						}
					}
					if (!cline.isEmpty()) {
						this.configComments.put("", cline);
					}
					return true;
				} catch (Exception e) {
					this.plugin.getLogger().log(Level.SEVERE, "Exception occurred while parsing configuration file from \"" + this.configFilePath + "\": " + e.getMessage());
					return false;
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (Exception e) {}
					}
				}
			} else {
				this.plugin.getLogger().log(Level.SEVERE, "Configuration file \"" + this.configFilePath + "\" is not readable!");
			}
		} else {
			RealFileTools.extractDefaultFile(this.plugin, this.configFilePath);
		}
		return false;
	}

	//------------------------------------------------------------------------------------------ save
	public boolean save()
	{
		File configFile = new File(this.configFilePath);
		if (configFile.canWrite() || (!configFile.exists() && configFile.getParentFile().canWrite())) {
			BufferedWriter writer = null;
			String commentStr;
			try {
				writer = new BufferedWriter(new FileWriter(configFile));
				if (!this.getConfigParams().isEmpty()) {
					Iterator<Entry<String, String>> cfgp = this.getConfigParams().entrySet().iterator();
					while (cfgp.hasNext()) {
						Entry<String, String> cfge = cfgp.next();
						commentStr = this.getConfigComments().get(cfge.getKey());
						if (commentStr != null) {
							writer.write(commentStr);
							writer.newLine();
						}
						writer.write(cfge.getKey() + "=" + cfge.getValue().toString());
						writer.newLine();
					}
				}
				commentStr = this.getConfigComments().get("");
				if (commentStr != null) {
					writer.write(commentStr);
					writer.newLine();
				}
				writer.flush();
				return true;
			} catch (Exception e) {
				this.plugin.getLogger().log(Level.SEVERE, "Exception occurred while saving configuration file to \"" + this.configFilePath + "\": " + e.getMessage());
				return false;
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (Exception e) {}
				}
			}
		} else {
			this.plugin.getLogger().log(Level.SEVERE, "Can not write configuration to \"" + this.configFilePath + "\"! Is it writeable?");
		}
		return false;
	}
	
	public String getValue(String cfgKey, String def) {
		String val = this.configParams.get(cfgKey);
		if (val != null) {
			return val;
		} else {
			this.setValue(cfgKey, def);
		}
		return def;
	}
	
	public void setValue(String cfgKey, String cfgVal) {
		this.configParams.put(cfgKey, cfgVal);
	}
	
	public String getStringValue(String cfgKey, String def) {
		return getValue(cfgKey, def);
	}
	
	public Integer getIntegerValue(String cfgKey, Integer def) {
		String sval = this.configParams.get(cfgKey);
		if (sval != null) {
			return RealVarTools.parseInt(sval, def);
		} else {
			this.setValue(cfgKey, String.valueOf(def));
		}
		return def;
	}
	
	public Double getDoubleValue(String cfgKey, Double def) {
		String sval = this.configParams.get(cfgKey);
		if (sval != null) {
			return RealVarTools.parseDouble(sval, def);
		} else {
			this.setValue(cfgKey, String.valueOf(def));
		}
		return def;
	}
	
	public Boolean getBooleanValue(String cfgKey, Boolean def) {
		String sval = this.configParams.get(cfgKey);
		if (sval != null) {
			return RealVarTools.parseBoolean(sval, def);
		} else {
			this.setValue(cfgKey, String.valueOf(def));
		}
		return def;
	}

}
