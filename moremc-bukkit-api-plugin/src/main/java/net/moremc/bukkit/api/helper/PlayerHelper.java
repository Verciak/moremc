package net.moremc.bukkit.api.helper;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class PlayerHelper {

    private static final Class<?> PacketPlayOutChatClass = ReflectionHelper.getNmsClass("PacketPlayOutChat");
    private static final Class<?> IChatBaseComponentClass = ReflectionHelper.getNmsClass("IChatBaseComponent");
    private static final Class<?> PacketPlayOutTitleClass = ReflectionHelper.getNmsClass("PacketPlayOutTitle");

    private static final Class<?> ChatSerializerClass = IChatBaseComponentClass.getDeclaredClasses()[0];
    private static final Class<?> EnumTitleActionClass = PacketPlayOutTitleClass.getDeclaredClasses()[0];

    private static final Method serializeMethod = ReflectionHelper.getMethod(ChatSerializerClass, "a", String.class);

    private static final Constructor<?> PacketPlayOutChatConstructor = ReflectionHelper.getConstructor(PacketPlayOutChatClass, IChatBaseComponentClass, byte.class);
    private static final Constructor<?> PacketPlayOutTitleConstructorConstructor = ReflectionHelper
            .getConstructor(PacketPlayOutTitleClass, EnumTitleActionClass, IChatBaseComponentClass, int.class, int.class, int.class);

    private static final Object TITLE = ReflectionHelper.getFieldValue(PacketPlayOutTitleClass, "TITLE");
    private static final Object SUBTITLE = ReflectionHelper.getFieldValue(PacketPlayOutTitleClass, "SUBTITLE");

    public static Helper helper = new Helper();

    private final Player player;
    private final PacketHelper packetHelper;
    private List<String> messages = new ArrayList<>();

    public PlayerHelper(Player player){
        this.player = player;
        this.packetHelper = new PacketHelper(player);
    }

    public Player getPlayer() {
        return player;
    }

    public int getPing(){
        return (int) ReflectionHelper.getFieldValue(getPacketHelper().getEntityPlayer(), "ping");
    }

    public PacketHelper getPacketHelper() {
        return packetHelper;
    }

    public List<String> getMessages() {
        return messages;
    }

    public PlayerHelper addMessage(String message){
        if(message.isEmpty()) return this;
        messages.add(message);
        return this;
    }

    public PlayerHelper addMessage(String... message){
        messages.addAll(Arrays.asList(message));
        return this;
    }

    public PlayerHelper setMessages(List<String> messages){
        this.messages = messages;
        return this;
    }

    public void sendActionbar(String text){


        Object packetChat = ReflectionHelper.newInstance(PacketPlayOutChatConstructor,

                ReflectionHelper.invoke(serializeMethod,null,"{\"text\":\"" + helper.translateAlternateColorCodes(text) + "\"}"),
                (byte)2);

        getPacketHelper()
                .addPacket(packetChat)
                .send();
    }

    public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut){

        getPacketHelper().addPackets(

                ReflectionHelper.newInstance(PacketPlayOutTitleConstructorConstructor, TITLE,
                        ReflectionHelper.invoke(null, "{\"text\":\"" + helper.translateAlternateColorCodes(title) + "\"}"),
                        fadeIn, stay, fadeOut),

                ReflectionHelper.newInstance(PacketPlayOutTitleConstructorConstructor, SUBTITLE,
                        ReflectionHelper.invoke(null, "{\"text\":\"" + helper.translateAlternateColorCodes(subTitle) + "\"}"),
                        fadeIn, stay, fadeOut)
        ).send();

    }

    public void giveItem(Consumer<Collection<ItemStack>> consumer, ItemStack... itemStacks){
        consumer.accept(player.getInventory().addItem(itemStacks).values());
    }

    public int getAmountOfItem(ItemStack itemStack){
        int amount = 0;
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack invStack = player.getInventory().getItem(i);
            if(invStack == null || invStack.getType() == Material.AIR || !invStack.isSimilar(itemStack)) continue;
            amount += invStack.getAmount();
        }
        return amount;
    }

    public int getAmountOfItem(Material material, short data){
        return getAmountOfItem(new ItemStack(material, 1, data));
    }

    public void send(){
        helper.sendTranslatedMessage(player, messages);
        messages.clear();
    }
}