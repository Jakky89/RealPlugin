package fr.crafter.tickleman.realplugin;

//######################################################################################## VarTools
public class RealVarTools
{

	//-------------------------------------------------------------------------------------- toString
	public static Boolean parseBoolean(String string, Boolean def)
	{
		if (string != null && !string.isEmpty()) {
			string = string.toLowerCase();
			if (string.equals("true") || string.equals("on") || string.equals("yes") || string.equals("enable") || string.equals("1"))
				return true;
			else if (string.equals("false") || string.equals("off") || string.equals("no") || string.equals("disable") || string.equals("0"))
				return false;
			else if (string.equals("null") || string.equals("default"))
				return null;
		}
		return def;
	}

	//----------------------------------------------------------------------------------- parseDouble
	public static Double parseDouble(String var, Double def)
	{
		try {
			return Double.parseDouble(var);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	//-------------------------------------------------------------------------------------- parseInt
	public static Integer parseInt(String var, Integer def)
	{
		try {
			return Integer.parseInt(var);
		} catch (NumberFormatException e) {
			return def;
		}
	}

}
