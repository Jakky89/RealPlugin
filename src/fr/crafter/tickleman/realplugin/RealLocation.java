package fr.crafter.tickleman.realplugin;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;


public class RealLocation
{
	protected String worldName;
	protected int locX;
	protected int locY;
	protected int locZ;
	
	protected static final int HASH_OFFSET = 18652613;
	protected static final int HASH_PRIME = -2130706029;

	
	public RealLocation(String theWorldName, int x, int y, int z)
	{
		this.worldName = theWorldName;
		this.locX = x;
		this.locY = y;
		this.locZ = z;
	}
	
	public RealLocation(Location location) {
		this(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	public RealLocation(BlockState block) {
		this(block.getLocation());
	}

	public RealLocation(Block block) {
		this(block.getLocation());
	}
	
	public RealLocation(RealLocation location) {
		this(location.getWorldName(), location.getX(), location.getY(), location.getZ());
	}
	
	public RealLocation(String str) {
		this.fromString(str);
	}
	
	public String getWorldName() {
		return this.worldName;
	}
	
	public World getWorld(Server theServer) {
		return theServer.getWorld(this.worldName);
	}
	
	public int getX() {
		return this.locX;
	}
	
	public int getY() {
		return this.locY;
	}
	
	public int getZ() {
		return this.locZ;
	}
	
	public int getAddX(int addX)
	{
		return this.getX() + addX;
	}
	
	public int getAddY(int addY)
	{
		return this.getY() + addY;
	}
	
	public int getAddZ(int addZ)
	{
		return this.getZ() + addZ;
	}
	
	public static double calculateDistance(RealLocation loc1, RealLocation loc2) {
		return (double)Math.sqrt(Math.pow(Math.abs(loc1.getX() - loc2.getX()), 2) + Math.pow(Math.abs(loc1.getY() - loc2.getY()), 2) + Math.pow(Math.abs(loc1.getZ() - loc2.getZ()), 2));
	}

	public double calculateDistance(RealLocation loc2) {
		return calculateDistance(this, loc2);
	}

	public Location getLocation(Server theServer) {
		return new Location(this.getWorld(theServer), this.getX(), this.getY(), this.getZ());
	}
	
	public boolean fromString(String[] str)
	{
		if (str != null && str.length >= 4)
		{
			try {
				this.worldName = str[0];
				this.locX = Integer.parseInt(str[1]);
				this.locY = Integer.parseInt(str[2]);
				this.locZ = Integer.parseInt(str[3]);
				return true;
			} catch (Exception e) {
				System.out.println("[RealLocation] Exception occurred while converting from string location: " + e.getMessage());
			}
		}
		return false;
	}

	public boolean fromString(String str)
	{
		if (str != null && !str.isEmpty()) {
			return this.fromString(str.split(";"));
		}
		return false;
	}
	
	//-------------------------------------------------------------------------------------- toString
	@Override
	public String toString() {
		return (this.getWorldName() + ";" + String.valueOf(this.getX()) + ";" + String.valueOf(this.getY()) + ";" + String.valueOf(this.getZ()));
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
		if (!(obj instanceof RealLocation)) {
			return false;
		}
		RealLocation other = (RealLocation)obj;
		if ((this.getWorldName() == other.getWorldName()) && (this.getX() == other.getX()) && (this.getY() == other.getY()) && (this.getZ() == other.getZ())) {
			return true;
		}
		return false;
	}
	
	/* 
	 * Using FNV-1a algorithm (performed best with only minimal amount of collisions)
	 */
	@Override
	public int hashCode()
	{
		int hash = HASH_OFFSET;
		if (this.getWorldName() != null) {
			hash = hash ^ this.getWorldName().hashCode();
			hash = hash * HASH_PRIME;
		}
		hash = hash ^ this.getX();
		hash = hash * HASH_PRIME;
		hash = hash ^ this.getY();
		hash = hash * HASH_PRIME;
		hash = hash ^ this.getZ();
		hash = hash * HASH_PRIME;
		return hash;
	}

}
