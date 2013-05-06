package fr.crafter.tickleman.realplugin;

import java.lang.reflect.Field;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

//#################################################################################### RealItemType
public class RealItemType implements Comparable<RealItemType>
{

	/**
	 * Minecraft type identifier of item
	 */
	private int typeId;

	/**
	 * Variant code of item, for items than can have variants
	 * Equals ItemStack.getDurability() for items that can be damaged
	 * Is null for non-applicable items
	 */
	private short variant;


	//-------------------------------------------------------------------------------------- ItemType
	public RealItemType(int theTypeId, short theVariant)
	{
		this.setTypeIdVariant(theTypeId, theVariant);
	}

	//-------------------------------------------------------------------------------------- ItemType
	public RealItemType(Material theMaterial, short theVariant)
	{
		this(theMaterial.getId(), theVariant);
	}

	//-------------------------------------------------------------------------------------- ItemType
	public RealItemType(int theTypeId)
	{
		this(theTypeId, (short)0);
	}
	
	//------------------------------------------------------------------------------------- ItemStack
	public RealItemType(ItemStack theItemStack)
	{
		this(theItemStack.getTypeId(), theItemStack.getDurability());
	}
	
	//------------------------------------------------------------------------------------- ItemStack
	public RealItemType(RealItemStack theItemStack)
	{
		this(theItemStack.getTypeId(), theItemStack.getDurability());
	}
	
	//-------------------------------------------------------------------------------------- ItemType
	public RealItemType(Material theMaterial)
	{
		this(theMaterial.getId(), (short)0);
	}
	
	//------------------------------------------------------------------------------------ hasVariant
	public boolean hasVariant(short theVariant)
	{
		if (theVariant == 0) {
			return true;
		}
		short[] variants = typeIdVariants(getTypeId());
		if (variants.length > 0) {
			for (short av : variants) {
				if (av == theVariant) {
					return true;
				}
			}
		}
		return false;
	}
	
	//------------------------------------------------------------------------------------ setVariant
	public void setVariant(short theVariant)
	{
		if (this.hasVariant(theVariant)) {
			this.variant = theVariant;
		} else {
			this.variant = 0;
		}
	}
	
	//------------------------------------------------------------------------------ setTypeIdVariant
	public void setTypeIdVariant(int theTypeId, short theVariant)
	{
		this.typeId = theTypeId;
		this.setVariant(theVariant);
	}

	//--------------------------------------------------------------------------------------- getName
	public String getName()
	{
		return getName(this.getTypeId());
	}

	//------------------------------------------------------------------------------------- getTypeId
	public int getTypeId()
	{
		return this.typeId;
	}

	//------------------------------------------------------------------------------------ getVariant
	public short getVariant()
	{
		return this.variant;
	}
	
	//--------------------------------------------------------------------------------------- getName
	public static String getName(int theTypeId)
	{
		Material tmpMaterial = Material.getMaterial(theTypeId);
		if (tmpMaterial != null) {
			return tmpMaterial.name();
		}
		return "Item" + String.valueOf(theTypeId);
	}

