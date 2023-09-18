package cc.mcyx.fastafk.info;

import cc.mcyx.fastafk.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * 挂机池信息类
 */
public class AfkInfo {
    private final String name;
    private double money;
    private String permission;
    private int points;
    private long delay;
    private int exp;
    private String join_title;
    private String join_sub_title;
    private String give_award_title;
    private String give_award_sub_title;
    private String no_permission_title;
    private String no_permission_sub_title;

    private final List<CommandAward> commandAwards = new LinkedList<>();

    public AfkInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 加载挂机池配置信息
     *
     * @return 是否加载成功
     */
    public boolean load() {
        Main.fastAfk.reloadConfig();
        FileConfiguration config = Main.fastAfk.getConfig();
        boolean enable = config.getBoolean("afk." + this.name + ".enable", false);
        if (!enable) {
            return false;
        }
        this.permission = config.getString("afk." + this.name + ".permission", "");
        this.money = config.getDouble("afk." + this.name + ".money", 0);
        this.points = config.getInt("afk." + this.name + ".points", 0);
        this.exp = config.getInt("afk." + this.name + ".exp", 0);
        this.delay = config.getLong("afk." + this.name + ".delay", 0);

        this.join_title = config.getString("afk." + this.name + ".join_title", "§a你已进入挂机池");
        this.join_sub_title = config.getString("afk." + this.name + ".join_sub_title", "");
        this.give_award_title = config.getString("afk." + this.name + ".give_award_title", "§e奖励已发放");
        this.give_award_sub_title = config.getString("afk." + this.name + ".give_award_sub_title", "");
        this.no_permission_title = config.getString("afk." + this.name + ".no_permission_title", "§c没有权限啊");
        this.no_permission_sub_title = config.getString("afk." + this.name + ".no_permission_sub_title", "");


        List<String> commands = config.getStringList("afk." + this.name + ".commands");
        for (String command : commands) {
            String[] cmdSplit = command.split(" ");
            String type = cmdSplit[0].toUpperCase();
            switch (type) {
                case "OP": {
                    CommandAward commandAward = new CommandAward(command.replace(command.substring(0, 3), ""), CommandAward.Type.valueOf(type));
                    this.commandAwards.add(commandAward);
                    break;
                }

                case "PLAYER": {
                    CommandAward commandAward = new CommandAward(command.replace(command.substring(0, 7), ""), CommandAward.Type.valueOf(type));
                    this.commandAwards.add(commandAward);
                    break;
                }

                case "CONSOLE": {
                    CommandAward commandAward = new CommandAward(command.replace(command.substring(0, 8), ""), CommandAward.Type.valueOf(type));
                    this.commandAwards.add(commandAward);
                    break;
                }

                default: {
                    Main.fastAfk.getLogger().warning("无法导入该命令 未知命令类型: " + this.name + " 原格式: " + command);
                    break;
                }
            }
        }
        return true;
    }


    /**
     * 用于将 带有 %xxx% 变量的文本中的变量替换为papi对应的玩家信息数据
     *
     * @param msg    原文本
     * @param player 数据源获取对象
     * @return 返回替换完成的文本
     */
    public static String replaceAPIMessage(String msg, Player player) {
        String form = msg;
        form = form.replace("&", "§");
        String[] msg_fg = form.split("%");
        for (int i = 0; i < msg_fg.length; i++) {
            if ((i + 1) % 2 == 0) {
                String PAPI = "%" + msg_fg[i] + "%";
                form = form.replace(PAPI, PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(player.getUniqueId()), PAPI));
            }
        }
        if (form.contains("%")) {
            msg_fg = form.split("%");
            for (int i = 0; i < msg_fg.length; i++) {
                if ((i + 1) % 2 == 0) {
                    String PAPI = "%" + msg_fg[i] + "%";
                    Main.fastAfk.getLogger().warning("配置变量异常," + " 错误变量: >> " + PAPI);
                }
            }
            player.sendMessage("§4变量配置异常,请联系管理员！");
        }
        return form;
    }

    /**
     * 获取此挂机池的给予货币树
     *
     * @return 货币
     */

    public double getMoney() {
        return money;
    }

    /**
     * 获取此挂机池的给予点券
     *
     * @return 点券
     */

    public int getPoints() {
        return points;
    }

    /**
     * 获取此挂机池给予的延迟
     *
     * @return 延迟时间ms
     */

    public long getDelay() {
        return delay;
    }

    /**
     * 获取此挂机池的经验
     *
     * @return 经验
     */

    public int getExp() {
        return exp;
    }

    /**
     * 获取加入大标题
     *
     * @param player 玩家
     * @return 大标题
     */

    public String getJoin_title(Player player) {
        return replaceAPIMessage(join_title, player);
    }

    /**
     * 获取加入小标题
     *
     * @param player 玩家
     * @return 小标i他
     */

    public String getJoin_sub_title(Player player) {
        return replaceAPIMessage(join_sub_title, player);
    }

    /**
     * 获取给予奖励后的提示 大标题
     *
     * @param player 玩家
     * @return 大标题
     */

    public String getGive_award_title(Player player) {
        return replaceAPIMessage(give_award_title, player);
    }

    /**
     * 获取给予奖励后的提示 小标题
     *
     * @param player 玩家
     * @return 小标题
     */
    public String getGive_award_sub_title(Player player) {
        return replaceAPIMessage(give_award_sub_title, player);
    }

    /**
     * 获取此挂机池的所需权限
     *
     * @return 权限节点
     */

    public String getPermission() {
        return permission;
    }

    /**
     * 获取没有权限给予的提示 小标题
     *
     * @param player 玩家
     * @return 小标题
     */
    public String getNo_permission_sub_title(Player player) {
        return replaceAPIMessage(no_permission_sub_title, player);
    }


    /**
     * 获取没有权限给予的提示 大标题
     *
     * @param player 玩家
     * @return 大标题
     */
    public String getNo_permission_title(Player player) {
        return replaceAPIMessage(no_permission_title, player);
    }

    /**
     * 获取给予的命令奖励类型
     *
     * @return 命令奖励列表
     */
    public List<CommandAward> getCommandAwards() {
        return commandAwards;
    }
}
