package cc.mcyx.fastafk.info;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * 命令奖励类
 */
public class CommandAward {
    private final String cmd;
    private final Type type;

    public CommandAward(String cmd, Type type) {
        this.cmd = cmd;
        this.type = type;
    }

    /**
     * 执行此命令奖励
     *
     * @param player 操作玩家
     */
    public void run(Player player) {
        String replaceCommand = AfkInfo.replaceAPIMessage(cmd, player);
        System.out.println(replaceCommand);
        switch (type) {
            case OP: {
                //玩家是否为OP
                boolean isOP = player.isOp();
                try {
                    //给予临时OP
                    player.setOp(true);
                    player.performCommand(replaceCommand);
                } finally {
                    //如果出现异常依旧按照玩家是否为op处理
                    player.setOp(isOP);
                }
                break;
            }
            case CONSOLE: {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replaceCommand);
                break;
            }
            case PLAYER: {
                player.performCommand(replaceCommand);
                break;
            }
        }
    }


    /**
     * 获取命令
     *
     * @return 命令
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * 获取发送类型
     *
     * @return 类型
     */
    public Type getType() {
        return type;
    }

    /**
     * 发送类型
     */
    public enum Type {
        PLAYER, CONSOLE, OP
    }
}