	//--------------------------------------------------------------------------------- parseItemType
	public static RealItemType parseItemType(String theTypeIdVariant)
	{
		if (!theTypeIdVariant.isEmpty()) {
			try {
				int tvPos = theTypeIdVariant.indexOf(':');
				if (tvPos > 0) {
					return new RealItemType(Integer.parseInt(theTypeIdVariant.substring(0, tvPos)), Short.parseShort(theTypeIdVariant.substring(tvPos+1)));
				} else {
					return new RealItemType(Integer.parseInt(theTypeIdVariant));
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	//------------------------------------------------------------------------- parseItemTypeKeywords
	public static RealItemType parseItemTypeKeywords(String[] keyWords)
	{
		RealItemType rit = null;
		try {
			for (String kw : keyWords) {
				rit = RealItemType.parseItemType(kw);
				if (rit != null)
					break;
			}
		} catch (Exception e) {
			return null;
		}
		return rit;
	}

	//------------------------------------------------------------------------------------- setTypeId
	public void setTypeId(int theTypeId)
	{
		this.setTypeIdVariant(theTypeId, this.getVariant());
	}

	//------------------------------------------------------------------------------------ isSameItem
	public boolean isSameItemType(RealItemType theItemType)
	{
		if (theItemType.getTypeId() == this.getTypeId() && theItemType.getVariant() == this.getVariant()) {
			return true;
		}
		return false;
	}

	//--------------------------------------------------------------------------------- toNamedString
	public String toNamedString()
	{
		if (this.getVariant() > 0) {
		 	return this.getName() + " : " + String.valueOf(this.getVariant());
		} else {
			return this.getName();
		}
	}

	//-------------------------------------------------------------------------------------- toString
	@Override
	public String toString()
	{
		if (this.getVariant() > 0) {
			return String.valueOf(this.getTypeId()) + ":" + String.valueOf(this.getVariant());
		} else {
			return String.valueOf(this.getTypeId());
		}
	}

	//------------------------------------------------------------------------------- typeIdHasDamage
	public static boolean typeIdHasDamage(int theTypeId)
	{
		if (typeIdVariants(theTypeId).length > 0) {
			return false;
		}
		return true;
	}

	//------------------------------------------------------------------------------ typeIdHasVariant
	public static boolean typeIdHasVariant(int theTypeId)
	{
		if (typeIdVariants(theTypeId).length > 0) {
			return true;
		}
		return false;
	}

	//------------------------------------------------------------------------------- typeIdMaxDamage
	public static short typeIdMaxDamage(int theTypeId)
	{
		if (typeIdHasVariant(theTypeId)) {
			return 0;
		} else if (theTypeId < 256) {
			// this could be easily broken on craftbukkit's next updates,
			// but I mean that blocks are never traded with a damage value, as they are
			// damaged only when you hit them, and are never damaged when in inventories
			try {
				Field strength = Material.class.getField("durability");
				if (!strength.isAccessible()) {
					strength.setAccessible(true);
				}
				return (short) Math.round(strength.getDouble(Material.getMaterial(theTypeId).getMaxDurability()));
			} catch (Exception e) {
				Bukkit.getServer().getLogger().log(Level.SEVERE, "Exception occurred while getting max damage of " + theTypeId + ": " + e.getMessage());
			}
			return 128;
		} else {
			return (short)Material.getMaterial(theTypeId).getMaxDurability();
		}
	}

	//-------------------------------------------------------------------------------- typeIdVariants
	public static short[] typeIdVariants(Material theMaterial)
	{
		switch (theMaterial) {
			case COBBLESTONE:
			case COBBLE_WALL:
			case COAL:
			case GOLDEN_APPLE:
			case MAP:
			case BOOK_AND_QUILL:
				return new short[]{0, 1};
			case SANDSTONE:
			case LONG_GRASS:
			case MONSTER_EGGS:
			case QUARTZ_BLOCK:
				return new short[]{0, 1, 2};
			case WOOD:
			case LOG:
			case SAPLING:
			case LEAVES:
			case SMOOTH_BRICK:
			case WOOD_DOUBLE_STEP:
			case WOOD_STEP:
				return new short[]{0, 1, 2, 3};
			case SKULL:
				return new short[]{0, 1, 2, 3, 4};
			case DOUBLE_STEP:
			case STEP:
				return new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
			case FLOWER_POT:
				return new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
			case WOOL:
			case HUGE_MUSHROOM_1:
			case HUGE_MUSHROOM_2:
			case INK_SACK:
				return new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
			case JUKEBOX:
				return new short[]{0, 2256, 2257, 2258, 2259, 2260, 2261, 2262, 2263, 2264, 2265, 2266, 2267};
			case MONSTER_EGG:
				return new short[]{
						50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 65, 66,
						90, 91, 92, 93, 94, 95, 96, 97, 98,
						100, 120
				};
			case POTION:
				return new short[]{
						0, 16, 32, 64, 8192, 8193, 8257, 8225, 8194, 8258, 8226, 8195, 8259, 8197, 8229, 8201, 8265,
						8233, 8196, 8260, 8228, 8200, 8264, 8202, 8266, 8204, 8236, 16384, 16385, 16449, 16417,
						16386, 16450, 16418, 16387, 16451, 16389, 16421, 16393, 16457, 16425, 16388, 16452, 16420,
						16392, 16456, 16394, 16458, 16396, 16428
				};
			default:
				return new short[]{0};
		}
	}
		
	//-------------------------------------------------------------------------------- typeIdVariants
	public static short[] typeIdVariants(int theItemTypeId)
	{
		return typeIdVariants(Material.getMaterial(theItemTypeId));
	}
	
	//-------------------------------------------------------------------------------- typeIdVariants
	public static short[] typeIdVariants(RealItemType parseItemType)
	{
		return typeIdVariants(parseItemType.getTypeId());
	}
	
	//-------------------------------------------------------------------------------- getRealId
	public int getRealId() {
		return (this.getTypeId() * 16429) + this.getVariant();
	}
	
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RealItemType)) {
            return false;
        }
        return this.isSameItemType((RealItemType)obj);
    }
	
    @Override
    public int hashCode() {
        return this.getRealId();
    }

	@Override
	public int compareTo(RealItemType itemType) {
		return this.getRealId() - itemType.getRealId();
	}

}
