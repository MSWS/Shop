package xyz.msws.gui.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.common.base.Preconditions;

public class Utils {
	public static boolean has(CommandSender sender, String perm, boolean verbose) {
		Preconditions.checkNotNull(sender);
		Preconditions.checkNotNull(perm);
		Preconditions.checkNotNull(verbose);

		if (verbose && !sender.hasPermission(perm)) {
			MSG.tell(sender, "Permissions", MSG.ERROR + "You do not have permission.");
			return false;
		}
		return sender.hasPermission(perm);
	}

	public static boolean isLookingAt(Player player, Location target) {
		Location eye = player.getEyeLocation();
		Vector toEntity = target.toVector().subtract(eye.toVector());
		double dot = toEntity.normalize().dot(eye.getDirection());

		return dot > 0.99D;
	}

	public static GameMode getMode(String mode) {
		for (GameMode m : GameMode.values()) {
			if (m.toString().toLowerCase().startsWith(mode.toLowerCase()))
				return m;
		}
		return null;
	}

	public static Player matchPlayer(CommandSender sender, String name, boolean verbose) {
		List<Player> matches = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().equalsIgnoreCase(name))
				return p;
			if (p.getName().toLowerCase().contains(name.toLowerCase())) {
				matches.add(p);
			}
		}

		if (matches.size() == 1)
			return matches.get(0);

		if (!verbose)
			return null;

		if (matches.size() == 0) {
			MSG.tell(sender, "Online Player Search",
					"Could not find any players that matched " + MSG.FORMAT_INFO + name);
			return null;
		}

		String separator = MSG.FORMAT_SEPARATOR + ", " + MSG.PLAYER;

		String match = "[" + MSG.PLAYER;
		for (Player p : matches)
			match += p.getName() + separator;

		if (match.length() > separator.length())
			match = match.substring(0, match.length() - separator.length());
		MSG.tell(sender, "Online Player Search", MSG.FORMAT_INFO + name + " " + MSG.DEFAULT + "matched " + MSG.NUMBER
				+ matches.size() + " " + MSG.DEFAULT + "names. " + match);
		return null;
	}

	@SuppressWarnings({ "unchecked" })
	public static Map<String, Object> mapValues(Object o, boolean deep) {
		if (o instanceof MemorySection) {
			return ((MemorySection) o).getValues(deep);
		}
		if (o instanceof Map<?, ?>)
			return (HashMap<String, Object>) o;
		return null;
	}
}
