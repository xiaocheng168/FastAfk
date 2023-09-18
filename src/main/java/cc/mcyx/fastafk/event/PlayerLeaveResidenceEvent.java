package cc.mcyx.fastafk.event;

import com.bekvon.bukkit.residence.event.ResidencePlayerEvent;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.entity.Player;

/**
 * 玩家离开某个领地事件
 */
public class PlayerLeaveResidenceEvent extends ResidencePlayerEvent {
    public PlayerLeaveResidenceEvent(String eventName, ClaimedResidence resref, Player player) {
        super(eventName, resref, player);
    }
}
