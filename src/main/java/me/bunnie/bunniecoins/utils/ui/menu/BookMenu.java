package me.bunnie.bunniecoins.utils.ui.menu;

import me.bunnie.bunniecoins.utils.ChatUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class BookMenu {

    private final String title;
    private final String author;
    private final Player player;

    public BookMenu(String title, String author, Player player) {
        this.title = title;
        this.author = author;
        this.player = player;
    }

    public abstract String getLines();

    public void open() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        if (bookMeta == null) return;
        bookMeta.setTitle(title);
        bookMeta.setAuthor(author);
        String line = getLines();

        List<BaseComponent> pageComponents = new ArrayList<>();

        while (line.contains("<link:")) {
            int linkStart = line.indexOf("<link:") + 6;
            int linkEnd = line.indexOf(">");
            int linkTextStart = linkEnd + 1;
            int linkTextEnd = line.indexOf("</link>");

            if (linkStart < linkEnd && linkEnd < linkTextStart && linkTextStart < linkTextEnd) {
                String beforeLink = line.substring(0, linkStart - 6);
                String link = line.substring(linkStart, linkEnd);
                String linkText = line.substring(linkTextStart, linkTextEnd);

                pageComponents.add(new TextComponent(ChatUtils.format(beforeLink)));

                TextComponent clickableText = new TextComponent(ChatUtils.format(linkText));
                clickableText.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
                pageComponents.add(clickableText);

                line = line.substring(linkTextEnd + 7);
            } else {
                player.sendMessage(ChatUtils.format("&cInvalid link format: " + line));
                break;
            }
        }

        pageComponents.add(new TextComponent(ChatUtils.format(line)));

        BaseComponent[] pageArray = pageComponents.toArray(new BaseComponent[0]);
        bookMeta.spigot().addPage(pageArray);
        book.setItemMeta(bookMeta);
        player.openBook(book);
    }
}
