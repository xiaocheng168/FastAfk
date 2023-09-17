package cc.mcyx.fastafk.event;

import com.bekvon.bukkit.residence.event.ResidencePlayerEvent;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.entity.Player;

public class PlayerLeaveResidenceEvent extends ResidencePlayerEvent {
    public PlayerLeaveResidenceEvent(String eventName, ClaimedResidence resref, Player player) {
        super(eventName, resref, player);
    }
}
