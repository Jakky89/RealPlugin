package fr.crafter.tickleman.realplugin;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RealItemStackList
{
	List<RealItemStack> itemStacks;
	
	
	public RealItemStackList()
	{
		itemStacks = new LinkedList<RealItemStack>();
	}

	public int add(RealItemStack itemStack)
	{
		if (itemStack != null) {
			if (itemStack.getAmount() != 0) {
				if (this.itemStacks.isEmpty()) {
					this.itemStacks.add((RealItemStack)new RealItemStack(itemStack));
				} else {
					Iterator<RealItemStack> listIterator = this.itemStacks.iterator();
					while (listIterator.hasNext()) {
						RealItemStack its = listIterator.next();
						if (its.equals(itemStack)) {
							its.addAmount(itemStack.getAmount());
							return its.getAmount();
						}
					}
				}
			}
			return this.getAmount(itemStack);
		}
		return 0;
	}
	
	public int add(ItemStack itemStack)
	{
		if (itemStack != null) {
			return this.add(new RealItemStack(itemStack));
		}
		return 0;
	}
	
	public void add(ItemStack[] itemStacks, boolean ignoreAir)
	{
		if (itemStacks != null && itemStacks.length > 0) {
			for (ItemStack stk : itemStacks) {
				if (!stk.getType().equals(Material.AIR) || !ignoreAir) {
					this.add(stk);
				}
			}
		}
	}
	
	public void add(Collection<ItemStack> itemStacks, boolean ignoreAir)
	{
		if (itemStacks != null && itemStacks.size() > 0) {
			Iterator<ItemStack> isi = itemStacks.iterator();
			while (isi.hasNext()) {
				ItemStack stk = isi.next();
				if (!stk.getType().equals(Material.AIR) || !ignoreAir) {
					this.add(stk);
				}
			}
		}
	}
	
	public int rem(RealItemStack itemStack)
	{
		if (itemStack != null) {
			if (itemStack.getAmount() != 0) {
				if (this.itemStacks.isEmpty()) {
					RealItemStack snew = new RealItemStack(itemStack);
					snew.setAmount(-snew.getAmount());
					this.itemStacks.add(snew);
				} else {
					Iterator<RealItemStack> listIterator = this.itemStacks.iterator();
					while (listIterator.hasNext()) {
						RealItemStack its = listIterator.next();
						if (its.equals(itemStack)) {
							if (its.getAmount() == itemStack.getAmount()) {
								listIterator.remove();
								return 0;
							} else {
								its.remAmount(itemStack.getAmount());
								return its.getAmount();
							}
						}
					}
				}
			}
			return this.getAmount(itemStack);
		}
		return 0;
	}
	
	public int rem(ItemStack itemStack)
	{
		if (itemStack != null) {
			return this.rem(new RealItemStack(itemStack));
		}
		return 0;
	}
	
	public void rem(ItemStack[] itemStack)
	{
		if (itemStack != null && itemStack.length > 0) {
			for (ItemStack stk : itemStack) {
				this.rem(stk);
			}
		}
	}
	
	public void rem(Collection<ItemStack> itemStacks)
	{
		if (itemStacks != null && itemStacks.size() > 0) {
			Iterator<ItemStack> isi = itemStacks.iterator();
			while (isi.hasNext()) {
				this.rem(isi.next());
			}
		}
	}
	
	public int getAmount(RealItemStack itemStack)
	{
		if (itemStack != null) {
			Iterator<RealItemStack> listIterator = this.itemStacks.iterator();
			while (listIterator.hasNext()) {
				RealItemStack its = listIterator.next();
				if (its.equals(itemStack)) {
					return its.getAmount();
				}
			}
		}
		return 0;
	}
	
	public int getAmount(RealItemType itemType)
	{
		if (itemType != null) {
			Iterator<RealItemStack> listIterator = this.itemStacks.iterator();
			while (listIterator.hasNext()) {
				RealItemStack its = listIterator.next();
				if (its.getItemType().equals(itemType)) {
					return its.getAmount();
				}
			}
		}
		return 0;
	}
	
	public List<RealItemStack> getStacks()
	{
		return this.itemStacks;
	}
	
	@Override
	public String toString()
	{
		String tmps = "";
		Iterator<RealItemStack> listIterator = this.itemStacks.iterator();
		while (listIterator.hasNext()) {
			RealItemStack its = listIterator.next();
			if (!tmps.isEmpty()) {
				tmps += ";";
			}
			tmps += its.toString();
		}
		return tmps;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof RealItemStackList)) {
			return false;
		}
		
		RealItemStackList other = (RealItemStackList)obj;
		if (other.getStacks().isEmpty() && this.getStacks().isEmpty()) {
			return true;
		}
		if (other.getStacks().isEmpty() != this.getStacks().isEmpty()) {
			return false;
		}
		boolean found = false;
		Iterator<RealItemStack> ownListIterator = this.getStacks().iterator();
		while (ownListIterator.hasNext()) {
			RealItemStack ownItemStack = ownListIterator.next();
			Iterator<RealItemStack> otherListIterator = other.getStacks().iterator();
			while (otherListIterator.hasNext()) {
				if (otherListIterator.next().equals(ownItemStack)) {
					found = true;
					break;
				}
			}
			if (!found)
				return false;
		}
		return true;
	}
	
    @Override
    public int hashCode() {
    	int hash = this.getStacks().size();
        if (!this.getStacks().isEmpty()) {
			Iterator<RealItemStack> stackIterator = this.getStacks().iterator();
			while (stackIterator.hasNext()) {
				hash = hash ^ stackIterator.next().hashCode();
			}
        }
        return hash;
    }
	
}
