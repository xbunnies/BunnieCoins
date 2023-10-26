package me.bunnie.bunniecoins.utils.ui.menu.page;

import me.bunnie.bunniecoins.utils.ui.menu.Button;
import me.bunnie.bunniecoins.utils.ui.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class PageMenu extends Menu {

    private int page = 1;

    public PageMenu(int size, Player player) {
        super(size, player);
    }

    public abstract Map<Integer, Button> getAllPagesButtons();

    public final int getPages() {
        int buttonAmount = getAllPagesButtons().size();

        if (buttonAmount == 0) {
            return 1;
        }

        return (int) Math.ceil(buttonAmount / (double) getMaxItemsPerPage());
    }

    public final void modPage(int mod) {
        page += mod;
        getButtons().clear();
        open();
    }

    @Override
    public final Map<Integer, Button> getButtons() {
        int minIndex = (int) ((double) (page - 1) * getMaxItemsPerPage());
        int maxIndex = (int) ((double) (page) * getMaxItemsPerPage());
        HashMap<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new PageButton(-1, this));
        buttons.put(8, new PageButton(1, this));

        for (Map.Entry<Integer, Button> entry : getAllPagesButtons().entrySet()) {
            int ind = entry.getKey();

            if (ind >= minIndex && ind < maxIndex) {
                ind -= (int) ((double) (getMaxItemsPerPage()) * (page - 1) - 9);
                buttons.put(ind, entry.getValue());
            }
        }

        return buttons;
    }

    public int getMaxItemsPerPage() {
        return 27;
    }

    public int getPage() {
        return page;
    }
}