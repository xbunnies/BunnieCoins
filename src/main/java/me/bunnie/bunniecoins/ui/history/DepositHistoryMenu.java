package me.bunnie.bunniecoins.ui.history;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.deposit.Deposit;
import me.bunnie.bunniecoins.utils.ItemBuilder;
import me.bunnie.bunniecoins.utils.ui.menu.Button;
import me.bunnie.bunniecoins.utils.ui.menu.page.PageMenu;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DepositHistoryMenu extends PageMenu {

    private final BCPlayer bcPlayer;
    private final BCPlugin plugin;
    private final OfflinePlayer target;

    public DepositHistoryMenu(Player player, OfflinePlayer target) {
        super(36, player);
        this.plugin = BCPlugin.getInstance();
        this.target = target;
        this.bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(target.getUniqueId());

        bcPlayer.loadDeposits();
    }

    private List<Button> getPageableButtons() {
        List<Button> buttons = new ArrayList<>();
        List<Deposit> depositList = bcPlayer.getSortedDeposits();
        String path = "deposit-history.buttons.deposit";
        depositList.forEach(deposit -> buttons.add(new Button() {
            @Override
            public ItemStack getItem(Player player) {
                String name = plugin.getMenusYML().getString(path + ".name")
                        .replace("%deposit.id%", deposit.getId().toString());
                Material material = Material.valueOf(plugin.getMenusYML().getString(path + ".material"));
                List<String> toReplace = plugin.getMenusYML().getStringList(path + ".lore");
                ArrayList<String> lore = new ArrayList<>();
                boolean enchanted = plugin.getMenusYML().getBoolean(path + ".enchanted");
                for (String s : toReplace) {
                    s = s.replace("%deposit.id%", deposit.getId().toString());
                    if(deposit.getWithdrawer().equals("CONSOLE")) {
                        s = s.replace("%deposit.source%", deposit.getWithdrawer());
                    } else {
                        s = s.replace("%deposit.source%", Objects.requireNonNull(deposit.getPlayer().getName()));
                    }
                    s = s.replace("%deposit.amount%", String.valueOf(deposit.getAmount()));
                    s = s.replace("%deposit.date%", deposit.getFormattedDate());
                    lore.add(s);
                }
                return new ItemBuilder(material)
                        .setName(name)
                        .setLore(lore)
                        .setGlow(enchanted)
                        .build();
            }
        }));
        return buttons;
    }

    @Override
    public void close() {
        bcPlayer.getDeposits().clear();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        int counter = 0;

        for (Button button : getPageableButtons()) {
            buttons.put(counter, button);
            counter++;
        }
        return buttons;
    }

    @Override
    public String getTitle() {
        return plugin.getMenusYML().getString("deposit-history.title")
                .replace("%menu.page%", String.valueOf(getPage()))
                .replace("%menu.total-pages%", String.valueOf(getPages()))
                .replace("%player%", target.getName());
    }
}