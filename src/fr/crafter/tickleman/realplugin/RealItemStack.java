package fr.crafter.tickleman.realplugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

//############################################################################# class RealItemStack
public class RealItemStack extends RealItemType
{

	/**
	 * Amount of stored item (can be negative, greater than 64, no special limitation)
	 */
	private int amount;

	/**
	 * Damage code for item : 0 to 255
	 * Equals ItemStack.getDurability() for items that can be damaged
	 * Is 0 for non damaged on non-applicable items
	 */
	private short damage;

	private Map<Enchantment, Integer> enchantments;

	//######################################################################################## PUBLIC


	//--------------------------------------------------------------------------------- RealItemStack
	public RealItemStack(int theTypeId, int theAmount, short theDurabilityVariant, Map<Enchantment, Integer> theEnchantments) {
		super(theTypeId, theDurabilityVariant);
		this.enchantments = new HashMap<Enchantment, Integer>();
		this.setAmount(theAmount);
		this.setDamage(theDurabilityVariant);
		if (theEnchantments != null && !theEnchantments.isEmpty()) {
			Iterator<Entry<Enchantment, Integer>> enchantmentIterator = theEnchantments.entrySet().iterator();
			while (enchantmentIterator.hasNext()) {
				Entry<Enchantment, Integer> enchantment = enchantmentIterator.next();
				if (enchantment.getKey() != null && enchantment.getValue() != null) {
					this.enchantments.put(enchantment.getKey(), enchantment.getValue());
				}
			}
		}
	}
	
	//--------------------------------------------------------------------------------- RealItemStack
	public RealItemStack(RealItemStack theItemStack)
	{
		this(theItemStack.getTypeId(), theItemStack.getAmount(), theItemStack.getDurability(), theItemStack.getEnchantments());
	}
	
	//--------------------------------------------------------------------------------- RealItemStack
	public RealItemStack(ItemStack theItemStack)
	{
		this(theItemStack.getTypeId(), theItemStack.getAmount(), theItemStack.getDurability(), theItemStack.getEnchantments());
	}

	//--------------------------------------------------------------------------------- RealItemStack
	public RealItemStack(int theTypeId, int theAmount, short theDurabilityVariant)
	{
		this(theTypeId, theAmount, theDurabilityVariant, null);
	}

	//-------------------------------------------------------------------------------- cloneItemStack
	public static ItemStack cloneItemStack(ItemStack theItemStack)
	{
		ItemStack newItemStack = theItemStack.clone();
		newItemStack.getEnchantments().clear();
		if (theItemStack.getEnchantments() != null && !theItemStack.getEnchantments().isEmpty()) {
			newItemStack.addEnchantments(theItemStack.getEnchantments());
		}
		return newItemStack;
	}

	//------------------------------------------------------------------------------------- getAmount
	public int getAmount()
	{
		return this.amount;
	}

	//------------------------------------------------------------------------------------- getDamage
	public short getDamage()
	{
		return this.damage;
	}

	//--------------------------------------------------------------------------------- getDurability
	public short getDurability()
	{
		return RealItemType.typeIdHasDamage(this.getTypeId()) ? this.getDamage() : this.getVariant();
	}

	//--------------------------------------------------------------------------- getEnchantmentLevel
	public Integer getEnchantmentLevel(Enchantment enchantment)
	{
		return this.enchantments.get(enchantment);
	}

	//------------------------------------------------------------------------------- getEnchantments
	public Map<Enchantment, Integer> getEnchantments()
	{
		return this.enchantments;
	}

	//----------------------------------------------------------------------------------- getItemType
	public RealItemType getItemType()
	{
		return new RealItemType(this.getTypeId(), this.getVariant());
	}

	//------------------------------------------------------------------------------------- setAmount
	public void setAmount(int newAmount)
	{
		this.amount = newAmount;
	}
	
	//------------------------------------------------------------------------------------- incAmount
	public void addAmount(int amt)
	{
		this.amount += amt;
	}
	
