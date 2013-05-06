package fr.crafter.tickleman.realplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class RealLocation
{
	protected String worldName;
	protected int locX;
	protected int locY;
	protected int locZ;

	//---------------------------------------------------------------------------------- RealLocation
	public RealLocation(String theWorldName, int x, int y, int z)
	{
		this.worldName = theWorldName;
		this.locX = x;
		this.locY = y;
		this.locZ = z;
	}
	
	//---------------------------------------------------------------------------------- RealLocation
	public RealLocation(Location location)
	{
		this(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	//---------------------------------------------------------------------------------- RealLocation
	public RealLocation(Block block)
	{
		this(block.getLocation());
	}
	
	public RealLocation(RealLocation location) {
		this(location.getWorldName(), location.getX(), location.getY(), location.getZ());
	}
	
	public RealLocation(RealBlock block) {
		this(block.getWorldName(), block.getX(), block.getY(), block.getZ());
	}
	
	//---------------------------------------------------------------------------------- RealLocation
	public RealLocation(String str)
	{
		fromString(str);
	}
	
	public String getWorldName() {
		return this.worldName;
	}
	
	public World getWorld() {
		return Bukkit.getServer().getWorld(worldName);
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

	//----------------------------------------------------------------------------- calculateDistance
	public double calculateDistance(RealLocation loc2)
	{
		return Math.sqrt(Math.pow(Math.abs(this.getX() - loc2.getX()), 2) + Math.pow(Math.abs(this.getY() - loc2.getY()), 2) + Math.pow(Math.abs(this.getZ() - loc2.getZ()), 2));
	}

	//------------------------------------------------------------------------------------ toLocation
	public Location getLocation()
	{
		return new Location(this.getWorld(), this.getX(), this.getY(), this.getZ());
	}
	
	public boolean fromString(String[] str)
	{
		if (str != null && str.length >= 4) {
			try {
				this.worldName = str[0];
				this.locX = Integer.parseInt(str[1]);
				this.locY = Integer.parseInt(str[2]);
				this.locZ = Integer.parseInt(str[3]);
				return true;
			} catch (Exception e) {
				System.out.println("[RealLocation] Error while converting from string location: " + e.getMessage());
				return false;
			}
		}
		System.out.println("[RealLocation] Error while converting from string location!");
		return false;
	}

	public boolean fromString(String str)
	{
		if (str != null && !str.isEmpty()) {
			try {
				String[] coords = str.trim().split(";");
				return this.fromString(coords);
			} catch (Exception e) {
				System.out.println("[RealLocation] Error while converting from string location: " + e.getMessage());
				return false;
			}
		}
		System.out.println("[RealLocation] Error while converting from string location!");
		return false;
	}
	
	//-------------------------------------------------------------------------------------- toString
	@Override
	public String toString()
	{
		return this.getWorldName() + ";" + String.valueOf(this.getX()) + ";" + String.valueOf(this.getY()) + ";" + String.valueOf(this.getZ());
	}

	@Override
	public boolean equals(Object obj) {
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
		if ((this.getWorldName().equals(other.getWorldName())) && (this.getX() == other.getX()) && (this.getY() == other.getY()) && (this.getZ() == other.getZ())) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		if (this.getWorldName() != null) {
			return (this.getX() ^ this.getY() ^ this.getZ() ^ this.getWorldName().hashCode() >>> 32);
		} else {
			return (this.getX() ^ this.getY() ^ this.getZ() >>> 32);
		}
	}

}
