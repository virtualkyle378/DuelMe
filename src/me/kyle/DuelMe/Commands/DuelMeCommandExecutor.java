package me.kyle.DuelMe.Commands;

import java.util.HashMap;
import java.util.Map.Entry;

import me.kyle.DuelMe.Arena;
import me.kyle.DuelMe.DuelMe;
import me.kyle.DuelMe.Listeners.DuelMeInteractListener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class DuelMeCommandExecutor implements CommandExecutor {

	private DuelMe plugin;
	public HashMap<String, DuelMeArenaCreator> arenacreations = new HashMap<String, DuelMeArenaCreator>();
	private DuelMeModifyCommand modifycommand;
	@SuppressWarnings("unused")
	private DuelMeMangeCommand managecommand;
	@SuppressWarnings("unused")
	private DuelMeMiscCommand misccommand;
	

	public DuelMeCommandExecutor(DuelMe plugin) {
		this.plugin = plugin;
		modifycommand = new DuelMeModifyCommand(this, plugin);
		managecommand = new DuelMeMangeCommand(this, plugin);
		misccommand = new DuelMeMiscCommand(this, plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;//TODO make sure that only players can do player stuff
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		
		String[] arg = plugin.util.safeArgs(args, 4);
		//System.out.println(arg1.substring(0, 5));
		
		if(arenacreations.containsKey(sender.getName())){
			arenacreations.get(sender.getName()).proccess(args);
			return true;
		}
		
		//create <arena>
		//modify <arena> ..
		//if modify arena no exist create one 
		//rename <arenafrom> <arenato>
		//delete <arena>
		//list should be own class?
		//list part of manage

		if (arg[0].equalsIgnoreCase("modify")) {
			modifycommand.onCommand(sender, cmd, label, arg, player);
		}
		if (arg[0].contains("lobby")) {
				plugin.lobbyspawn = player.getLocation();
				sender.sendMessage("[DuelArena] Location of spawn set");
		}
		if (arg[0].contains("rename")) {
			if(!arg[1].equals("") && !arg[2].equals("")){
				if(plugin.arenas.containsKey(arg[1])){
					plugin.arenas.put(arg[2], plugin.arenas.remove(arg[1]));
					plugin.util.sendMessage("Renamed arena " + arg[1] + " to " + arg[2], ChatColor.GREEN, player);
				} else
					plugin.util.sendMessage("No arena found for: " + arg[1], ChatColor.RED, player);
			} else 
				plugin.util.sendMessage("Usage: " + ChatColor.ITALIC + "/dm rename <old> <new>", ChatColor.RED, player);
		}
		if (arg[0].contains("list")) {
			if(!plugin.arenas.isEmpty()){
				plugin.util.sendMessage("Arenas:", ChatColor.GREEN, player);
				for(String i: plugin.arenas.keySet()){
					plugin.util.sendMessage(i, ChatColor.GREEN, player);
				}
			} else
				plugin.util.sendMessage("No arenas to list..", ChatColor.RED, player);
		}
		if (arg[0].contains("create")) {
			if(!arg[1].equals(""))
				arenacreations.put(player.getName(), new DuelMeArenaCreator(player, plugin, this, arg[1]));
			else
				plugin.util.sendMessage("Usage: " + ChatColor.ITALIC + "/dm create <arena>", ChatColor.RED, player);
		}
		if (arg[0].contains("remove")) {
			if(!arg[1].equals("")){
				if(plugin.arenas.containsKey(arg[1])){
					plugin.arenas.remove(arg[1]);
					plugin.util.sendMessage("Removed arena " + arg[1], ChatColor.GREEN, player);
				}
			} else
				plugin.util.sendMessage("Usage: " + ChatColor.ITALIC + "/dm remove <arena>", ChatColor.RED, player);
		}
		if(arg[0].equalsIgnoreCase("checkcontents")){
			//int num = Integer.parseInt(arg1);
			System.out.println("contents of all arenas:");
			for(Entry<String, Arena> i: plugin.arenas.entrySet()){
				if(i == null){
					System.out.println("Arena: null");
					break;
				}
				Arena a = i.getValue();
				output(i.getKey(), "pos1", a.pos[0]);
				output(i.getKey(), "pos2", a.pos[1]);
				output(i.getKey(), "spawn1", a.spawn[0]);
				output(i.getKey(), "spawn2", a.spawn[1]);
				output(i.getKey(), "door11", a.door[0][0]);
				output(i.getKey(), "door12", a.door[0][1]);
				output(i.getKey(), "door21", a.door[1][0]);
				output(i.getKey(), "door22", a.door[1][1]);
				output(i.getKey(), "stands", a.stands);
				output(i.getKey(), "block1", a.block[0]);
				output(i.getKey(), "block2", a.block[1]);
				
				
			}
		}
//		if(arg[0].equalsIgnoreCase("pos")){
//			sender.sendMessage(plugin.arenas.get(0).player[0]);
//			sender.sendMessage(plugin.arenas.get(0).player[1]);
//		}
		if(arg[0].equalsIgnoreCase("start")){
			new DuelMeInteractListener(plugin).countdown(plugin.arenas.get("one"));
		}
		if(arg[0].equalsIgnoreCase("perms")){
			for(PermissionAttachmentInfo i: player.getEffectivePermissions()){
				System.out.println("Permission 1: " + i.getPermission());
				if(i.getAttachment() == null){
					continue;
				}
				for(Entry<String, Boolean> x: i.getAttachment().getPermissions().entrySet()){
					System.out.println("Permission: " + x.getKey() + ": " + x.getValue());
				}
			}
			
		}
		//System.out.println("hit nothing");
		return true;
	}
	
	private void output(String name, String prename, Location loc){
		if(loc == null)
			System.out.println(name + ": " + prename + ": null");
		else{
			System.out.println(name + ": " + prename + "." + "X: " + loc.getBlockX());
			System.out.println(name + ": " + prename + "." + "Y: " + loc.getBlockY());
			System.out.println(name + ": " + prename + "." + "Z: " + loc.getBlockZ());
		}
	}
}
