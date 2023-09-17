package cc.mcyx.fastafk.listener;


import cc.mcyx.fastafk.Main;
import cc.mcyx.fastafk.event.PlayerJoinResidenceEvent;
import cc.mcyx.fastafk.event.PlayerLeaveResidenceEvent;
import cc.mcyx.fastafk.info.AfkInfo;
import cc.mcyx.fastafk.thread.PlayerAfkThread;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerListener implements Listener {
    //记录玩家是否进入领地与离开领地
    public static final HashMap<Player, ClaimedResidence> PLAYER_CLAIMED_RESIDENCE_HASH_MAP = new HashMap<>();

    //线程池
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        //加入线程池处理
        EXECUTOR_SERVICE.execute(() -> {
            Location location = event.getPlayer().getLocation();
            ClaimedResidence residence = Residence.getInstance().getResidenceManager().getByLoc(location);
            Player player = event.getPlayer();
            //玩家进入领地
            if (residence != null) {
                //玩家不在任何领地里，加入领地名
                if (!PLAYER_CLAIMED_RESIDENCE_HASH_MAP.containsKey(player) || !PLAYER_CLAIMED_RESIDENCE_HASH_MAP.get(player).getName().equals(residence.getName())) {
                    PLAYER_CLAIMED_RESIDENCE_HASH_MAP.put(player, residence);
                    Bukkit.getScheduler().runTask(Main.fastAfk, () -> Bukkit.getPluginManager().callEvent(new PlayerJoinResidenceEvent(PlayerJoinResidenceEvent.class.getName(), PLAYER_CLAIMED_RESIDENCE_HASH_MAP.get(player), player)));
                }
            } else {
                //玩家领地缓存 玩家离开领地
                if (PLAYER_CLAIMED_RESIDENCE_HASH_MAP.containsKey(player)) {
                    Bukkit.getScheduler().runTask(Main.fastAfk, () -> {
                        Bukkit.getPluginManager().callEvent(new PlayerLeaveResidenceEvent(PlayerLeaveResidenceEvent.class.getName(), PLAYER_CLAIMED_RESIDENCE_HASH_MAP.get(player), player));
                        PLAYER_CLAIMED_RESIDENCE_HASH_MAP.remove(player);
                    });
                }
            }
        });
    }

    @EventHandler
    public void onJoinResidence(PlayerJoinResidenceEvent event) {
        //玩家进入某个领地，判断是否为挂机池领地
        AfkInfo afkInfo = new AfkInfo(event.getResidence().getName().replace(".", "_"));
        //挂机池领地配置是否启动或者存在
        if (afkInfo.load()) {
            Player player = event.getPlayer();
            //判断是否拥有权限
            if (!afkInfo.getPermission().equals("") && !player.hasPermission(afkInfo.getPermission())) {
                player.sendTitle(
                        afkInfo.getNo_permission_title(player),
                        afkInfo.getNo_permission_sub_title(player),
                        5, 20, 5);
                return;
            }
            //启动挂机线程
            PlayerAfkThread playerAfkThread = new PlayerAfkThread(event.getPlayer(), event.getResidence(), afkInfo);
            playerAfkThread.start();
        }
    }

    @EventHandler
    public void onLeaveResidence(PlayerLeaveResidenceEvent event) {
        Player player = event.getPlayer();
        //玩家离开直接关闭这个挂机线程
        if (PlayerAfkThread.PLAYER_PLAYER_AFK_THREAD_HASH_MAP.containsKey(player)) {
            PlayerAfkThread.PLAYER_PLAYER_AFK_THREAD_HASH_MAP.get(player).stop();
        }
    }
}
