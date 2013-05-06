package fr.crafter.tickleman.realplugin;

import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
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
		adjustCoords();
	}
	
	public RealChest(BlockState theBlock)
	{
		super(theBlock.getBlock());
		adjustCoords();
	}
	
	public RealChest(Location location)
	{
		this(location.getBlock());
	}
	
	public RealChest(RealLocation location)
	{
		this(location.getLocation());
	}

	public RealChest(String str) {
		super(str);
		adjustCoords();
	}
	
	public void adjustCoords() {
		Block block = this.getBlock();
		if (block != null) {
			Location loc = null;
			if (block instanceof Chest) {
				loc = ((Chest)block.getState()).getLocation();
			} else if (block instanceof DoubleChest) {
				loc = ((DoubleChest)block.getState()).getLocation();
			} else if (block instanceof Furnace) {
				loc = ((Furnace)block.getState()).getLocation();
			} else if (block instanceof Dispenser) {
				loc = ((Dispenser)block.getState()).getLocation();
			} else if (block instanceof BrewingStand) {
				loc = ((BrewingStand)block.getState()).getLocation();
			}
			if (loc != null) {
				this.worldName = loc.getWorld().getName();
				this.locX = loc.getBlockX();
				this.locY = loc.getBlockY();
				this.locZ = loc.getBlockZ();
			}
		}
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

	public static Chest getNeighborChest(RealLocation theLocation) {
		return getNeighborChest(theLocation.getLocation().getBlock());
	}
	
	public Chest getNeighborChest() {
		return getNeighborChest(this);
	}

	public Inventory getNeighborChestInventory() {
		Chest nbs = this.getNeighborChest();
		if (nbs != null) {
			return nbs.getBlockInventory();
		}
		return null;
	}
	
	//-------------------------------------------------------------------------------- getInventory
	public Inventory getInventory()
	{
		Block block = this.getBlock();
		if (block != null && (block instanceof InventoryHolder)) {
			return ((InventoryHolder)block.getState()).getInventory();
		}
		return null;
	}
	
	//-------------------------------------------------------------------------------- getItems
	public RealItemStackList getItems()
	{
		RealItemStackList chi = new RealItemStackList();
		Inventory own = this.getInventory();
		if (own != null) {
			Iterator<ItemStack> owni = own.iterator();
			while (owni.hasNext()) {
				chi.add(owni.next());
			}
		}
		Inventory other = this.getNeighborChestInventory();
		if (other != null) {
			Iterator<ItemStack> othi = other.iterator();
			while (othi.hasNext()) {
				chi.add(othi.next());
			}
		}
		return chi;
	}
	
}