	//------------------------------------------------------------------------------------- incAmount
	public void remAmount(int amt)
	{
		this.amount -= amt;
	}

	//------------------------------------------------------------------------------------- setDamage
	public void setDamage(short newDamage)
	{
		if (typeIdHasDamage(getTypeId())) {
			this.damage = newDamage;
		}
	}

	//------------------------------------------------------------------------------------- setTypeId
	@Override
	public void setTypeId(int newTypeId)
	{
		super.setTypeId(newTypeId);
		if (!RealItemType.typeIdHasDamage(newTypeId)) {
			this.damage = (short)0;
		}
	}

	//----------------------------------------------------------------------------------- toItemStack
	public ItemStack toItemStack()
	{
		ItemStack itemStack = new ItemStack(this.getTypeId(), this.getAmount(), this.getDamage());
		itemStack.addEnchantments(this.getEnchantments());
		return itemStack;
	}

	//--------------------------------------------------------------------------------- toNamedString
	@Override
	public String toNamedString()
	{
		String str = super.toNamedString();
		if (this.getDamage() > 0) {
			str += " (" + String.valueOf(this.getDamage()) + ")";
		}
		if (!this.getEnchantments().isEmpty()) {
			String ench = "";
			Iterator<Entry<Enchantment, Integer>> enIterator = this.getEnchantments().entrySet().iterator();
			while (enIterator.hasNext()) {
				Entry<Enchantment, Integer> eel = enIterator.next();
				if (!ench.isEmpty()) {
					ench += ", ";
				}
				ench += eel.getKey().getName() + " : " + String.valueOf(eel.getValue());
			}
			str += " [" + ench + "]";
		}
		str += " x " + String.valueOf(getAmount());
		return str;
	}

	//-------------------------------------------------------------------------------------- toString
	@Override
	public String toString()
	{
		String str = super.toString();
		if (this.getDamage() > 0) {
			str += "(" + String.valueOf(this.getDamage()) + ")";
		}
		if (!this.getEnchantments().isEmpty()) {
			String ench = "";
			Iterator<Entry<Enchantment, Integer>> enIterator = this.getEnchantments().entrySet().iterator();
			while (enIterator.hasNext()) {
				Entry<Enchantment, Integer> eel = enIterator.next();
				if (!ench.isEmpty()) {
					ench += ",";
				}
				ench += eel.getKey().toString() + ":" + String.valueOf(eel.getValue());
			}
			str += "[" + ench + "]";
		}
		str += "x" + String.valueOf(this.getAmount());
		return str;
	}
	
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RealItemStack)) {
            return false;
        }
        RealItemStack other = (RealItemStack)obj;
        if (this.getTypeId() == other.getTypeId() && this.getVariant() == other.getVariant() && this.getDamage() == other.getDamage()) {
        	if ((other.getEnchantments().isEmpty() && this.getEnchantments().isEmpty())) {
        		return true;
        	}
        	if ((other.getEnchantments().isEmpty() && !this.getEnchantments().isEmpty()) || (!other.getEnchantments().isEmpty() && this.getEnchantments().isEmpty())) {
        		return false;
        	}
        	Iterator<Entry<Enchantment, Integer>> oeIterator = other.getEnchantments().entrySet().iterator();
        	while (oeIterator.hasNext()) {
        		Entry<Enchantment, Integer> eel = oeIterator.next();
        		Integer tel = this.getEnchantments().get(eel.getKey());
       			if (tel == null || tel != eel.getValue()) {
       				return false;
       			}
        	}
        	return true;
        }
        return false;
    }
	
    @Override
    public int hashCode() {
        int hash = (super.hashCode() ^ this.getDamage() >>> 32);
        if (!this.getEnchantments().isEmpty()) {
			Iterator<Entry<Enchantment, Integer>> enIterator = this.getEnchantments().entrySet().iterator();
			while (enIterator.hasNext()) {
				Entry<Enchantment, Integer> eel = enIterator.next();
				hash = (hash ^ eel.getKey().getId() ^ eel.getValue() >>> 32);
			}
        }
        return hash;
    }

}
