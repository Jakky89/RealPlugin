package fr.crafter.tickleman.realplugin;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;


public class RealChunk
{

	protected String worldName;
	protected int chunkX;
	protected int chunkZ;

	//---------------------------------------------------------------------------------- RealChunk
	public RealChunk(String theWorldName, int theXposition, int theZposition)
	{
		this.worldName = theWorldName;
		this.chunkX = theXposition;
		this.chunkZ = theZposition;
	}
	
	//---------------------------------------------------------------------------------- RealChunk
	public RealChunk(Chunk chunk)
	{
		this(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
	}
	
	//---------------------------------------------------------------------------------- RealChunk
	public RealChunk(RealLocation location)
	{
		this(location.getLocation().getChunk());
	}
	
	//---------------------------------------------------------------------------------- RealChunk
	public RealChunk(Location location)
	{
		this(location.getChunk());
	}

	//---------------------------------------------------------------------------------- RealChunk
	public RealChunk(Block block)
	{
		this(block.getLocation());
	}
	
	//---------------------------------------------------------------------------------- RealChunk
	public RealChunk(BlockState block)
	{
		this(block.getLocation());
	}
	
	public String getWorldName() {
		return this.worldName;
	}
	
	public World getWorld() {
		return Bukkit.getServer().getWorld(this.worldName);
	}
	
	public Chunk getChunk() {
		return this.getWorld().getChunkAt(this.getX(), this.getZ());
	}
	
	public int getX() {
		return this.chunkX;
	}
	
	public int getZ() {
		return this.chunkZ;
	}

	//----------------------------------------------------------------------------- calculateDistance
	public double calculateDistance(Chunk chunk2)
	{
		return Math.sqrt(Math.pow(Math.abs(this.getX() - chunk2.getX()), 2) + Math.pow(Math.abs(this.getZ() - chunk2.getZ()), 2));
	}

	//-------------------------------------------------------------------------------------- toString
	@Override
	public String toString()
	{
		return this.getWorldName() + ";" + String.valueOf(this.getX()) + ";" + String.valueOf(this.getZ());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof RealChunk)) {
			return false;
		}
		RealChunk other = (RealChunk)obj;
		if ((this.getWorldName() == other.getWorldName()) && (this.getX() == other.getX()) && (this.getZ() == other.getZ())) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		if (this.getWorldName() != null) {
			return (this.getX() ^ this.getZ() ^ this.getWorldName().hashCode() >>> 32);
		} else {
			return (this.getX() ^ this.getZ() >>> 32);
		}
	}

}
