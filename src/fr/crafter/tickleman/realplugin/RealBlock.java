package fr.crafter.tickleman.realplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

//####################################################################################### RealBlock
public class RealBlock extends RealLocation
{
	public static final BlockFace[] relativeBlockFaces = new BlockFace[] {
		BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN
	};
	
	public static boolean isSignBlock(Block theBlock) {
		if (theBlock.getType().equals(Material.SIGN) || theBlock.getType().equals(Material.SIGN_POST) || theBlock.getType().equals(Material.WALL_SIGN)) {
			return true;
		}
		return false;
	}
	
	public static boolean isSignBlock(BlockState theBlock) {
		return isSignBlock(theBlock.getBlock());
	}

	//---------------------------------------------------------------------------------- RealLocation
	public RealBlock(Location location)
	{
		super(location);
	}

	//---------------------------------------------------------------------------------- RealLocation
	public RealBlock(String theWorldName, int blockX, int blockY, int blockZ)
	{
		super(theWorldName, blockX, blockY, blockZ);
	}
	
	//---------------------------------------------------------------------------------- RealLocation
	public RealBlock(BlockState block)
	{
		super(block);
	}
	
	//---------------------------------------------------------------------------------- RealLocation
	public RealBlock(Block block)
	{
		super(block);
	}
	
	public RealBlock(RealLocation location) {
		super(location);
	}
	
	public RealBlock(RealBlock block) {
		super(block.getWorldName(), block.getX(), block.getY(), block.getZ());
	}
	
	public RealBlock(String str) {
		super(str);
	}
	
	public Block getBlock(Server theServer) {
		return super.getLocation(theServer).getBlock();
	}
	
	public BlockState getBlockState(Server theServer) {
		return this.getBlock(theServer).getState();
	}

}
