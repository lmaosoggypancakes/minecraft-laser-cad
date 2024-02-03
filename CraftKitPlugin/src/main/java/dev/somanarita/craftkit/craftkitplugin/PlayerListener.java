package dev.somanarita.craftkit.craftkitplugin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class PlayerListener implements Listener {
	private Map<String, World> playerWorldMap;
	private String book_str = "{pages:"
			+ "['{\"text\":\"How to use CraftKit\n"
			+ "1. Build!\n"
			+ "You can build with any solid or slabs. "
			+ "Please build in the 10x10x10 space given.\n"
			+ "2. Export!\n"
			+ "Right click on the yellow wool to send your creation to our website!\n"
			+ "3. Order your parts!\n"
			+ "Click on the link on the next page.\"}'"
			+ ", '{\"text\":\"Click on this!\",\"color\":\"blue\",\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://gaming.stackexchange.com/\"}}'], "
			+ "title: 'test', author: 'test'}";
	private Map<String, JsonArray> blockDataMap;
	private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
	public PlayerListener() {
		playerWorldMap = new HashMap<String, World>();
		blockDataMap = new HashMap<String, JsonArray>();
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.setGameMode(GameMode.CREATIVE);
		player.setFlying(true);
		System.out.println(String.format("%s joined! Enjoy CraftKit!", player.getName()));
		WorldCreator cadWorldCreator = new WorldCreator("plugins/CraftKitPlugin/maps/cad");
		World cadWorld = Bukkit.getServer().createWorld(cadWorldCreator);
		playerWorldMap.put(player.getName(), cadWorld);
		blockDataMap.put(player.getName(), new JsonArray());
		cadWorld.setDifficulty(Difficulty.PEACEFUL);
		cadWorld.setTime(1200);
		cadWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		cadWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		cadWorld.setAutoSave(false);
		Location newLocation = new Location(cadWorld, -5, -55, -5, -45, 45);
		player.teleport(newLocation);
		player.getInventory().clear();
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), 
				"item replace entity " + player.getName() + " hotbar.8 with minecraft:written_book" + book_str);
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), 
				"item replace entity " + player.getName() + " hotbar.7 with minecraft:yellow_wool{Enchanted: {id: mending}}");
		Inventory inv = player.getInventory();
		ItemStack book = inv.getItem(8);
		player.openBook(book);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerJoinEvent event) {
		World cadWorld = playerWorldMap.get(event.getPlayer().getName());
		Bukkit.getServer().unloadWorld(cadWorld, false);
		Bukkit.getServer().createWorld(new WorldCreator("plugins/CraftKitPlugin/maps/cad"));
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block blockPlaced = event.getBlockPlaced();
		Location blockPos = blockPlaced.getLocation();
		int x = blockPos.getBlockX();
		int y = blockPos.getBlockY();
		int z = blockPos.getBlockZ();
		int normalized_x = x;
		int normalized_y = y + 61;
		int normalized_z = z;
		System.out.println(String.format("%d %d %d", normalized_x, normalized_y, normalized_z));
		if (!(0 <= normalized_x && normalized_x < 10) ||
			!(0 <= normalized_y && normalized_y < 10) || 
			!(0 <= normalized_z && normalized_z < 10)) {
			blockPlaced.setType(Material.AIR);
			player.sendMessage(ChatColor.RED + "Cannot place block out of the building zone!");
		} else {
			JsonObject obj = new JsonObject();
			obj.addProperty("x", normalized_x);
			obj.addProperty("y", normalized_y);
			obj.addProperty("z", normalized_z);
			obj.addProperty("blockId", event.getBlockPlaced().getType().name());
			blockDataMap.get(player.getName()).add(obj);
		}
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		HumanEntity player = event.getWhoClicked();
		if (inv.getItem(8) == null || inv.getItem(8).getType() != Material.WRITTEN_BOOK) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), 
					"item replace entity " + player.getName() + " hotbar.8 with minecraft:written_book" + book_str);
		}
	}
	@EventHandler
	public void onPLayerUse(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		JsonArray arr = blockDataMap.get(p.getName());
		try {
			if (p.getInventory().getItemInMainHand().getType() == Material.YELLOW_WOOL) {
				HttpRequest request = HttpRequest.newBuilder()
						.POST(HttpRequest.BodyPublishers.ofString(arr.toString()))
						.uri(URI.create("https://localhost:3000/blockData"))
						.setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
						.header("Content-Type", "application/x-www-form-urlencoded")
						.build();
				HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
				if (response.statusCode() == 200) {
					p.sendMessage(ChatColor.GREEN + "Building was exported successfully!");
				} else {
					p.sendMessage(ChatColor.RED + "Export failed. Please check your internet connection.");
				}
			}
		} catch (Exception e) {
			p.sendMessage(ChatColor.RED + "Export failed. Please check your internet connection.");
		}
	}
}
