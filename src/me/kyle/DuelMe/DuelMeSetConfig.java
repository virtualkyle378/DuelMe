package me.kyle.DuelMe;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class DuelMeSetConfig {
	private DuelMe plugin;
	//private String[] arenakeys = {"pos1","pos2","spawn1","spawn2","stands","door1.pos1","door1.pos2","door2.pos1","door2.pos2","block1","block2"};
	
	public DuelMeSetConfig(DuelMe plugin){
		this.plugin = plugin;
		exportInfo();
	}
	
	public void exportInfo(){
		if (plugin.lobbyspawn != null) {
			setValue("lobby.world", plugin.lobbyspawn.getWorld().getName());
			setLocation(plugin.lobbyspawn, "lobby.spawnpoint", true);
		}
		plugin.getConfig().set("arenas", null);
		for(Entry<String, Arena> i: plugin.arenas.entrySet()){
			if(i == null)
				continue;
			String key = "arenas." + i.getKey() + ".";
			Arena a = i.getValue();
			setLocation(a.pos[0], key.concat("pos1"), false);
			setLocation(a.pos[1], key.concat("pos2"), false);
			setLocation(a.spawn[0], key.concat("spawn1"), true);
			setLocation(a.spawn[1], key.concat("spawn2"), true);
			setLocation(a.stands, key.concat("stands"), true);
			setLocation(a.door[0][0], key.concat("door1.pos1"), false);
			setLocation(a.door[0][1], key.concat("door1.pos2"), false);
			setLocation(a.door[1][0], key.concat("door2.pos1"), false);
			setLocation(a.door[1][1], key.concat("door2.pos2"), false);
			setLocation(a.block[0], key.concat("block1"), false);
			setLocation(a.block[1], key.concat("block2"), false);
			setInv(a.inventory, key.concat("inv"));
			setInv(a.armor, key.concat("arm"));
		}
		plugin.saveConfig();
	}
	
	public void setInv(ItemStack[] inv,String path){
		HashMap<String, ItemStack> values = new HashMap<String, ItemStack>();
		for(int i = 0; i < inv.length; i++){
			values.put("." + Integer.toString(i), inv[i]);
		}
		plugin.getConfig().set(path, null);
		plugin.getConfig().createSection(path, values);
	}
	
	
	private void setLocation(Location loc, String path, boolean deep){
		setValue(path.concat(".x"), loc.getX());
		setValue(path.concat(".y"), loc.getY());
		setValue(path.concat(".z"), loc.getZ());
		if(deep){
			setValue(path.concat(".pitch"), loc.getPitch());
			setValue(path.concat(".yaw"), loc.getYaw());
		}
	}
	
	private void setValue(String path, Object value){
		System.out.println("Outputting: " + path + ": " + value);
		if(plugin.getConfig().contains(path)){
			plugin.getConfig().set(path, value);
		} else {
			plugin.getConfig().createSection(path);
			plugin.getConfig().set(path, value);
		} 
	}
}
