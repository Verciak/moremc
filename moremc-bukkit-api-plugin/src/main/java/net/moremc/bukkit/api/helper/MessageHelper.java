package net.moremc.bukkit.api.helper;

import net.moremc.api.nats.packet.client.type.SendMessageType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageHelper {

    private final Player player;
    private final List<String> messageList;
    private String message;

    public MessageHelper(Player player, String message){
        this.player = player;
        this.messageList = new ArrayList<>();
        this.message = message;
    }
    public MessageHelper addMessage(String message){
        if(!this.messageList.contains(message)){
            this.messageList.add(message);
        }
        return this;
    }
    public MessageHelper setMessage(String message) {
        this.message = message;
        return this;
    }
    public MessageHelper send(SendMessageType type){
        switch (type) {
            case CHAT: {
                player.sendMessage(translateText(this.message));
                break;
            }
            case ACTIONBAR:{
                PlayerHelper.helper.sendTranslatedMessageActionBar(player, translateText(this.message));
                break;
            }
            case TITLE:{
                player.sendTitle("", translateText(this.message));
                break;
            }
        }
        return this;
    }
    public static String progress(int green, int red) {
        String s = "";
        s += "&a";
        s += stringMultiply("■", green);
        s += "&c";
        s += stringMultiply("■", red);
        return s;
    }
    public static String stringMultiply(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }
    public void build(){
        for (String message : this.messageList) {
            player.sendMessage(translateText(message));
        }
    }
    public static String translateText(String text){
        return   ChatColor.translateAlternateColorCodes('&', text)
                .replace(">>", "»")
                .replace("<<", "«")
                .replace("{STATUS}", "■");
    }
    public static List<String> translateText(List<String> textList){
        return textList.stream().map(MessageHelper::translateText).collect(Collectors.toList());
    }

    public static String colored(String message){
        return translateText(message);
    }
    public static List<String> colored(List<String> messages) {
        return messages.stream()
                .map(MessageHelper::colored)
                .collect(Collectors.toList());
    }
}
