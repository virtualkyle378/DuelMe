package me.kyle.DuelMe.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import me.kyle.DuelMe.Arena;
import me.kyle.DuelMe.DuelMe;

public class DuelMeInteractListener implements Listener {
	private DuelMe plugin;

	public DuelMeInteractListener(DuelMe plugin){
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event){
		if(event.getPlayer().getLocation().getWorld() != plugin.lobbyspawn.getWorld() || event.getClickedBlock() == null)
			return;
		Arena arena = inArena(event);
		if (arena == null)
			return;
		if(!arena.pendingaccept)
			return;
		if(arena.player[0].equals(event.getPlayer().getName()) && event.getClickedBlock().getLocation().equals(arena.block[0])){
			arena.accepted[0] = true;
			arena.block[0].getBlock().setType(Material.AIR);
			if(arena.accepted[1]){
				for(Player i: plugin.lobbyspawn.getWorld().getPlayers()){
					if(i.getName().equals(arena.player[0]) || i.getName().equals(arena.player[1]))
						continue;
					if(withinRange(i, arena))
						i.teleport(arena.stands);
				}
				arena.door[0][0].getBlock().setType(Material.IRON_FENCE);
				arena.door[0][1].getBlock().setType(Material.IRON_FENCE);
				arena.door[1][0].getBlock().setType(Material.IRON_FENCE);
				arena.door[1][1].getBlock().setType(Material.IRON_FENCE);
				countdown(arena);
			}
		} else if(arena.player[1].equals(event.getPlayer().getName()) && event.getClickedBlock().getLocation().equals(arena.block[1])) {
			arena.accepted[1] = true;
			arena.block[1].getBlock().setType(Material.AIR);
			if(arena.accepted[0]){
				for(Player i: plugin.lobbyspawn.getWorld().getPlayers()){
					if(i.getName().equals(arena.player[0]) || i.getName().equals(arena.player[1]))
						continue;
					if(withinRange(i, arena))
						i.teleport(arena.stands);
				}
				arena.door[0][0].getBlock().setType(Material.IRON_FENCE);
				arena.door[0][1].getBlock().setType(Material.IRON_FENCE);
				arena.door[1][0].getBlock().setType(Material.IRON_FENCE);
				arena.door[1][1].getBlock().setType(Material.IRON_FENCE);
				countdown(arena);
			}
			//plugin.getServer().getWorld(plugin.arenas.get(index).block[1].getWorld().getName()).getBlockAt(plugin.arenas.get(index).block[1]).setType(Material.AIR);
		}
	}
	
	private boolean withinRange(Entity i, Arena arena){//paste
		
		return i.getLocation().getBlockX() <= arena.pos[1].getBlockX() &&
				i.getLocation().getBlockX() >= arena.pos[0].getBlockX() &&
				i.getLocation().getBlockZ() <= arena.pos[1].getBlockZ() &&
				i.getLocation().getBlockZ() >= arena.pos[0].getBlockZ();
	}
	
	private Arena inArena(PlayerInteractEvent event){
		for(Arena i: plugin.arenas.values()){
			if(i.player[0].equals(event.getPlayer().getName()) || i.player[1].equals(event.getPlayer().getName())){
				return i;
			}
		}
		return null;
	}
	
	public void countdown(final Arena arena){	
		
		//disabled inventory stuff
		plugin.getServer().getPlayerExact(arena.player[0]).getInventory().setContents(arena.inventory);
		plugin.getServer().getPlayerExact(arena.player[1]).getInventory().setContents(arena.inventory);
		plugin.getServer().getPlayerExact(arena.player[0]).getInventory().setArmorContents(arena.armor);
		plugin.getServer().getPlayerExact(arena.player[1]).getInventory().setArmorContents(arena.armor);
		
		arena.running = true;
		arena.pendingaccept = false;
		stopMe(arena);//incase of a concurrent call ofc
		int id = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			int countdown = 4;
			
			@Override
			public void run() {
				try {
					if(countdown == 4){
						plugin.halt.add(arena.player[0]);
						plugin.halt.add(arena.player[1]);
						plugin.getServer().getPlayerExact(arena.player[0]).sendMessage(ChatColor.GREEN + "[DuelMe] Ready?");
						plugin.getServer().getPlayerExact(arena.player[1]).sendMessage(ChatColor.GREEN + "[DuelMe] Ready?");
						countdown--;
					} else if(countdown == 3){
						plugin.getServer().getPlayerExact(arena.player[0]).sendMessage(ChatColor.GREEN + "[DuelMe] 3!");
						plugin.getServer().getPlayerExact(arena.player[1]).sendMessage(ChatColor.GREEN + "[DuelMe] 3!");
						countdown--;
					} else if(countdown == 2){
						plugin.getServer().getPlayerExact(arena.player[0]).sendMessage(ChatColor.GREEN + "[DuelMe] 2!");
						plugin.getServer().getPlayerExact(arena.player[1]).sendMessage(ChatColor.GREEN + "[DuelMe] 2!");
						countdown--;
					} else if(countdown == 1){
						plugin.getServer().getPlayerExact(arena.player[0]).sendMessage(ChatColor.GREEN + "[DuelMe] 1!");
						plugin.getServer().getPlayerExact(arena.player[1]).sendMessage(ChatColor.GREEN + "[DuelMe] 1!");
						countdown--;
					} else if(countdown == 0){
						plugin.getServer().getPlayerExact(arena.player[0]).sendMessage(ChatColor.GREEN + "[DuelMe] Go!");
						plugin.getServer().getPlayerExact(arena.player[1]).sendMessage(ChatColor.GREEN + "[DuelMe] Go!");
						stopMe(arena);
						countdown--;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}}, 0, 20);
		plugin.countdownid.put(arena, id);
	}
	
	private void stopMe(Arena arena){
		plugin.halt.remove(arena.player[0]);
		plugin.halt.remove(arena.player[1]);
		plugin.getServer().getScheduler().cancelTask(plugin.countdownid.get(arena));
	}
	
	@EventHandler 
	public void onInteract(PlayerInteractEvent event){
		if(event.getPlayer().getLocation().getWorld() != plugin.lobbyspawn.getWorld() || event.getClickedBlock() == null)
			return;
		for(Arena i: plugin.arenas.values()){
			if((sameblock(event.getClickedBlock(), i.block[0].getBlock()) || (sameblock(event.getClickedBlock(), i.block[1].getBlock()))) && 
					!(i.player[0].equals(event.getPlayer().getName()) || i.player[1].equals(event.getPlayer().getName()))){
				event.setCancelled(true);
			}
		}
	}
	
	private boolean sameblock(Block a, Block b){
		return a.getX() == b.getX() &&
				a.getY() == b.getY() &&
				a.getZ() == b.getZ();
	}
	
	@EventHandler
	public void onBowInteract(PlayerInteractEvent event){
		if(event.getPlayer().getLocation().getWorld() != plugin.lobbyspawn.getWorld())
			return;
		if(inArena(event) != null && plugin.halt.contains(event.getPlayer().getName())){
			event.setCancelled(true);
		}
			
	}
}
