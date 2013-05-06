package fr.crafter.tickleman.realplugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

//####################################################################################### FileTools
public class RealFileTools
{

	//------------------------------------------------------------------------------------ deleteFile
	public static void deleteFile(String fileName)
	{
		File file = new File(fileName);
		if (file.canWrite()) {
			file.delete();
		}
	}
	
	//---------------------------------------------------------------------------- extractDefaultFile
	public static void extractDefaultFile(JavaPlugin plugin, String filePath)
	{
		if (filePath == null)
			return;
		String[] split = filePath.split(File.separator);
		if (split.length <= 0)
			return;
		String fileName = split[split.length - 1];
		File actual = new File(filePath);
		if (!actual.exists()) {
			InputStream input = null;
			FileOutputStream output = null;
			plugin.getServer().getLogger().log(Level.INFO, "Create default file " + fileName + " for " + filePath);
			try {
				input = plugin.getClass().getResourceAsStream("/default/" + fileName);
				output = new FileOutputStream(actual);
				byte[] buf = new byte[8192];
				int length = 0;
				while ((length = input.read(buf)) > 0) {
					output.write(buf, 0, length);
				}
				plugin.getServer().getLogger().log(Level.INFO, "Default file written " + filePath);
			} catch (Exception e) {
				plugin.getServer().getLogger().log(Level.WARNING, "Default file could not be written: " + e.getMessage());
			} finally {
				if (input != null) {
					try {
						input.close(); 
					} catch (Exception e) {}
				}
				if (output != null) {
					try {
						output.close();
					} catch (Exception e) {}
				}
			}
		}
	}

	//----------------------------------------------------------------------------------------- mkDir
	public static void mkDir(String dirName)
	{
		File dir = new File(dirName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	//------------------------------------------------------------------------------ readFullTextFile
	public static String readFullTextFile(String fileName)
	{
		StringBuffer sb = new StringBuffer(10240);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			char[] chars = new char[10240];
			while (reader.read(chars) > -1) {
				sb.append(String.valueOf(chars));	
			}
			return sb.toString();
		} catch (Exception e) {
			System.out.println("Content could not be read from " + fileName + ": " + e.getMessage());
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {}
			}
		}
	}

	//------------------------------------------------------------------------------------ renameFile
	public static void renameFile(String fromFile, String toFile)
	{
		File from = new File(fromFile);
		File to = new File(toFile);
		if (from.exists() && !to.exists()) {
			from.renameTo(to);
		}
	}

	//-------------------------------------------------------------------------------- setFileContent
	public static void setFileContent(String fileName, String content)
	{
		if (fileName != null && !fileName.isEmpty() && content != null) {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(fileName, false));
				writer.write(content);
				writer.flush();
			} catch (Exception e) {
				System.out.println("Content could not be written to \"" + fileName + "\": " + e.getMessage());
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (Exception e) {}
				}
			}
		}
	}

}
