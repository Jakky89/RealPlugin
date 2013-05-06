package fr.crafter.tickleman.realplugin;

import org.bukkit.ChatColor;

//####################################################################################### RealColor
public class RealColor
{

	public static String cancel   = ChatColor.RED.toString();
	public static String command  = ChatColor.AQUA.toString();
	public static String doc      = ChatColor.BLUE.toString();
	public static String item     = ChatColor.AQUA.toString();
	public static String message  = ChatColor.GREEN.toString();
	public static String player   = ChatColor.GOLD.toString();
	public static String price    = ChatColor.YELLOW.toString();
	public static String quantity = ChatColor.AQUA.toString();
	public static String shop     = ChatColor.LIGHT_PURPLE.toString();
	public static String text     = ChatColor.GREEN.toString();

	
	public static String stripColorCode(String str) {
		return ChatColor.stripColor(str);
	}
	
}
