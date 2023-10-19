package me.bunnie.bunniecoins.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {

    public static String format(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> format(List<String> messages) {
        ArrayList<String> toReturn = new ArrayList<>();
        messages.forEach(message -> toReturn.add(format(message)));
        return toReturn;
    }

    public static String removeColorCodes(String input) {
        return input.replaceAll("(?i)&[0-9A-FK-OR]", "")
                .replaceAll("#[0-9A-Fa-f]{6}", "");
    }

    public static String fixCapitalisation(String message) {
        String[] arr = message.split("_");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }


    public static TextComponent getHoverableText(String message, String toHover) {
        TextComponent playerName = new TextComponent(format(message));
        BaseComponent[] hoverText = new BaseComponent[]{new TextComponent(format(toHover))};
        playerName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        return playerName;
    }

    public static List<BaseComponent> getHoverableListText(String message, String toHover) {
        TextComponent text = new TextComponent(format(message));
        TextComponent hoverText = new TextComponent(format(toHover));
        hoverText.setColor(ChatColor.GRAY);
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {hoverText}));
        return Arrays.asList(text);
    }
}
