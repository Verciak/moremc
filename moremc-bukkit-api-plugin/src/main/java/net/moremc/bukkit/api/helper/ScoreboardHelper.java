package net.moremc.bukkit.api.helper;

import net.moremc.bukkit.api.helper.type.SendType;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScoreboardHelper {

    private static final Class<?> ScoreboardClass = ReflectionHelper.getNmsClass("Scoreboard");
    private static final Class<?> ScoreboardObjectiveClass = ReflectionHelper.getNmsClass("ScoreboardObjective");
    private static final Class<?> PacketPlayOutScoreboardObjectiveClass = ReflectionHelper.getNmsClass("PacketPlayOutScoreboardObjective");
    private static final Class<?> PacketPlayOutScoreboardDisplayObjectiveClass = ReflectionHelper.getNmsClass("PacketPlayOutScoreboardDisplayObjective");
    private static final Class<?> PacketPlayOutScoreboardScoreClass = ReflectionHelper.getNmsClass("PacketPlayOutScoreboardScore");
    private static final Class<?> IScoreboardCriteriaClass = ReflectionHelper.getNmsClass("IScoreboardCriteria");
    private static final Class<?> IScoreboardCriteriaClass_EnumScoreboardHealthDisplayClass = IScoreboardCriteriaClass.getDeclaredClasses()[0];
    private static final Class<?> ScoreboardServerClass_ActionClass = PacketPlayOutScoreboardScoreClass.getDeclaredClasses()[0];
    private static final Class<?> ScoreboardScoreClass = ReflectionHelper.getNmsClass("ScoreboardScore");

    private static final Method setScore = ReflectionHelper.getMethod(ScoreboardScoreClass, "setScore", int.class);

    private static final Method setDisplayName = ReflectionHelper.getMethod(ScoreboardObjectiveClass, "setDisplayName", String.class);

    private static final Method getObjective = ReflectionHelper.getMethod(ScoreboardClass, "getObjective", String.class);
    private static final Method registerObjective = ReflectionHelper.getMethod(ScoreboardClass, "registerObjective",
          String.class,
          IScoreboardCriteriaClass);

    private static final Object DUMMY = ReflectionHelper.getFieldValue(IScoreboardCriteriaClass, "b");


    private static final Enum<?> INTEGER = (Enum<?>) IScoreboardCriteriaClass_EnumScoreboardHealthDisplayClass.getEnumConstants()[0];

    private static final Enum<?> CHANGE = (Enum<?>) ScoreboardServerClass_ActionClass.getEnumConstants()[0];
    private static final Enum<?> REMOVE = (Enum<?>) ScoreboardServerClass_ActionClass.getEnumConstants()[1];

    private static final Constructor<?> PacketPlayOutScoreboardObjectiveConstructor =
          ReflectionHelper.getConstructor(PacketPlayOutScoreboardObjectiveClass, ScoreboardObjectiveClass, int.class);

    private static final Constructor<?> PacketPlayOutScoreboardDisplayObjectiveConstructor =
          ReflectionHelper.getConstructor(PacketPlayOutScoreboardDisplayObjectiveClass, int.class, ScoreboardObjectiveClass);

    private static final Constructor<?> PacketPlayOutScoreboardScoreConstructor =
          ReflectionHelper.getConstructor(PacketPlayOutScoreboardScoreClass, ScoreboardScoreClass);

    private static final Constructor<?> ScoreboardScoreConstructor = ReflectionHelper.getConstructor(ScoreboardScoreClass, ScoreboardClass, ScoreboardObjectiveClass, String.class);

    public static Helper helper = new Helper();

    private final Player player;

    private String title;
    private List<String> lines = new ArrayList<>();

    private final Object scoreboard = ReflectionHelper.newInstance(ScoreboardClass);
    private Object scoreboardObjective;
    private PacketHelper packetHelper;

    public ScoreboardHelper(Player player, PacketHelper packetHelper){
        this.player = player;
        if(packetHelper != null) this.packetHelper = packetHelper;
        else this.packetHelper = new PacketHelper(player);
    }

    public ScoreboardHelper apply(Consumer<ScoreboardHelper> consumer){
        consumer.accept(this);
        prepareObjective();
        return this;
    }

    private void prepareObjective() {

        Object obj = ReflectionHelper.invoke(getObjective, scoreboard, "moremc");;
        if (obj == null) this.scoreboardObjective = ReflectionHelper.invoke(registerObjective, scoreboard, "moremc", DUMMY);
        else this.scoreboardObjective = obj;
        ReflectionHelper.invoke(setDisplayName, scoreboardObjective, MessageHelper.translateText(title));
    }

    public void send(SendType type){

        Object remove = ReflectionHelper.newInstance(PacketPlayOutScoreboardObjectiveConstructor, scoreboardObjective, 1);
        Object create = ReflectionHelper.newInstance(PacketPlayOutScoreboardObjectiveConstructor, scoreboardObjective, 0);
        Object display = ReflectionHelper.newInstance(PacketPlayOutScoreboardDisplayObjectiveConstructor, 1, scoreboardObjective);

        if(type.equals(SendType.UPDATE)) {
            packetHelper.addPackets(remove, create, display);

            for (int i = 0; i < lines.size(); i++) {

                Object scoreboardScore =  ReflectionHelper.newInstance(ScoreboardScoreConstructor, scoreboard, scoreboardObjective, helper.cutLength(MessageHelper.translateText(lines.get(i)), 40));
                ReflectionHelper.invoke(setScore, scoreboardScore, -i);

                packetHelper.addPacket(ReflectionHelper.newInstance(PacketPlayOutScoreboardScoreConstructor,
                        scoreboardScore));
            }
        } else {
            packetHelper.addPacket(remove);
        }
        packetHelper.send();
    }

    public Player getPlayer() {
        return player;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}