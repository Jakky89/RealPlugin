package fr.crafter.tickleman.realplugin;

import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


//####################################################################################### RealChest
/*
 * This Chest object manages small and big chests
 */
public class RealChest extends RealBlock
{
	public static boolean isChestBlock(Block theBlock) {
		if (theBlock != null && (theBlock.getType().equals(Material.CHEST) || theBlock.getType().equals(Material.TRAPPED_CHEST) || theBlock.getType().equals(Material.FURNACE) || theBlock.getType().equals(Material.DISPENSER) || theBlock.getType().equals(Material.BREWING_STAND))) {
			return true;
		}
		return false;
	}
	
	public static boolean isChestBlock(BlockState theBlock) {
		return isChestBlock(theBlock.getBlock());
	}
	
	//------------------------------------------------------------------------------------- ReadChest
	/**
	 * create chest from an existing block reference
	 * block must be a chest tested with block.getType().equals(Material.CHEST)
	 */
	public RealChest(Block theBlock)
	{
		super(theBlock);
	}
	
	public RealChest(BlockState theBlock)
	{
		super(theBlock);
	}
	
	public RealChest(Location location)
	{
		super(location);
	}
	
	public RealChest(RealLocation location)
	{
		super(location);
	}

	public RealChest(String str, Server theServer) {
		super(str);
	}
	
	public static Chest getNeighborChest(Block theBlock) {
		if (theBlock != null && (theBlock.getType().equals(Material.CHEST) || theBlock.getType().equals(Material.TRAPPED_CHEST))) {
			for (BlockFace bf : RealBlock.relativeBlockFaces) {
				Block nb = theBlock.getRelative(bf);
				if (nb != null) {
					if (nb.getType().equals(Material.CHEST) || nb.getType().equals(Material.TRAPPED_CHEST)) {
						return (Chest)nb.getState();
					}
				}
			}
		}
		return null;
	}

	public static Chest getNeighborChest(RealBlock theBlock, Server theServer) {
		return getNeighborChest(theBlock.getBlock(theServer));
	}
	
	public Chest getNeighborChest(Server theServer) {
		return getNeighborChest(this, theServer);
	}

	public Inventory getNeighborChestInventory(Server theServer) {
		Chest nbs = this.getNeighborChest(theServer);
		if (nbs != null) {
			return nbs.getBlockInventory();
		}
		return null;
	}
	
	//-------------------------------------------------------------------------------- getInventory
	public Inventory getInventory(Server theServer)
	{
		Block block = this.getBlock(theServer);
		if (block != null && (block instanceof InventoryHolder)) {
			return ((InventoryHolder)block.getState()).getInventory();
		}
		return null;
	}
	
	//-------------------------------------------------------------------------------- getItems
	public RealItemStackList getItems(Server theServer)
	{
		RealItemStackList chi = new RealItemStackList();
		Inventory own = this.getInventory(theServer);
		if (own != null) {
			Iterator<ItemStack> owni = own.iterator();
			while (owni.hasNext()) {
				chi.add(owni.next());
			}
		}
		Inventory other = this.getNeighborChestInventory(theServer);
		if (other != null) {
			Iterator<ItemStack> othi = other.iterator();
			while (othi.hasNext()) {
				chi.add(othi.next());
			}
		}
		return chi;
	}
	
}
