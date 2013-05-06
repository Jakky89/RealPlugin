package fr.crafter.tickleman.realplugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;


//##################################################################################### RealRecipes
public class RealRecipe
{
	private Server server;
	private RealItemStack resultItem;
	private RealItemStackList recipeIngredients;
	
	//--------------------------------------------------------------------------------- getResultItem
	public RealItemStackList getRecipeIngredients()
	{
		return this.recipeIngredients;
	}

	//--------------------------------------------------------------------------------- getResultItem
	public RealItemStack getResultItem()
	{
		return this.resultItem;
	}

	//------------------------------------------------------------------------------------ RealRecipe
	/**
	 * Generate a easily usable recipe, based on Minecraft's crafting recipe
	 */
	public RealRecipe(Server theServer, Recipe theRecipe, RealItemStack theResultItem)
	{
		this.server = theServer;
		this.resultItem = theResultItem;
		this.recipeIngredients = new RealItemStackList();
		if (theRecipe != null) {
			try {
				if (theRecipe instanceof FurnaceRecipe) {
					this.recipeIngredients.add(((FurnaceRecipe)theRecipe).getInput());
				} else if (theRecipe instanceof ShapedRecipe) {
					this.recipeIngredients.add(((ShapedRecipe)theRecipe).getIngredientMap().values(), true);
				} else if (theRecipe instanceof ShapelessRecipe) {
					this.recipeIngredients.add(((ShapelessRecipe)theRecipe).getIngredientList(), true);
				} else {
					this.server.getLogger().log(Level.SEVERE, "Unknown recipe type \"" + theRecipe.getClass().getName() + "\" of " + this.resultItem.toNamedString());
				}
			} catch (Exception e) {
				this.server.getLogger().log(Level.SEVERE, "Error on \"" + theRecipe.getClass().getName() + "\" recipe of " + this.resultItem.toNamedString() + ": " + e.getMessage());
			}
		}
	}

	//-------------------------------------------------------------------------------- getItemRecipes
	/**
	 * Return a set of possible recipes for given item type
	 */
	public static List<RealRecipe> getItemRecipes(Server theServer, RealItemType theRealItemType)
	{
		List<RealRecipe> itemRecipes = new ArrayList<RealRecipe>();
		if (theRealItemType != null) {
		    Iterator<Recipe> recipeIterator = theServer.recipeIterator();
			while (recipeIterator.hasNext()) {
			    Recipe oneRecipe = recipeIterator.next();
			    if (oneRecipe == null)
			    	continue;
			    ItemStack itemStack = oneRecipe.getResult();
			    if (itemStack == null || itemStack.getType().equals(Material.AIR))
			    	continue;
			    RealItemStack resultItemStack = new RealItemStack(itemStack);
			    if (theRealItemType.isSameItemType((RealItemType)resultItemStack)) {
			    	itemRecipes.add(new RealRecipe(theServer, oneRecipe, resultItemStack));
			    }
		    }
		}
		return itemRecipes;
	}

	//-------------------------------------------------------------------------------- dumpAllRecipes
	public static void dumpRecipes(Server theServer, Integer theItemId)
	{
		Iterator<Recipe> recipeIterator = theServer.recipeIterator();
		while (recipeIterator.hasNext()) {
		    Recipe chkRecipe = recipeIterator.next();
		    if (chkRecipe == null)
		    	continue;
		    if (theItemId != null && chkRecipe.getResult().getTypeId() != theItemId)
		    	continue;
		    RealRecipe realrec = new RealRecipe(theServer, chkRecipe, new RealItemStack(chkRecipe.getResult()));
		    if (realrec != null) {
		    	theServer.getLogger().log(Level.INFO, "Recipe for " + realrec.getResultItem().toNamedString() + " : " + realrec.toNamedString());
		    }
		}
	}

	//--------------------------------------------------------------------------------- toNamedString
	public String toNamedString()
	{
		String result = this.resultItem.toNamedString() + " = ";
		if (!this.recipeIngredients.getStacks().isEmpty()) {
			Iterator<RealItemStack> itemIterator = this.recipeIngredients.getStacks().iterator();
			while (itemIterator.hasNext()) {
				RealItemStack itemStack = itemIterator.next();
				result += " + " + itemStack.toNamedString();
			}
		}
		return result;
	}

	//-------------------------------------------------------------------------------------- toString
	@Override
	public String toString()
	{
		String result = this.resultItem.toString() + "=";
		if (!this.recipeIngredients.getStacks().isEmpty()) {
			Iterator<RealItemStack> itemIterator = this.recipeIngredients.getStacks().iterator();
			while (itemIterator.hasNext()) {
				RealItemStack itemStack = itemIterator.next();
				result += "+" + itemStack.toString();
			}
		}
		return result;
	}
	
	//-------------------------------------------------------------------------------------- equals
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof RealRecipe)) {
			return false;
		}
		RealRecipe other = (RealRecipe)obj;
		return  (this.resultItem.equals(other.resultItem) && this.recipeIngredients.equals(other.recipeIngredients));
	}
	
	//-------------------------------------------------------------------------------------- hashCode
	@Override
	public int hashCode()
	{
		return (this.recipeIngredients.hashCode() ^ this.resultItem.hashCode() >>> 32);
	}
	
}
