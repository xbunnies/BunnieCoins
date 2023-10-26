package me.bunnie.bunniecoins.utils.ui.menu.page;

import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.ItemBuilder;
import me.bunnie.bunniecoins.utils.ui.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


public class PageButton extends Button {

    private final int mod;
    private final PageMenu menu;

    public PageButton(int mod, PageMenu menu) {
        this.mod = mod;
        this.menu = menu;
    }

    @Override
    public ItemStack getItem(Player player) {
        if (this.hasNext()) {
            return new ItemBuilder(Material.CHERRY_BUTTON)
                    .setName(mod > 0 ? ChatUtils.format("&dNext Page") : ChatUtils.format("&dPrevious Page"))
                    .build();
        } else {
            return new ItemBuilder(Material.CRIMSON_BUTTON)
                    .setName(mod > 0 ? ChatUtils.format("&cLast Page") : ChatUtils.format("&cFirst Page"))
                    .build();
        }
    }

    @Override
    public void onButtonClick(Player player, int slot, ClickType clickType) {
        if (hasNext()) {
            this.menu.modPage(mod);
        }
    }

    private boolean hasNext() {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0 && this.menu.getPages() >= pg;
    }

}