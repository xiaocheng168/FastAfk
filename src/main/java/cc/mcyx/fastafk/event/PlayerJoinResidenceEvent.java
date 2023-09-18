package cc.mcyx.fastafk.event;

import com.bekvon.bukkit.residence.event.ResidencePlayerEvent;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.entity.Player;

/**
 * 玩家进入某个领地事件
 */
public class PlayerJoinResidenceEvent extends ResidencePlayerEvent {
    public PlayerJoinResidenceEvent(String eventName, ClaimedResidence resref, Player player) {
        super(eventName, resref, player);
    }
}
