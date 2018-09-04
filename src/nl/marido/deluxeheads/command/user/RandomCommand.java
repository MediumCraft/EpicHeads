package nl.marido.deluxeheads.command.user;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.marido.deluxeheads.DeluxeHeads;
import nl.marido.deluxeheads.cache.CacheHead;
import nl.marido.deluxeheads.command.AbstractCommand;
import nl.marido.deluxeheads.config.MainConfig;
import nl.marido.deluxeheads.config.lang.Lang;

public class RandomCommand extends AbstractCommand {

	private static final Random RANDOM = new Random();

	@Override
	public String getCommandLabel(MainConfig config) {
		return config.getRandomCommand();
	}

	@Override
	public String getPermission() {
		return "heads.random";
	}

	@Override
	public Lang.HelpSection getHelp() {
		return Lang.Command.Random.help();
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 1 && args.length != 2) {
			sendInvalidArgs(sender);
			return true;
		}

		if (DeluxeHeads.getCache().getHeadCount() == 0) {
			Lang.Command.Random.noHeads().send(sender);
			return true;
		}

		CacheHead random = DeluxeHeads.getCache().getRandomHead(RANDOM);

		if (args.length == 1) {
			if (!(sender instanceof Player)) {
				Lang.Command.Errors.mustBePlayer().send(sender);
				return true;
			}

			Lang.Command.Random.retrievingOwn(random).send(sender);

			((Player) sender).getInventory().addItem(random.getItemStack());
			return true;
		}

		Player player = Bukkit.getPlayer(args[1]);

		if (player == null) {
			Lang.Command.Random.cantFindPlayer(args[1]).send(sender);
			return true;
		}

		Lang.Command.Random.retrieving(random).send(player);
		Lang.Command.Random.give(player, random).send(sender);

		player.getInventory().addItem(random.getItemStack());
		return true;
	}
}
