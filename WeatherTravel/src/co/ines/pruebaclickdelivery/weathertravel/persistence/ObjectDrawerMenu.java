package co.ines.pruebaclickdelivery.weathertravel.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;


/**
 * An class representing a object for menu.
 */
public class ObjectDrawerMenu {

	public static List<ItemsMenu> ITEMS_MENU = new ArrayList<ItemsMenu>();

	public static Map<String, ItemsMenu> ITEM_MAP_MENU = new HashMap<String, ItemsMenu>();

	String[] menu;
	public static Context mContext;

	private static void addItem(ItemsMenu item) {
		ITEMS_MENU.add(item);
		ITEM_MAP_MENU.put(item.id, item);
	}

	/**
	 * A item representing a piece of name.
	 */
	public static class ItemsMenu {

		/** item menu id 	 */ 
		public String id;
		/** item menu icon 	 */ 
		public int icon;
		/** item menu name 	 */ 
		public String name;

		public ItemsMenu(String id, int icon, String name) {
			this.id = id;
			this.icon = icon;
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public int getIcon() {
			return icon;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

}