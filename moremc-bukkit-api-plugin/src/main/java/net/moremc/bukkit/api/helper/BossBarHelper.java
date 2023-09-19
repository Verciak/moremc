package net.moremc.bukkit.api.helper;

import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.moremc.bukkit.api.helper.type.SendType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class BossBarHelper {

    private static final Class<?> CraftWorldClass = ReflectionHelper.getOcbClass("CraftWorld");
    private static final Class<?> WorldClass = ReflectionHelper.getNmsClass("World");
    private static final Class<?> EntityWitherClass = ReflectionHelper.getNmsClass("EntityWither");
    private static final Class<?> EntityLivingClass = ReflectionHelper.getNmsClass("EntityLiving");
    private static final Class<?> EntityClass = ReflectionHelper.getNmsClass("Entity");
    private static final Class<?> DataWatcherClass = ReflectionHelper.getNmsClass("DataWatcher");

    private static final Class<?> PacketPlayOutSpawnEntityLivingClass = ReflectionHelper.getNmsClass("PacketPlayOutSpawnEntityLiving");
    private static final Class<?> PacketPlayOutEntityDestroyClass = ReflectionHelper.getNmsClass("PacketPlayOutEntityDestroy");
    private static final Class<?> PacketPlayOutEntityMetadataClass = ReflectionHelper.getNmsClass("PacketPlayOutEntityMetadata");
    private static final Class<?> PacketPlayOutEntityTeleportClass = ReflectionHelper.getNmsClass("PacketPlayOutEntityTeleport");

    private static final Constructor<?> PacketPlayOutSpawnEntityLivingConstructor = ReflectionHelper.getConstructor(PacketPlayOutSpawnEntityLivingClass, EntityLivingClass);
    private static final Constructor<?> PacketPlayOutEntityDestroyConstructor = PacketPlayOutEntityDestroyClass.getDeclaredConstructors()[1];
    private static final Constructor<?> PacketPlayOutEntityTeleportConstructor = ReflectionHelper.getConstructor(PacketPlayOutEntityTeleportClass, EntityClass);
    private static final Constructor<?> PacketPlayOutEntityMetadataConstructor = ReflectionHelper.getConstructor(PacketPlayOutEntityMetadataClass, int.class, DataWatcherClass, boolean.class);

    private static final Constructor<?> DataWatcherConstructor = DataWatcherClass.getDeclaredConstructors()[0];
    private static final Constructor<?> EntityWitherConstructor = EntityWitherClass.getDeclaredConstructors()[0];

    private static final Method getHandle = ReflectionHelper.getMethod(CraftWorldClass, "getHandle");

    private static final Method setLocation = ReflectionHelper.getMethod(EntityClass, "setLocation", double.class, double.class, double.class, float.class, float.class);
    private static final Method setInvisible = ReflectionHelper.getMethod(EntityClass, "setInvisible", boolean.class);
    private static final Method setCustomName = ReflectionHelper.getMethod(EntityClass, "setCustomName", String.class);
    private static final Method setCustomNameVisible = ReflectionHelper.getMethod(EntityClass, "setCustomNameVisible", boolean.class);
    private static final Method setHealth = ReflectionHelper.getMethod(EntityLivingClass, "setHealth", float.class);

    private static final Method getDataWatcher = ReflectionHelper.getMethod(EntityClass, "getDataWatcher");
    private static final Method isAlive = ReflectionHelper.getMethod(EntityClass, "isAlive");
    private static final Method getId = ReflectionHelper.getMethod(EntityClass, "getId");

    private final Player player;
    private final PacketHelper packetHelper;

    private String displayName;
    private float health;

    private Object entity;

    public BossBarHelper(Player player, PacketHelper packetHelper) {
        this.player = player;
        this.packetHelper = packetHelper;
    }

    public BossBarHelper(Player player) {
        this(player, new PacketHelper(player));
    }

    public BossBarHelper apply(Consumer<BossBarHelper> consumer) {
        consumer.accept(this);
        return this;
    }

    public void setDisplayName(String text) {
        this.displayName = text;
    }

    public boolean isAlive() {
        return entity != null;
    }


    public void send(SendType sendType) {
        try {
            if (sendType.equals(SendType.UPDATE)) {

                Location location = getWitherLocation(player.getEyeLocation());

                if (isAlive()) {
                    entity.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class)
                            .invoke(entity, location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
                    packetHelper.addPacket(ReflectionHelper.newInstance(PacketPlayOutEntityTeleportConstructor, entity));

                    Object dataWatcher = ReflectionHelper.invoke(getDataWatcher, entity);
                    ReflectionHelper.invoke(setCustomName, entity, displayName);
                    this.entity.getClass().getMethod("setAirTicks", int.class).invoke(entity, 0);
                    ReflectionHelper.invoke(setHealth, entity, health);
                    packetHelper.addPacket(ReflectionHelper.newInstance(PacketPlayOutEntityMetadataConstructor, ReflectionHelper.invoke(getId, entity), dataWatcher, true));
                    packetHelper.send();
                    return;
                }

                Object world = ReflectionHelper.as(ReflectionHelper.invoke(getHandle, player.getWorld()), WorldClass);
                this.entity = ReflectionHelper.newInstance(EntityWitherConstructor, world);

                this.entity.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class)
                        .invoke(entity, location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
                this.entity.getClass().getMethod("setAirTicks", int.class).invoke(entity, 0);
                ReflectionHelper.invoke(setInvisible, entity, true);

                if (displayName != null && !displayName.isEmpty()) {
                    ReflectionHelper.invoke(setCustomName, entity, displayName);
                    ReflectionHelper.invoke(setCustomNameVisible, entity, true);
                }

                packetHelper.addPacket(ReflectionHelper.newInstance(PacketPlayOutSpawnEntityLivingConstructor, entity));
                packetHelper.send();
            } else if (sendType.equals(SendType.REMOVE)) {
                if (!isAlive()) return;
                EntityWither entityWither = (EntityWither) entity;
                PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(entityWither.getId());
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
                this.entity = null;
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    public Location getWitherLocation(Location location) {
        return location.add(location.getDirection().normalize().multiply(20).add(new Vector(0, -20, 0)));
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public String getDisplayName() {
        return displayName;
    }

}
