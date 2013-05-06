package fr.crafter.tickleman.realplugin;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;


//########################################################################################### Perms
public class RealPermissions
{

	private Permission vaultPermissions;

	//----------------------------------------------------------------------------------------- Perms
	public RealPermissions(Server theServer)
	{
		if (theServer != null && theServer.getPluginManager() != null && theServer.getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Permission> permissionsProvider = theServer.getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
			if (permissionsProvider != null) {
				vaultPermissions = permissionsProvider.getProvider();
				if (vaultPermissions != null) {
					theServer.getLogger().info("Vault Permissions Provider for " + vaultPermissions.getName() + " detected.");
				}
			} else {
				theServer.getLogger().warning("No Permissions Provider detected!");
			}
		}
	}

	//--------------------------------------------------------------------------------- hasPermission
	public boolean hasPermission(Player player, String permissionString)
	{
		boolean result = false;
		if (vaultPermissions != null) {
			result = vaultPermissions.has(player, permissionString);
			if (!result && !permissionString.contains(".*")) {
				result = hasPermission(player, permissionString + ".*");
				while (!result && permissionString.contains(".")) {
					permissionString = permissionString.substring(0, permissionString.lastIndexOf("."));
					result = hasPermission(player, permissionString + ".*");
				}
			}
		}
		return result;
	}
	
}
