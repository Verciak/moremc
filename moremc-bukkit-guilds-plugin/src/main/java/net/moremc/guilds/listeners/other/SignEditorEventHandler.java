package net.moremc.guilds.listeners.other;

import net.moremc.guilds.GuildsPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import net.moremc.api.API;
import net.moremc.api.data.guild.dues.DuesGuildData;
import net.moremc.api.data.guild.dues.DuesGuildDataArray;
import net.moremc.api.entity.guild.dues.category.GuildDuesCategory;
import net.moremc.api.entity.guild.permission.GuildPermission;
import net.moremc.api.entity.guild.permission.type.GuildPermissionType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.utilities.ItemUtilities;
import net.moremc.communicator.plugin.CommunicatorPlugin;

public class SignEditorEventHandler implements Listener {


    private final DuesGuildData duesGuildData = CommunicatorPlugin.getInstance().getDuesGuildData();
    private final GuildService guildService = API.getInstance().getGuildService();

    @EventHandler
    public void onSignEditor(SignChangeEvent event) {
        Player player = event.getPlayer();
        String[] lines = event.getLines();

        new BukkitRunnable(){

            @Override
            public void run() {
                Location newSign = player.getLocation().add(0.0, 100.0, 0.0);
                Location fixnewSign = player.getLocation().add(0.0, 99.0, 0.0);
                newSign.getBlock().setType(Material.AIR);
                fixnewSign.getBlock().setType(Material.AIR);
            }
        }.runTaskLater(GuildsPlugin.getInstance(), 10L);


        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            if(lines[2].equalsIgnoreCase("Wpisz nazwe")){
                if(lines[0].length() <= 2)return;

                if(guild.findPermissionByName(lines[0]).isPresent()){
                    player.sendMessage(MessageHelper.translateText("&cGrupa o takiej nazwie już istnieje!"));
                    return;
                }
                if(guild.getPermissionList().size() >= 7){
                    player.sendMessage(MessageHelper.translateText("&cMaksymalna ilośc grup w gildi została przekroczona!"));
                    return;
                }
                guild.getPermissionList().add(new GuildPermission(guild.getPermissionList().size() + 1, lines[0], GuildPermissionType.NORMAL));
                player.sendTitle("", MessageHelper.translateText("&aPomyślnie stworzono grupe: &2 " + lines[0]));
                guild.synchronize(SynchronizeType.UPDATE);
            }
        });
        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            if(lines[2].equalsIgnoreCase("Podaj nazwe")){
                if(lines[0].length() <= 2)return;

                if(guild.findPermissionByName(lines[0]).isPresent()){
                    player.sendMessage(MessageHelper.translateText("&cGrupa o takiej nazwie już istnieje!"));
                    return;
                }
                guild.findPermissionByName(lines[3]).ifPresent(guildPermission -> {
                    guildPermission.setName(lines[0]);
                    player.sendTitle("", MessageHelper.translateText("&aPomyślnie nadano nową nazwe&8: &2"+ lines[0]));
                    guild.synchronize(SynchronizeType.UPDATE);
                });
            }
        });
        if(lines[2].equalsIgnoreCase("Emeraldów")) {
            try {
                Integer.parseInt(lines[0]);
            } catch (Exception e) {
                player.sendMessage(MessageHelper.colored(" &cTo nie jest liczba."));
                return;
            }
            this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
                int amount = Integer.parseInt(lines[0]);

                if (amount <= 0) {
                    player.sendTitle(MessageHelper.colored("&5&lGILDIA"),
                            MessageHelper.colored("&cPodaj liczbę większą niż 0."));
                    return;
                }
                int amountInInventory = ItemUtilities.getAmountOf(player, Material.EMERALD, (short) 0);
                if(amountInInventory < amount){
                    player.closeInventory();
                    player.sendTitle(MessageHelper.colored("&5&lGILDIA"), MessageHelper.colored("&fNie posiadasz wystarczająco emeraldów!"));
                    return;
                }
                ItemUtilities.removeItem(player, new ItemStack(Material.EMERALD, amount));
                player.closeInventory();
                player.sendTitle("", MessageHelper.colored("&aPomyślnie wpłacono emeraldy"));
                guild.setEmeraldsCount(guild.getEmeraldsCount() + amount);
                guild.synchronize(SynchronizeType.UPDATE);
            });
        }
        if (lines[1].equalsIgnoreCase("PODAJ ILOŚĆ") && lines[2].equalsIgnoreCase("#1") || lines[2].equalsIgnoreCase("#2") || lines[2].equalsIgnoreCase("#3")) {
            this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
                try {
                    Integer.parseInt(lines[0]);
                } catch (Exception e) {
                    player.sendMessage(MessageHelper.colored("&cTo nie jest liczba."));
                    return;
                }

                int amount = Integer.parseInt(lines[0]);
                if (amount <= 0) {
                    player.sendTitle(MessageHelper.colored("&5&lGILDIA"),
                            MessageHelper.colored("&cPodaj liczbę większą niż 0."));
                    return;
                }
                guild.findGuildDuesById(1).ifPresent(guildDues -> {
                    guild.findGuildDuesById(Integer.parseInt(lines[2].split("#")[1])).ifPresent(duesGuild -> {
                        if (!ItemUtilities.hasItem(player, new ItemStack(Material.valueOf(duesGuild.getMaterialName().toUpperCase()), amount))) {
                            player.sendMessage(MessageHelper.colored("&cNie posiadasz wystarczająco przedmiotów!"));
                            return;
                        }
                        ItemUtilities.removeItem(player, new ItemStack(Material.valueOf(duesGuild.getMaterialName().toUpperCase()), amount));
                        guildDues.setAmountWithdraw(guild, guildDues.getAmountWithdraw() + amount);
                        guildDues.addPlayerPayment(guild, player.getName());

                        if (guildDues.getAmountWithdraw() >= guildDues.getAmountEnd()) {
                            duesGuild.setCategory(guild, GuildDuesCategory.END);
                            duesGuild.setActive(guild, false);
                            guild.sendMessage("&fSkładka &d" + guildDues.getId() +  " &fgildyjna została zakończona i czeka na odbiór!");
                        }
                    });
                });
            });
            return;
        }
        if(lines[1].equalsIgnoreCase("WPISZ ILOŚĆ") && this.duesGuildData.findDuesGuildByName(lines[2]) != null){
            DuesGuildDataArray duesGuildDataArray = this.duesGuildData.findDuesGuildByName(lines[2]);
            if(duesGuildDataArray == null)return;
            try {
                Integer.parseInt(lines[0]);
            } catch (Exception e) {
                player.sendMessage(MessageHelper.colored("&cTo nie jest liczba."));
                return;
            }

            int amount = Integer.parseInt(lines[0]);
            if (amount <= 0) {
                player.sendTitle(MessageHelper.colored("&5&lGILDIA"),
                        MessageHelper.colored("&cPodaj liczbę większą niż 0."));
                return;
            }

            this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
                guild.findGuildDuesById(Integer.parseInt(lines[3])).ifPresent(duesGuild -> {
                    duesGuild.setActive(guild, true);
                    duesGuild.setAmountEnd(guild, amount);
                    duesGuild.setOwner(guild, player.getName());
                    duesGuild.setMaterialName(guild, duesGuildDataArray.getMaterialName());
                    duesGuild.setCategory(guild, GuildDuesCategory.ACTIVE);
                });
                guild.sendMessage("&fGracz&8: &d" + player.getName() + " &furuchomił składkę &d/g skladka");
            });
        }
    }

}
