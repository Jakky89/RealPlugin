package fr.crafter.tickleman.realplugin;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;


public class RealChunk
{

	protected String worldName;
	protected int chunkX;
	protected int chunkZ;
	
	protected static final int HASH_OFFSET = 18652613;
	protected static final int HASH_PRIME = -2130706029;


	public RealChunk(String theWorldName, int theXposition, int theZposition)
	{
		this.worldName = theWorldName;
		this.chunkX = theXposition;
		this.chunkZ = theZposition;
	}
	
	public RealChunk(Chunk chunk) {
		this(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
	}

	public RealChunk(Location location) {
		this(location.getChunk());
	}

	public RealChunk(Block block) {
		this(block.getLocation());
	}
	
	public RealChunk(BlockState block) {
		this(block.getLocation());
	}
	
	public String getWorldName() {
		return this.worldName;
	}
	
	public World getWorld(Server theServer) {
		return theServer.getWorld(this.worldName);
	}
	
	public Chunk getChunk(Server theServer) {
		return this.getWorld(theServer).getChunkAt(this.getX(), this.getZ());
	}
	
	public int getX() {
		return this.chunkX;
	}
	
	public int getZ() {
		return this.chunkZ;
	}
	
	public int getAddX(int addX) {
		return this.getX() + addX;
	}
	
	public int getAddZ(int addZ) {
		return this.getZ() + addZ;
	}
	
	public static double calculateDistance(RealChunk realChunk, RealChunk chunk2) {
		return Math.sqrt(Math.pow(Math.abs(realChunk.getX() - chunk2.getX()), 2) + Math.pow(Math.abs(realChunk.getZ() - chunk2.getZ()), 2));
	}

	//----------------------------------------------------------------------------- calculateDistance
	public double calculateDistance(RealChunk chunk2) {
		return calculateDistance(this, chunk2);
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
	
	/* 
	 * Using FNV-1a algorithm (performed best with minimal amount of collisions)
	 */
	@Override
	public int hashCode() {
		int hash = HASH_OFFSET;
		if (this.getWorldName() != null) {
			hash = hash ^ this.getWorldName().hashCode();
			hash = hash * HASH_PRIME;
		}
		hash = hash ^ this.getX();
		hash = hash * HASH_PRIME;
		hash = hash ^ this.getZ();
		hash = hash * HASH_PRIME;
		return hash;
	}

}
