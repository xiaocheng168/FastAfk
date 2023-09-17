package cc.mcyx.fastafk.event;

import com.bekvon.bukkit.residence.event.ResidencePlayerEvent;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.entity.Player;

public class PlayerJoinResidenceEvent extends ResidencePlayerEvent {
    public PlayerJoinResidenceEvent(String eventName, ClaimedResidence resref, Player player) {
        super(eventName, resref, player);
    }
}
