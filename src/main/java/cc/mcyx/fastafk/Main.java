package cc.mcyx.fastafk;

import cc.mcyx.fastafk.listener.PlayerListener;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Economy economy;

    public static Main fastAfk;

    public static PlayerPointsAPI playerPointsAPI;

    public static boolean stop = false;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        fastAfk = this;
    }

    @Override
    public void onEnable() {
        RegisteredServiceProvider<Economy> registration = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (registration != null) {
            economy = registration.getProvider();
            getLogger().info("已找到 Vault 经济体 √");
        }
        Plugin playerPointsPlugin = Bukkit.getPluginManager().getPlugin("PlayerPoints");
        if (playerPointsPlugin != null) {
            playerPointsAPI = PlayerPoints.getInstance().getAPI();
            getLogger().info("已找到 PlayerPoints 经济体 √");
        }
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        stop = true;
    }
}
