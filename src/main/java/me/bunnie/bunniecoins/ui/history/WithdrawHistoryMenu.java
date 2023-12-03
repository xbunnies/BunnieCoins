package me.bunnie.bunniecoins.ui.history;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.withdraw.Withdraw;
import me.bunnie.bunniecoins.utils.ItemBuilder;
import me.bunnie.bunniecoins.utils.ui.menu.Button;
import me.bunnie.bunniecoins.utils.ui.menu.page.PageMenu;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithdrawHistoryMenu extends PageMenu {

    private final BCPlayer bcPlayer;
    private final BCPlugin plugin;
    private final OfflinePlayer target;

    public WithdrawHistoryMenu(Player player, OfflinePlayer target) {
        super(36, player);
        this.plugin = BCPlugin.getInstance();
        this.target = target;
        this.bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(target.getUniqueId());

        bcPlayer.loadWithdrawals();
    }

    private List<Button> getPageableButtons() {
        List<Button> buttons = new ArrayList<>();
        List<Withdraw> withdrawList = bcPlayer.getSortedWithdrawals();
        String path = "withdraw-history.buttons.withdraw";
        withdrawList.forEach(withdraw -> buttons.add(new Button() {
            @Override
            public ItemStack getItem(Player player) {
                String name = plugin.getMenusYML().getString(path + ".name")
                        .replace("%withdraw.id%", withdraw.getId().toString());
                Material material = Material.valueOf(plugin.getMenusYML().getString(path + ".material"));
                List<String> toReplace = plugin.getMenusYML().getStringList(path + ".lore");
                ArrayList<String> lore = new ArrayList<>();
                boolean enchanted = plugin.getMenusYML().getBoolean(path + ".enchanted");
                for (String s : toReplace) {
                    s = s.replace("%withdraw.id%", withdraw.getId().toString());
                    s = s.replace("%withdraw.amount%", String.valueOf(withdraw.getAmount()));
                    s = s.replace("%withdraw.date%", withdraw.getFormattedDate());
                    s = s.replace("%withdraw.withdrawn%", (withdraw.isWithdrawn() ?
                            plugin.getConfigYML().getString("placeholders.withdraw-history.withdrawn-status.true") :
                            plugin.getConfigYML().getString("placeholders.withdraw-history.withdrawn-status.false")
                    ));
                    lore.add(s);
                }
                return new ItemBuilder(material)
                        .setName(name)
                        .setLore(lore)
                        .setGlow(enchanted)
                        .build();
            }}));
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        int counter = 0;

        for(Button button : getPageableButtons()) {
            buttons.put(counter, button);
            counter++;
        }
        return buttons;
    }

    @Override
    public String getTitle() {
        return plugin.getMenusYML().getString("withdraw-history.title")
                .replace("%menu.page%", String.valueOf(getPage()))
                .replace("%menu.total-pages%", String.valueOf(getPages()))
                .replace("%player%", target.getName());
    }

}