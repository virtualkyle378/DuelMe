package me.kyle.DuelMe.Commands;

import me.kyle.DuelMe.Arena;
import me.kyle.DuelMe.DuelMe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelMeModifyCommand {
	
	DuelMeCommandExecutor commandexecutor;
	DuelMe plugin;
	
	public DuelMeModifyCommand(DuelMeCommandExecutor commandexecutor, DuelMe plugin) {
		this.commandexecutor = commandexecutor;
		this.plugin = plugin;
	}
	
	public void onCommand(CommandSender sender, Command cmd, String label, String[] arg, Player player) {
		//dm modify <arenaname> <flag>
		String arenaname = arg[1];
		String flag = arg[2];
		if(!plugin.arenas.containsKey(arg[1])){
			commandexecutor.arenacreations.put(player.getName(), new DuelMeArenaCreator(player, plugin, commandexecutor, arenaname));
			return;
		}
		Arena arena = plugin.arenas.get(arenaname);
		if (flag.equalsIgnoreCase("pos1")) {
			arena.pos[0] = player.getLocation();
			sender.sendMessage("[DuelArena] Pos1 set");
		} else if (flag.equalsIgnoreCase("pos2")) {//TODO implement inventory shizz... multiplayer support
			arena.pos[1] = player.getLocation();
			sender.sendMessage("[DuelArena] Pos2 set");
			if (plugin.util.checkCoords(arena.pos[0], arena.pos[1])) {
				sender.sendMessage("[DuelArena] Coordantes have been successfully created");
			} else {
				sender.sendMessage("[DuelArena] Coordnates are not representing the cube correctly");
				sender.sendMessage("[DuelArena] Please try representing the figure differently");
				sender.sendMessage("[DuelArena] If it is not correct YOU WILL BREAK THINGS");
				arena.pos[0] = null;
				arena.pos[1] = null;
			}
		} else if (flag.equalsIgnoreCase("spawn1")) {
			arena.spawn[0] = player.getLocation();
			sender.sendMessage("[DuelArena] Location of spawn1 set");
		} else if (flag.equalsIgnoreCase("spawn2")) {
			arena.spawn[1] = player.getLocation();
			sender.sendMessage("[DuelArena] Location of spawn2 set");
		} else if (flag.substring(0, 5).equalsIgnoreCase("door1")) {
			try {
				int pos = Integer.parseInt(flag.replace("door1", "")) - 1;
				arena.door[0][pos] = player.getTargetBlock(null, 5).getLocation();
				sender.sendMessage("[DuelArena] Location of door1 set");
			} catch (NumberFormatException e) {
				sender.sendMessage("UFAIL");
			}

		} else if (flag.substring(0, 5).equalsIgnoreCase("door2")) {
			try {
				int pos = Integer.parseInt(flag.replace("door2", "")) - 1;
				try {
					arena.door[1][pos] = player.getTargetBlock(null, 5).getLocation();
				} catch (Exception e) {
					e.printStackTrace();
					sender.sendMessage("U FAIELASLDASKDJASFHLKAJFHGKLJASHFKLJHASD KTHXBAI");
				}
				sender.sendMessage("[DuelArena] Location of door2 set");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else if (flag.equalsIgnoreCase("block1")) {
			try {
				try {
					arena.block[0] = player.getTargetBlock(null, 5).getLocation();
				} catch (Exception e) {
					e.printStackTrace();
					sender.sendMessage("U FAIELASLDASKDJASFHLKAJFHGKLJASHFKLJHASD KTHXBAI");
				}
				sender.sendMessage("[DuelArena] Location of block1 set");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

		} else if (flag.equalsIgnoreCase("block2")) {
			try {
				try {
					arena.block[1] = player.getTargetBlock(null, 5).getLocation();
				} catch (Exception e) {
					e.printStackTrace();
					sender.sendMessage("U FAIELASLDASKDJASFHLKAJFHGKLJASHFKLJHASD KTHXBAI");
				}

				sender.sendMessage("[DuelArena] Location of block2 set");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else if (flag.equalsIgnoreCase("inventory")) {
			try {
				try {
					arena.inventory = player.getInventory().getContents();
					arena.armor = player.getInventory().getArmorContents();
				} catch (Exception e) {
					e.printStackTrace();
					sender.sendMessage("U FAIELASLDASKDJASFHLKAJFHGKLJASHFKLJHASD KTHXBAI");
				}

				sender.sendMessage("[DuelArena] Location of inv blolk set");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else if (flag.equalsIgnoreCase("getinventory")) {
			try {
				try {
					player.getInventory().setContents(arena.inventory);
					player.getInventory().setArmorContents(arena.armor);
				} catch (Exception e) {
					e.printStackTrace();
					sender.sendMessage("U FAIELASLDASKDJASFHLKAJFHGKLJASHFKLJHASD KTHXBAI");
				}

				sender.sendMessage("[DuelArena] Location of inv blolk set");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else if (flag.equalsIgnoreCase("stands")) {
			try {
				arena.stands = player.getLocation();
				sender.sendMessage("[DuelArena] Location of stands set");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			plugin.util.sendMessage("I jus dun geddit...{pos1,pos2,spawn1,spawn2,block1,block2,door(1/2)(1/2),stands}", ChatColor.RED, player);
		}
	}
}
