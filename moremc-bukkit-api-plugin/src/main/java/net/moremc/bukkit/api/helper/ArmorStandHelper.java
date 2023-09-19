package net.moremc.bukkit.api.helper;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.moremc.bukkit.api.helper.type.SendType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.moremc.bukkit.api.utilities.ReflectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ArmorStandHelper {


    private static final Class<?> CraftWorldClass = ReflectionHelper.getOcbClass("CraftWorld");
    private static final Class<?> WorldClass = ReflectionHelper.getNmsClass("World");
    private static final Class<?> EntityWitherClass = ReflectionHelper.getNmsClass("EntityArmorStand");
    private static final Class<?> EntityLivingClass = ReflectionHelper.getNmsClass("EntityLiving");
    private static final Class<?> EntityClass = ReflectionHelper.getNmsClass("Entity");
    private static final Class<?> DataWatcherClass = ReflectionHelper.getNmsClass("DataWatcher");

    private static final Class<?> PacketPlayOutSpawnEntityLivingClass = ReflectionHelper.getNmsClass("PacketPlayOutSpawnEntityLiving");
    private static final Class<?> PacketPlayOutEntityDestroyClass = ReflectionHelper.getNmsClass("PacketPlayOutEntityDestroy");
    private static final Class<?> PacketPlayOutEntityMetadataClass = ReflectionHelper.getNmsClass("PacketPlayOutEntityMetadata");
    private static final Class<?> PacketPlayOutEntityTeleportClass = ReflectionHelper.getNmsClass("PacketPlayOutEntityTeleport");

    private static final Constructor<?> PacketPlayOutSpawnEntityLivingConstructor = ReflectionHelper.getConstructor(PacketPlayOutSpawnEntityLivingClass, EntityLivingClass);
    private static final Constructor<?> PacketPlayOutEntityDestroyConstructor = ReflectionHelper.getConstructor(PacketPlayOutEntityDestroyClass, EntityLivingClass);
    private static final Constructor<?> PacketPlayOutEntityTeleportConstructor = ReflectionHelper.getConstructor(PacketPlayOutEntityTeleportClass, EntityClass);
    private static final Constructor<?> PacketPlayOutEntityMetadataConstructor = ReflectionHelper.getConstructor(PacketPlayOutEntityMetadataClass, int.class, DataWatcherClass, boolean.class);

    private static final Constructor<?> DataWatcherConstructor = DataWatcherClass.getDeclaredConstructors()[0];
    private static final Constructor<?> EntityWitherConstructor =  ReflectionHelper.getConstructor(EntityWitherClass, ReflectionHelper.getNmsClass("World"));

    private static final Method getHandle = ReflectionHelper.getMethod(CraftWorldClass, "getHandle");

    private static final Method setLocation = ReflectionHelper.getMethod(EntityClass, "setLocation", double.class, double.class, double.class, float.class, float.class);
    private static final Method setInvisible = ReflectionHelper.getMethod(EntityClass, "setInvisible", boolean.class);
    private static final Method setCustomName = ReflectionHelper.getMethod(EntityClass, "setCustomName", String.class);
    private static final Method setCustomNameVisible = ReflectionHelper.getMethod(EntityClass, "setCustomNameVisible", boolean.class);
    private static final Method setHealth = ReflectionHelper.getMethod(EntityLivingClass, "setHealth", float.class);

    private static final Method getDataWatcher = ReflectionHelper.getMethod(EntityClass, "getDataWatcher");
    private static final Method isAlive = ReflectionHelper.getMethod(EntityClass, "isAlive");
    private static final Method getCustomName = ReflectionHelper.getMethod(EntityClass, "getCustomName");
    private static final Method getId = ReflectionHelper.getMethod(EntityClass, "getId");

    private final Player player;
    private final PacketHelper packetHelper;

    private List<String> displayName;
    private float health;

    private List<Object> entity = new ArrayList<>();
    private Location location;
    private double distance = 0.15D;
    int count;


    public List<Object> getEntity() {
        return entity;
    }

    public ArmorStandHelper(Player player, PacketHelper packetHelper) {
        this.player = player;
        this.packetHelper = packetHelper;
    }

    public ArmorStandHelper(Player player) {
        this(player, new PacketHelper(player));
    }

    public ArmorStandHelper apply(Consumer<ArmorStandHelper> consumer) {
        consumer.accept(this);
        return this;
    }

    public void setDisplayName(List<String> text) {
        this.displayName = text;
    }

    public Object isAlive() {
        return this.entity.stream()
                .filter(entity -> entity != null && (boolean) ReflectionHelper.invoke(isAlive, entity))
                .findFirst()
                .orElse(null);
    }
    public String getCustomName(Object entity){
        return (String) ReflectionHelper.invoke(getCustomName, entity);
    }

    public Location getLocation() {
        return location;
    }

    public void setEntity(List<Object> entity) {
        this.entity = entity;
    }

    public void send(SendType sendType, Location locationSet, String head)  {
        try {
            if (sendType.equals(SendType.UPDATE)) {
                Location location = this.location.subtract(-0.50D, 0, -0.50D);
                if (isAlive() != null) {


                    for (Object entity : this.entity) {
                        entity.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class)
                                .invoke(entity, location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
                        packetHelper.addPacket(ReflectionHelper.newInstance(PacketPlayOutEntityTeleportConstructor, entity));

                        Object dataWatcher = ReflectionHelper.invoke(getDataWatcher, entity);
                        ReflectionHelper.invoke(setCustomName, entity, MessageHelper.colored(getCustomName(entity)));
                        if (health != 0.0f) ReflectionHelper.invoke(setHealth, entity, health);

                        Class<?> packetClass = ReflectUtils.getCraftClass("PacketPlayOutEntityEquipment");
                        Object packet = packetClass.newInstance();

                        ReflectUtils.setValue(ReflectUtils.getField(packet.getClass(), "a"), packet, this.entity.get(0).getClass().getMethod("getId").invoke(this.entity.get(0)));
                        ReflectUtils.setValue(ReflectUtils.getField(packet.getClass(), "b"), packet, 4);
                        if(!head.equalsIgnoreCase("null")) {
                            ReflectUtils.setValue(ReflectUtils.getField(packet.getClass(), "c"), packet,
                                    ReflectUtils.getMethod(ReflectUtils.getBukkitClass("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class).invoke(null,
                                            new ItemHelper(Material.SKULL_ITEM, 1, (short) 3).setOwnerUrl(head).toItemStack()));
                        }

                        packetHelper.addPackets(ReflectionHelper.newInstance(PacketPlayOutEntityMetadataConstructor, ReflectionHelper.invoke(getId, entity), dataWatcher, true), packet);
                        packetHelper.send();
                        this.location.add(0, this.distance, 0);
                        count++;

                        for (int i = 0; i < count; i++) {
                            this.location.add(0, this.distance, 0);
                        }
                        this.count = 0;
                    }
                    for(int i = 0; i < this.displayName.size(); i++) {
                        EntityArmorStand entityArmorStand = (EntityArmorStand) this.entity.get(i);
                        entityArmorStand.setCustomName(MessageHelper.translateText(this.displayName.get(i)));
                    }
                    return;
                }
                for (String text : this.getDisplayName()) {
                    Object world = ReflectionHelper.as(ReflectionHelper.invoke(getHandle, player.getWorld()), WorldClass);
                    Object entity = ReflectionHelper.newInstance(EntityWitherConstructor, world);

                    Location firstLocation = new Location(Bukkit.getWorld("world"), locationSet.getX(), locationSet.getY(), locationSet.getZ());
                    this.setLocation(firstLocation);


                    ReflectionHelper.invoke(setLocation, entity, firstLocation.getX(), firstLocation.getY(), firstLocation.getZ(), 0, 0);
                    ReflectionHelper.invoke(setInvisible, entity, true);


                    if (text != null && !text.isEmpty()) {
                        ReflectionHelper.invoke(setCustomName, entity, text);
                        ReflectionHelper.invoke(setCustomNameVisible, entity, true);
                    }
                    this.entity.add(entity);
                    this.location.subtract(-0.50D, this.distance, -0.50D);
                    packetHelper.addPackets(ReflectionHelper.newInstance(PacketPlayOutSpawnEntityLivingConstructor, entity));
                    packetHelper.send();
                    count++;

                    for (int i = 0; i < count; i++) {
                        this.location.add(0, this.distance, 0);
                    }
                    this.count = 0;
                }
            } else if (sendType.equals(SendType.REMOVE)) {
                if (isAlive() == null) return;
                for (Object entity : this.entity) {
                    EntityArmorStand entityArmorStand = (EntityArmorStand) entity;
                    PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(entityArmorStand.getId());
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public List<String> getDisplayName() {
        return displayName;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
