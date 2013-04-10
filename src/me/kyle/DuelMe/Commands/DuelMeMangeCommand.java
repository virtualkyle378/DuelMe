package me.kyle.DuelMe.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kyle.DuelMe.DuelMe;

public class DuelMeMangeCommand {
	DuelMeCommandExecutor commandexecutor;
	DuelMe plugin;
	
	public DuelMeMangeCommand(DuelMeCommandExecutor commandexecutor, DuelMe plugin) {
		this.commandexecutor = commandexecutor;
		this.plugin = plugin;
	}
	
	public void onCommand(CommandSender sender, Command cmd, String label, String[] arg, Player player) {
		
	}
}
