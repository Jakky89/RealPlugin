package fr.crafter.tickleman.realplugin;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

//################################################################################ RealPluginPlugin
public class RealPluginPlugin extends RealPlugin
{

	//------------------------------------------------------------------------------------- onCommand
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (sender.isOp() || (sender instanceof ConsoleCommandSender)) {
			String command = cmd.getName().toLowerCase();
			if (args.length >= 1) {
				if (command.equals("realrecipes") || command.equals("rer") || command.equals("rr")) {
					if (args[0].equals("dump") || args[0].equals("d")) {
						if (args.length >= 2) {
							sender.sendMessage("Dumping recipes on console for item " + args[1]);
							RealRecipe.dumpRecipes(this.getServer(), Integer.parseInt(args[1]));
						} else {
							sender.sendMessage("Dumping all recipes on console");
							RealRecipe.dumpRecipes(this.getServer(), null);
						}
						return true;
					}
				} else if (command.equals("realplugin") || command.equals("relp") ||  command.equals("rp")) {
					if (args.length >= 2) {
						Plugin plugin = getServer().getPluginManager().getPlugin(args[1]);
						if (args[0].equals("load") || args[0].equals("l")) {
							if (plugin == null) {
								try {
									getServer().getPluginManager().loadPlugin(new File("plugins" + File.separator + args[1]));
									sender.sendMessage("Plugin " + args[1] + " has been loaded.");
								} catch (UnknownDependencyException e) {
									sender.sendMessage("Plugin " + args[1] + " can't be loaded : unknown dependency : " + e.getMessage());
								} catch (InvalidPluginException e) {
									sender.sendMessage("Plugin " + args[1] + " can't be loaded : Invalid plugin : " + e.getMessage());
								} catch (InvalidDescriptionException e) {
									sender.sendMessage("Plugin " + args[1] + " can't be loaded : Invalid description : " + e.getMessage());
								} catch (Exception e) {
									sender.sendMessage("Plugin " + args[1] + " can't be loaded : Exception occurred : " + e.getMessage());
								}
								plugin = getServer().getPluginManager().getPlugin(args[1]);
							}
							if (plugin != null) {
								if (!plugin.isEnabled()) {
									getServer().getPluginManager().enablePlugin(plugin);
									sender.sendMessage("Plugin " + plugin.getName() + " has been enabled.");
								} else {
									sender.sendMessage("Plugin " + plugin.getName() + " is already enabled.");
								}
							} else {
								sender.sendMessage("Plugin " + args[1] + " not found.");
							}
							return true;
						} else if (args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("d")) {
							if (plugin != null) {
								if (plugin.isEnabled()) {
									getServer().getPluginManager().disablePlugin(plugin);
									sender.sendMessage("Plugin " + plugin.getName() + " has been disabled.");
								} else {
									sender.sendMessage("Plugin " + plugin.getName() + " is already disabled.");
								}
							} else {
								sender.sendMessage("Plugin " + args[1] + " not found.");
							}
							return true;
						} else if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("e")) {
							if (plugin != null) {
								if (!plugin.isEnabled()) {
									getServer().getPluginManager().enablePlugin(plugin);
									sender.sendMessage("Plugin " + plugin.getName() + " has been enabled.");
								} else {
									sender.sendMessage("Plugin " + plugin.getName() + " is already enabled.");
								}
							} else {
								sender.sendMessage("Plugin " + args[1] + " not found.");
							}
							return true;
						} else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
							if (plugin != null) {
								if (plugin.isEnabled()) {
									getServer().getPluginManager().disablePlugin(plugin);
									sender.sendMessage("Plugin " + plugin.getName() + " has been disabled.");
								}
								try {
									getServer().getPluginManager().loadPlugin(new File("plugins/" + plugin.getName() + ".jar"));
									sender.sendMessage("Plugin " + args[1] + " has been loaded.");
								} catch (UnknownDependencyException e) {
									sender.sendMessage("Plugin " + args[1] + " can't be loaded : unknown dependency " + e.getMessage());
									e.printStackTrace();
								} catch (InvalidPluginException e) {
									sender.sendMessage("Plugin " + args[1] + " can't be loaded : Invalid plugin " + e.getMessage());
									e.printStackTrace();
								} catch (InvalidDescriptionException e) {
									sender.sendMessage("Plugin " + args[1] + " can't be loaded : Invalid description " + e.getMessage());
									e.printStackTrace();
								}
								plugin = getServer().getPluginManager().getPlugin(args[1]);
								getServer().getPluginManager().enablePlugin(plugin);
								sender.sendMessage("Plugin " + plugin.getName() + " has been enabled.");
							} else {
								sender.sendMessage("Plugin " + args[1] + " not found.");
							}
							return true;
						}
					} else if (args.length == 1) {
						// /realplugin help
						if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
							String[] help = {
									ChatColor.YELLOW + "/rp r PluginName" + ChatColor.WHITE + " : reload any plugin",
									ChatColor.YELLOW + "/rp d PluginName" + ChatColor.WHITE + " : disable any plugin",
									ChatColor.YELLOW + "/rp e PluginName" + ChatColor.WHITE + " : enable any plugin",
							};
							for (String h : help) {
								sender.sendMessage(h);
							}
							return true;
						}
					} else {
						StringBuilder list = new StringBuilder("Plugins list: ");
						for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
							if (plugin.isEnabled()) {
								list.append(ChatColor.DARK_GREEN + plugin.getName() + " ");
							} else {
								list.append(ChatColor.RED + plugin.getName() + " ");
							}
						}
						sender.sendMessage(list.toString());
						return true;
					}
				}
			}
		}
		return false;
	}

}
