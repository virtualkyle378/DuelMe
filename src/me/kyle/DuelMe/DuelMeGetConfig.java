package me.kyle.DuelMe;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class DuelMeGetConfig {
	
	private DuelMe plugin;
	//private String[] arenakeys = {"pos1","pos2","spawn1","spawn2","stands","door1.pos1","door1.pos2","door2.pos1","door2.pos2","block1","block2"};
	
	@SuppressWarnings("unused")
	public DuelMeGetConfig(DuelMe plugin){
		this.plugin = plugin;
		FileConfiguration config = plugin.getConfig();
		//config.options().copyDefaults(false);
		plugin.saveConfig();
		importInfo();
	}

	public void importInfo(){
		if (plugin.getServer().getWorld(plugin.getConfig().getString("lobby.world")) != null) {
			plugin.lobbyspawn = new Location(plugin.getServer().getWorld(
					plugin.getConfig().getString("lobby.world")), plugin
					.getConfig().getDouble("lobby.spawnpoint.x", 0), plugin
					.getConfig().getDouble("lobby.spawnpoint.y", 0), plugin
					.getConfig().getDouble("lobby.spawnpoint.z", 0),
					(float) plugin.getConfig().getDouble("lobby.spawnpoint.yaw", 0), 
					(float) plugin.getConfig().getDouble("lobby.spawnpoint.pitch", 0));
		}
		for(String i: plugin.getConfig().getConfigurationSection("arenas").getKeys(false)){
			String key = "arenas." + i + ".";
			Arena a = new Arena();
			a.pos[0] = getLocation(key.concat("pos1"), false);
			a.pos[1] = getLocation(key.concat("pos2"), false);
			a.spawn[0] = getLocation(key.concat("spawn1"), true);
			a.spawn[1] = getLocation(key.concat("spawn2"), true);
			a.stands = getLocation(key.concat("stands"), true);
			a.door[0][0] = getLocation(key.concat("door1.pos1"), false);
			a.door[0][1] = getLocation(key.concat("door1.pos2"), false);
			a.door[1][0] = getLocation(key.concat("door2.pos1"), false);
			a.door[1][1] = getLocation(key.concat("door2.pos2"), false);
			a.block[0] = getLocation(key.concat("block1"), false);
			a.block[1] = getLocation(key.concat("block2"), false);
			a.inventory = getInv(key.concat("inv"));
			a.armor = getInv(key.concat("arm"));
			
			a.player[0] = ""; 
			a.player[1] = "";
			plugin.arenas.put(i, a);
			//plugin.util.set(index, a);
		}
	}
	
	public ItemStack[] getInv(String path){
		ArrayList<ItemStack> inv = new ArrayList<ItemStack>();
		plugin.getConfig().getMapList(path);
		if (plugin.getConfig().getConfigurationSection(path) == null) {//TODO maybe identify wat inv pos it came from. meh
			return null;
		}
		try {
			for(String i: plugin.getConfig().getConfigurationSection(path).getKeys(false)){
				inv = set(Integer.parseInt(i), plugin.getConfig().getItemStack(path + "." + i, null), inv);
			}
		} catch (NumberFormatException e) {}
		ItemStack[] items = new ItemStack[inv.size() - 1];
		return inv.toArray(items);
	}

	public ArrayList<ItemStack> set(int index, ItemStack element, ArrayList<ItemStack> list){
		list.ensureCapacity(index);
		while (list.size() <= index) {
			list.add(null);
	    }
		list.set(index, element);
		return list;
	}
	
	public Location getLocation(String path, boolean deep){
		if(deep)
			return new Location(plugin.lobbyspawn.getWorld(),
				plugin.getConfig().getDouble(path + ".x", 0),
				plugin.getConfig().getDouble(path + ".y", 0),
				plugin.getConfig().getDouble(path + ".z", 0),
				(float)plugin.getConfig().getDouble(path + ".yaw", 0),
				(float)plugin.getConfig().getDouble(path + ".pitch", 0));
		else
			return new Location(plugin.lobbyspawn.getWorld(),
					plugin.getConfig().getDouble(path + ".x", 0),
					plugin.getConfig().getDouble(path + ".y", 0),
					plugin.getConfig().getDouble(path + ".z", 0));
	}
}
