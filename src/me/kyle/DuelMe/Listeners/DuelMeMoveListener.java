package me.kyle.DuelMe.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import me.kyle.DuelMe.Arena;
import me.kyle.DuelMe.DuelMe;

public class DuelMeMoveListener implements Listener {
	private DuelMe plugin;

	public DuelMeMoveListener(DuelMe plugin){
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event){
		if(event.getPlayer().getWorld() != plugin.lobbyspawn.getWorld())
			return;
		if(plugin.halt.contains(event.getPlayer().getName())){
			event.setTo(event.getFrom());
			return;
		}
		for(Arena i: plugin.arenas.values()){
			if(i.running)
				continue;
			if(onLoc(event.getTo(), i.spawn[0])){
				if(!i.player[0].equalsIgnoreCase("")){//part of my spectator takeover prevention system.
					break;
				}
				i.player[0] = event.getPlayer().getName();
				if(!i.player[1].equals("") && !i.pendingaccept) {
					initGame(event.getPlayer(), plugin.getServer().getPlayerExact(i.player[1]), i);
				}
				break;
			} if(onLoc(event.getTo(), i.spawn[1])){
				if(!i.player[1].equalsIgnoreCase("")){
					break;
				}
				i.player[1] = event.getPlayer().getName();
				if(!i.player[0].equals("") && !i.pendingaccept) {
					initGame(event.getPlayer(), plugin.getServer().getPlayerExact(i.player[0]), i);
				}
				break;
			}
		}
		for(Arena i: plugin.arenas.values()){
			if(i.running)
				continue;
			//very obscure code here.. to clarify, the last check is to make sure that the player who was previously on was the one who moved off, and
			//make sure that it was someone not on the block before. A.K.A. a spectator
			if(onLoc(event.getFrom(), i.spawn[0]) && !onLoc(event.getTo(), i.spawn[0]) && event.getPlayer().getName().equals(i.player[0])){
			//if(onLoc(event.getFrom(), i.spawn[0]) && !onLoc(event.getTo(), i.spawn[0])){
				i.player[0] = "";
				i.pendingaccept = false;
				i.block[0].getBlock().setType(Material.AIR);
				i.block[1].getBlock().setType(Material.AIR);
				i.accepted[0] = false;//PROBLEM hi
				i.accepted[1] = false;
				if(!i.player[1].equals(""))
					plugin.getServer().getPlayerExact(i.player[1]).sendMessage(ChatColor.RED + "[DuelMe] Other player moved off of the starting point");
				break;
			} if(onLoc(event.getFrom(), i.spawn[1]) && !onLoc(event.getTo(), i.spawn[1]) && event.getPlayer().getName().equals(i.player[1])){
			//} if(onLoc(event.getFrom(), i.spawn[1]) && !onLoc(event.getTo(), i.spawn[1])){
				i.player[1] = "";
				i.pendingaccept = false;
				i.block[0].getBlock().setType(Material.AIR);
				i.block[1].getBlock().setType(Material.AIR);
				i.accepted[0] = false;
				i.accepted[1] = false;
				if(!i.player[0].equals(""))
					plugin.getServer().getPlayerExact(i.player[0]).sendMessage(ChatColor.RED + "[DuelMe] Other player moved off of the starting point");
				break;
			}
		}
	}
	
	private boolean onLoc(Location pos1, Location pos2){
		if(pos1.getBlockX() != pos2.getBlockX())
			return false;
		if(pos1.getBlockY() != pos2.getBlockY())
			return false;
		if(pos1.getBlockZ() != pos2.getBlockZ())
			return false;
		return true;
	}
	
	private void initGame(Player player1, Player player2, Arena arena){
		player1.sendMessage(ChatColor.GREEN + "[DuelMe] You and " + ChatColor.GOLD + player2.getName() + ChatColor.GREEN + " are now ready for a duel.");
		player2.sendMessage(ChatColor.GREEN + "[DuelMe] You and " + ChatColor.GOLD + player1.getName() + ChatColor.GREEN + " are now ready for a duel.");
		player1.sendMessage(ChatColor.GREEN + "[DuelMe] To accept please hit the glowstone block");
		player2.sendMessage(ChatColor.GREEN + "[DuelMe] To accept please hit the glowstone block");
		arena.pendingaccept = true;
		arena.block[0].getBlock().setType(Material.GLOWSTONE);
		arena.block[1].getBlock().setType(Material.GLOWSTONE);
	
		//if (useHorsesOrSomething) {
		Horse horse1 = initHorse(player1);
		Horse horse2 = initHorse(player2);
		
		horse1.setPassenger(player1);
		horse2.setPassenger(player2);
		//}
	}
	
	private Horse initHorse(Player player) {
		Horse horse = (Horse) player.getWorld().spawn(player.getLocation(), Horse.class);
		horse.setAdult();
		horse.setTamed(true);
		horse.setVariant(Variant.SKELETON_HORSE);
		horse.getInventory().setItem(0, new ItemStack(Material.SADDLE));
		horse.getInventory().setItem(0, new ItemStack(Material.DIAMOND_BARDING));
		
		return horse;
	}
}
