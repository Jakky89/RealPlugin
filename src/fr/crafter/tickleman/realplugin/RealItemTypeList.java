package fr.crafter.tickleman.realplugin;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//#################################################################################### ItemTypeList
public class RealItemTypeList
{

	private Set<RealItemType> content;

	public RealItemTypeList()
	{
		content = new HashSet<RealItemType>();
	}
	
	//----------------------------------------------------------------------------------------- clear
	public void clear()
	{
		this.content.clear();
	}

	//-------------------------------------------------------------------------------- addRemoveChain
	public void addRemoveChain(String chain)
	{
		if (chain != null && !chain.isEmpty()) {
			String chn = chain.toLowerCase();
			if (chn.equals("all")) {
				this.clear();
			} else if (chn.equals("none") || chn.equals("no")) {
				this.clear();
				this.put(RealItemType.parseItemType("0"));
			} else {
				String[] chnArr = chn.split("\\+");
				for (String subChain : chnArr) {
					subChain = subChain.toLowerCase();
					boolean isPlus = true;
					for (String elem : subChain.split("\\-")) {
						elem = elem.toLowerCase();
						if (!elem.isEmpty()) {
							if (elem.contains(":*")) {
								elem = elem.replace(":*", "");
								for (short variant : RealItemType.typeIdVariants(RealItemType.parseItemType(elem))) {
									this.addRemoveItem(elem + ":" + variant, isPlus);
								}
							} else {
								this.addRemoveItem(elem, isPlus);
							}
						}
						isPlus = false;
					}
				}
			}
		}
	}

	//--------------------------------------------------------------------------------- addRemoveItem
	private void addRemoveItem(String elem, boolean isPlus)
	{
		RealItemType itemType = RealItemType.parseItemType(elem);
		if (isPlus) {
			this.put(itemType);
		} else {
			this.remove(itemType);
		}
	}

	//------------------------------------------------------------------------------------ getContent
	public Set<RealItemType> getContent()
	{
		return this.content;
	}

	//--------------------------------------------------------------------------------------- isEmpty
	public boolean isEmpty()
	{
		return this.content.isEmpty();
	}
	
	//--------------------------------------------------------------------------------------- contains
	public boolean contains(RealItemType itemType) {
		return this.content.contains(itemType);
	}

	//----------------------------------------------------------------------------- parseItemTypeList
	public static RealItemTypeList parseItemTypeList(String list)
	{
		RealItemTypeList itemTypeList = new RealItemTypeList();
		for (String typeIdVariant : list.split(",")) {
			if (typeIdVariant.length() > 0) {
				itemTypeList.put(RealItemType.parseItemType(typeIdVariant));
			}
		}
		return itemTypeList;
	}

	//----------------------------------------------------------------------------- parseItemTypeList
	public static RealItemTypeList parseItemTypeList(Set<String> list)
	{
		RealItemTypeList itemTypeList = new RealItemTypeList();
		for (String typeIdVariant : list) {
			itemTypeList.put(RealItemType.parseItemType(typeIdVariant));
		}
		return itemTypeList;
	}

	//------------------------------------------------------------------------------------------- put
	public void put(RealItemType itemType)
	{
		this.content.add(itemType);
	}

	//---------------------------------------------------------------------------------------- remove
	public void remove(RealItemType itemType)
	{
		this.content.remove(itemType);
	}

	//-------------------------------------------------------------------------------------- toString
	@Override
	public String toString()
	{
		String result = "";
		Iterator<RealItemType> iti = this.content.iterator();
		while (iti.hasNext()) {
			if (!result.isEmpty()) {
				result += ",";
			}
			result += iti.next().toString();
		}
		return result;
	}

	//--------------------------------------------------------------------------------- toNamedString
	public String toNamedString(RealTranslation tr)
	{
		String result = "";
		Iterator<RealItemType> iti = this.content.iterator();
		while (iti.hasNext()) {
			RealItemType rit = iti.next();
			if (!result.isEmpty()) {
				result += ",";
			}
			if (tr != null) {
				result += RealTranslation.trItemName(rit.getName());
			} else {
				result += rit.getName();
			}
		}
		return result;
	}
	
	//--------------------------------------------------------------------------------- toNamedString
	public String toNamedString()
	{
		return toNamedString(null);
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
		RealItemTypeList other = (RealItemTypeList)obj;
		if (other.getContent().isEmpty() && this.getContent().isEmpty()) {
			return true;
		}
		if (other.getContent().isEmpty() != this.getContent().isEmpty()) {
			return false;
		}
		Iterator<RealItemType> listIterator = this.getContent().iterator();
		while (listIterator.hasNext()) {
			RealItemType itemType = listIterator.next();
			if (!other.contains(itemType)) {
				return false;
			}
		}
		return true;
	}
	
    @Override
    public int hashCode() {
    	int hash = this.getContent().size();
        if (!this.getContent().isEmpty()) {
			Iterator<RealItemType> contentIterator = this.getContent().iterator();
			while (contentIterator.hasNext()) {
				hash = hash ^ contentIterator.next().hashCode();
			}
        }
        return hash;
    }
	
}
