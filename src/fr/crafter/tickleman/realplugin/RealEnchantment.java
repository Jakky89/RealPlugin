package fr.crafter.tickleman.realplugin;

import org.bukkit.enchantments.Enchantment;


//################################################################################# RealEnchantment
public class RealEnchantment
{

	//-------------------------------------------------------------------------- getEnchantmentWeight
	public static int getEnchantmentWeight(int enchantmentId)
	{
		switch (enchantmentId)
		{
			// Protection
			case 0:
			// Sharpness
			case 16:
			// Efficiency
			case 32:
			// Power
			case 48:
				return 10;
			// Fire Protection
			case 1:
			// Feather Falling
			case 2:
			// Projectile Protection
			case 4:
			// Smite
			case 17:
			// Bane of Arthropods
			case 18:
			// Knockback
			case 19:
			// Unbreaking
			case 34:
				return 5;
			// Blast Protection
			case 3:
			// Respiration
			case 5:
			// Aqua Affinity
			case 6:
			// Fire Aspect
			case 20:
			// Looting
			case 21:
			// Fortune
			case 35:
			// Punch
			case 49:
			// Flame
			case 50:
				return 2;
			// Thorns
			case 7:
			// Silk Touch
			case 33:
			// Infinity
			case 51:
				return 1;
		}
		return 0;
	}
	
	public static int getEnchantmentWeight(Enchantment enchantment)
	{
		return getEnchantmentWeight(enchantment.getId());
	}
	
	public static double getEnchantmentWeight(Enchantment enchantment, int level)
	{
		return (double)((double)getEnchantmentWeight(enchantment.getId()) * (double)((double)level / (double)enchantment.getMaxLevel()));
	}

}
