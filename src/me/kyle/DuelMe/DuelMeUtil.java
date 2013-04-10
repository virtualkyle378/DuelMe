package me.kyle.DuelMe;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DuelMeUtil {
	
	private DuelMe plugin;

	public DuelMeUtil(DuelMe plugin) {
		this.plugin = plugin;
	}
	
	public void globalReset() {
		for(Arena i: plugin.arenas.values()){
			resetArena(i);
		}
	}
	
	public void resetArena(Arena i){
		i.door[0][0].getBlock().setType(Material.AIR);
		i.door[0][1].getBlock().setType(Material.AIR);
		i.door[1][0].getBlock().setType(Material.AIR);
		i.door[1][1].getBlock().setType(Material.AIR);
		i.block[0].getBlock().setType(Material.AIR);
		i.block[1].getBlock().setType(Material.AIR);
		i.running = false;
		i.accepted[0] = false;
		i.accepted[1] = false;//use 4 loops too
		i.player[0] = "";
		i.player[1] = "";
		i.pendingaccept = false;
		plugin.countdownid.put(i, 0);
	}
	
	public void sendMessage(String message, ChatColor color, Player player){
		player.sendMessage(color + "[DuelMe] " + message);
	}
	
	public void sendMessage(String message, ChatColor color, String player){
		sendMessage(message, color, plugin.getServer().getPlayer(player));
	}

	public String[] safeArgs(String[] args, int minargs) {
		String[] out = new String[minargs];
		Arrays.fill(out, "");
		System.arraycopy(args, 0, out, 0, args.length);
		return out;
	}
	
	public boolean checkCoords(Location pos1, Location pos2) {
//		System.out.println("pos1: X:" + pos1.getBlockX() + " Y:" + pos1.getBlockY()+ " Z:" + pos1.getBlockZ());
//		System.out.println("pos1: X:" + pos2.getBlockX() + " Y:" + pos2.getBlockY()+ " Z:" + pos2.getBlockZ());
//		System.out.println("diff of x:" + (pos2.getBlockX() - pos1.getBlockX()));
//		System.out.println("diff of Z:" + (pos2.getBlockZ() - pos1.getBlockZ()));
		if (pos2.getBlockX() - pos1.getBlockX() <= 0) {
			return false;
		}
		if (pos2.getBlockZ() - pos1.getBlockZ() <= 0) {
			return false;
		}
		return true;
	}
}
