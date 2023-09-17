package cc.mcyx.fastafk.thread;

import cc.mcyx.fastafk.Main;
import cc.mcyx.fastafk.info.AfkInfo;
import cc.mcyx.fastafk.info.CommandAward;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Time;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

//奖励线程，每个玩家单独计算
public class PlayerAfkThread extends TimerTask {
    //定时器表
    public static final Timer TIMER = new Timer();
    //已存在的玩家
    public static final HashMap<Player, PlayerAfkThread> PLAYER_PLAYER_AFK_THREAD_HASH_MAP = new HashMap<>();

    Player player;
    ClaimedResidence residence;
    AfkInfo afkInfo;

    public PlayerAfkThread(Player player, ClaimedResidence residence, AfkInfo afkInfo) {
        this.player = player;
        this.residence = residence;
        this.afkInfo = afkInfo;
    }

    //开启挂机
    public void start() {
        player.sendTitle(this.afkInfo.getJoin_title(player), this.afkInfo.getJoin_sub_title(player), 5, 20, 5);
        //防止卡多个挂机线程
        if (PLAYER_PLAYER_AFK_THREAD_HASH_MAP.containsKey(this.player)) {
            PLAYER_PLAYER_AFK_THREAD_HASH_MAP.get(player).cancel();
        }
        //将玩家加入
        PLAYER_PLAYER_AFK_THREAD_HASH_MAP.put(player, this);
        TIMER.schedule(this, afkInfo.getDelay(), afkInfo.getDelay());
    }

    //关闭挂机
    public void stop() {
        PLAYER_PLAYER_AFK_THREAD_HASH_MAP.remove(player);
        this.cancel();
    }

    @Override
    public void run() {
        if (Residence.getInstance().getResidenceManager().getByLoc(this.player) != this.residence) {
            this.stop();
            return;
        }
        if (Main.stop) {
            return;
        }
        Bukkit.getScheduler().runTask(Main.fastAfk, () -> {
            //玩家离线，直接跳出线程
            if (!this.player.isOnline()) {
                this.stop();
                return;
            }
            //给予经验
            if (this.afkInfo.getExp() > 0) {
                player.giveExp(this.afkInfo.getExp());
            }
            //给予货币
            if (this.afkInfo.getMoney() > 0 && Main.economy != null) {
                Main.economy.depositPlayer(player, this.afkInfo.getMoney());
            }
            //给予点券
            if (this.afkInfo.getPoints() > 0 && Main.playerPointsAPI != null) {
                Main.playerPointsAPI.give(player.getUniqueId(), this.afkInfo.getPoints());
            }
            //执行命令
            if (!afkInfo.getCommandAwards().isEmpty()) {
                for (CommandAward commandAward : afkInfo.getCommandAwards()) {
                    commandAward.run(player);
                }
            }
            //发送通知
            player.sendTitle(this.afkInfo.getGive_award_title(player), this.afkInfo.getGive_award_sub_title(player), 5, 20, 5);
        });

    }
}
