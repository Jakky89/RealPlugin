package fr.crafter.tickleman.realplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


//##################################################################################### Translation
public class RealTranslation
{
	private static Map<String, String> translations = new HashMap<String, String>();

	
	// ---------------------------------------------------------------------------------- Translation
	public RealTranslation(RealPlugin plugin)
	{
		RealTranslation.load(plugin, "en");
	}

	// ---------------------------------------------------------------------------------- Translation
	public RealTranslation(RealPlugin plugin, String languageCode)
	{
		RealTranslation.load(plugin, languageCode);
	}
	
	// ----------------------------------------------------------------------------------------- clear
	public static void clear() {
		translations.clear();
	}

	// ----------------------------------------------------------------------------------------- load
	public static void load(RealPlugin plugin, String languageCode)
	{
		plugin.getLog().info("Loading translation \"" + languageCode + "\" ...", true);
		String filePath = plugin.getDataFolder().getPath() + File.separator + languageCode + ".lang.txt";
		File trFile = new File(filePath);
		if (!trFile.exists()) {
			RealFileTools.extractDefaultFile(plugin, filePath);
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(trFile));
			String buffer;
			while ((buffer = reader.readLine()) != null) {
				buffer = buffer.trim();
				buffer = buffer.replaceAll("(\\r|\\n)", "");
				if (!buffer.isEmpty() && buffer.charAt(0) != '#') {
					String[] line = buffer.split("=");
					if (line.length >= 2) {
						String key = line[0].trim();
						String value = line[1].trim();
						if (!key.isEmpty() && !value.isEmpty()) {
							translations.put(key, value);
						}
					}
				}
			}
			plugin.getLog().info(String.valueOf(translations.size()) + " translation items loaded.", true);
		} catch (Exception e) {
			if (filePath.contains(File.separator + "en.lang.txt")) {
				plugin.getLog().info("You can create " + filePath + " to change texts", true);
			} else {
				plugin.getLog().warning("Missing " + filePath + " (check your language configuration option)", true);
			}
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {};
			}
		}
	}
	
	// ------------------------------------------------------------------------------------------- tr
	public static String tr(String text)
	{
		if ((text.length() > 0) && (text.charAt(0) == '#')) {
			text = text.substring(1);
		}
		String translated = translations.get(text);
		if ((translated == null) || (translated.equals(""))) {
			return text;
		} else {
			return translated;
		}
	}

	//------------------------------------------------------------------------------------ trItemName
	/**
	 * Get item name from translation file entry looking like 35:14=Red wool
	 * If there is no translation file entry, then try to generate item name
	 * from realItemType.getName() translation
	 *
	 * @param RealItemType itemType
	 * @return String
	 */
	public static String trItemName(RealItemType itemType)
	{
		return trItemName(itemType.toString());
	}

	//------------------------------------------------------------------------------------ trItemName
	/**
	 * Get item name from translation file entry looking like 35:14=Red wool
	 * If there is no translation file entry, then try to generate item name
	 * from realItemType.getName() translation
	 *
	 * @param String typeIdVariant
	 * @return String
	 */
	public static String trItemName(String typeIdVariant)
	{
		String itemName = tr(typeIdVariant);
		if (itemName.equals(typeIdVariant)) {
			itemName = tr(RealItemType.parseItemType(typeIdVariant).getName());
		}
		return itemName;
	}

}
