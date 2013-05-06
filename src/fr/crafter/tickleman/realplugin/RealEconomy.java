package fr.crafter.tickleman.realplugin;

import java.text.DecimalFormat;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;

//##################################################################################### RealEconomy
public class RealEconomy
{
	private net.milkbowl.vault.economy.Economy vaultEconomy;

	//----------------------------------------------------------------------------------- RealEconomy
	public RealEconomy(Server theServer)
	{
		this.vaultEconomy = null;
		if (theServer.getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			vaultEconomy = economyProvider.getProvider();
			if (vaultEconomy != null) {
				theServer.getLogger().info("Using Vault as economy handler for " + vaultEconomy.getName());
			}
		}
	}

	//---------------------------------------------------------------------------------------- format
	public String format(Double amount)
	{
		if (amount != null) {
			if (vaultEconomy != null) {
				if (amount != 1)
					return new DecimalFormat("#.## " + vaultEconomy.currencyNamePlural()).format(amount);
				else
					return new DecimalFormat("#.## " + vaultEconomy.currencyNameSingular()).format(amount);
			}
			return new DecimalFormat("#.##").format(amount);
		}
		return "0.00";
	}

	//------------------------------------------------------------------------------------ getBalance
	public double getBalance(String playerName)
	{
		if (vaultEconomy != null) {
			return Math.round(vaultEconomy.getBalance(playerName) * 100) / 100;
		}
		return 0;
	}

	//------------------------------------------------------------------------------------ getBalance
	public String getBalance(String playerName, boolean withCurrency)
	{
		if (vaultEconomy != null) {
			Double balance = getBalance(playerName);
			if (withCurrency) {
				return this.format(balance);
			} else {
				return String.valueOf(balance.toString());
			}
		}
		return this.format((double)0);
	}

	//----------------------------------------------------------------------------------- getCurrency
	public String getCurrency()
	{
		if (vaultEconomy != null) {
			return vaultEconomy.currencyNamePlural();
		}
		return "?";
	}

	//------------------------------------------------------------------------------ getEconomyPlugin
	public Economy getVaultEconomy()
	{
		return vaultEconomy;
	}

	//------------------------------------------------------------------------------------ hasAccount
	public boolean hasAccount(String playerName)
	{
		if (vaultEconomy != null) {
			return vaultEconomy.hasAccount(playerName);
		}
		return false;
	}
	
	//------------------------------------------------------------------------------------ setBalance
	public boolean remBalance(String playerName, double amount)
	{
		if (vaultEconomy != null) {
			return vaultEconomy.withdrawPlayer(playerName, Math.abs(amount)).transactionSuccess();
		}
		return false;
	}
	
	//------------------------------------------------------------------------------------ setBalance
	public boolean addBalance(String playerName, double amount)
	{
		if (vaultEconomy != null) {
			if (amount > 0) {
				return vaultEconomy.depositPlayer(playerName, amount).transactionSuccess();
			} else if (amount < 0) {
				return remBalance(playerName, amount);
			}
		}
		return false;
	}

	//-------------------------------------------------------------------------------------- transfer
	public boolean transfer(String playerNameFrom, String playerNameTo, double amount)
	{
		if (remBalance(playerNameFrom, amount)) {
			if (addBalance(playerNameTo, amount)) {
				return true;
			} else {
				addBalance(playerNameFrom, amount);
			}
		}
		return false;
	}

}
