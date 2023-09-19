package net.moremc.guilds.command;

import net.moremc.api.entity.guild.GuildArea;
import net.moremc.api.nats.packet.guild.GuildSchematicPastePacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.serializer.LocationSerializer;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;
import net.moremc.guilds.inventory.create.GuildFreeLocationSelectSectorInventory;

public class CreateGuildCommand extends GuildSubCommand {


    private final GuildService guildService = API.getInstance().getGuildService();
    private final UserService userService = API.getInstance().getUserService();
    private final GuildFreeLocationSelectSectorInventory freeLocationSelectSectorInventory = new GuildFreeLocationSelectSectorInventory();

    public CreateGuildCommand() {
        super("zaloz");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        MessageHelper messageHelper = new MessageHelper(player, "");
        if (this.guildService.findGuildByNickName(player.getName()).isPresent()) {
            messageHelper.setMessage(MessageHelper.translateText("&cPosiadasz już gildię")).send(SendMessageType.CHAT);
            return false;
        }

        if (args.length < 2) {
            messageHelper.setMessage(MessageHelper.translateText("&7Poprawne użycie&8: &d{COMMAND}").replace("{COMMAND}", "/g zaloz <nazwa> <pelnaNazwa>")).send(SendMessageType.CHAT);
            return false;
        }

        String name = args[0].toUpperCase();
        String fullName = args[1];

        if (name.length() < 3 || name.length() > 4 || fullName.length() < 8 || fullName.length() > 16) {
            messageHelper.setMessage(MessageHelper.translateText("&cTag gildii może zawierać maksymalnie 3-4 znaków a pełna nazwa 8-16 znaków.")).send(SendMessageType.CHAT);
            return false;
        }
        if (this.guildService.findByValueOptional(name).isPresent()) {
            messageHelper.setMessage(MessageHelper.translateText("&cGildia o takiej nazwie już istnieje w bazie danych.")).send(SendMessageType.CHAT);
            return false;
        }
        LocationSerializer location = new LocationSerializer("world", player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), 32);
        GuildImpl guild = new GuildImpl(name, fullName, player.getName(), location);

        if (API.getInstance().getSectorService().getCurrentSector().get().isSpawn()) {
            messageHelper.setMessage(MessageHelper.translateText("&cGilide można zakładać tylko na sektorach gildii.")).send(SendMessageType.TITLE);
            return false;
        }

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
//            this.freeLocationSelectSectorInventory.show(player);

            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel",new ClientSendMessagePacket("&fGracz &d{PLAYER} &fzałożył gildię &8(&d{NAME}&8, &d{FULL_NAME}&8)"
                    .replace("{PLAYER}", player.getName())
                    .replace("{NAME}", guild.getName())
                    .replace("{FULL_NAME}", guild.getFullName()), SendMessageReceiverType.ALL, SendMessageType.CHAT));

            user.setGuildAreaSelect(new GuildArea(1, player.getLocation().getBlockX(), player.getLocation().getBlockZ()));
            this.guildService.findGuildAreaById(user.getGuildAreaSelect().getId()).ifPresent(guildArea -> {
                this.guildService.getGuildAreaList().remove(guildArea.getId());
            });



            user.setGuildCreated(true);
            user.setViewTerrainGuild(false);
            guild.setLocation(new LocationSerializer("world", user.getGuildAreaSelect().getX(), 38, user.getGuildAreaSelect().getZ(), 32));
            guild.setLocationHome(new LocationSerializer("world", user.getGuildAreaSelect().getX(), 38, user.getGuildAreaSelect().getZ(), 32));
            guild.synchronize(SynchronizeType.UPDATE);
            player.teleport(new Location(player.getWorld(), user.getGuildAreaSelect().getX(), 35, user.getGuildAreaSelect().getZ()));

            API.getInstance().getNatsMessengerAPI().sendPacket(user.getUserSector().getActualSectorName(), new GuildSchematicPastePacket(guild.getName(), user.getGuildAreaSelect().getX(), user.getGuildAreaSelect().getZ()));

        });
        return false;
    }
}
