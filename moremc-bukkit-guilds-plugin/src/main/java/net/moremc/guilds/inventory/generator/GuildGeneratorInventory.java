package net.moremc.guilds.inventory.generator;

import net.moremc.guilds.GuildsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.entity.guild.generator.GuildGenerator;
import net.moremc.api.serializer.LocationSerializer;
import net.moremc.api.service.entity.GuildPlayerSelectAreaGeneratorService;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.generator.Generator;
import net.moremc.bukkit.api.generator.RegionGenerator;
import net.moremc.bukkit.api.generator.player.GeneratorPlayer;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public abstract class GuildGeneratorInventory extends InventoryHelperImpl {

    private final GuildService guildService = API.getInstance().getGuildService();
    private final GuildPlayerSelectAreaGeneratorService playerSelectAreaGeneratorService = API.getInstance().getSelectAreaGeneratorService();
    private final GuildGenerator guildGenerator;

    public GuildGeneratorInventory(GuildGenerator guildGenerator, String inventoryName) {
        super(inventoryName, 54);
        this.guildGenerator = guildGenerator;
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 10).setName(" ").toItemStack());
            }
            Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
            Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 4, 18, 26, 27, 35, 47, 51};

            Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
            Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));


            ItemHelper magazineItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                    .setOwnerUrl(this.guildGenerator.getMaterialSkullItemOwnerUrl())
                    .setName(this.getInventoryName());

            inventory.setItem(4, magazineItem.toItemStack());
            inventory.setItem(22, magazineItem.setName("&dMagazyn").setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTgzMmZkYzM4ZTZhOGUwNWVlYzM5NmJhZmIwYTgxZjkzN2E2MTdmMTVhNzUwOWQ4NWRjOTgyZGJmMGFiNWE5ZiJ9fX0=").toItemStack());

            inventory.setItem(31, new ItemHelper(Material.GOLD_AXE)
                    .setName("&dZaznacz teren").toItemStack());

           inventory.setItem(20, new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                    .setName("&aRozpocznij prace")
                            .addLore("")
                            .addLore(" &fPozycja &8(&dRCB&8)&7: " + (guildGenerator.hasSetLocationFirst() ? "&aUstawiono." : "&cDo ustawienia."))
                            .addLore(" &fPozycja &8(&dLCB&8)&7: " + (guildGenerator.hasSetLocationSecond() ? "&aUstawiono." : "&cDo ustawienia."))
                            .addLore("")
                            .addLore(" &fPozycja &8(&dLCB&8)&7: &dX&8:&5" + guildGenerator.getLocationCornerSecond().getX() + "&7, &dY&8:&5" + guildGenerator.getLocationCornerSecond().getY() + "&7, &dZ&8:&5" + guildGenerator.getLocationCornerSecond().getZ())
                            .addLore(" &fPozycja &8(&dRCB&8)&7: &dX&8:&5" + guildGenerator.getLocationCornerFirst().getX() + "&7, &dY&8:&5" + guildGenerator.getLocationCornerFirst().getY() + "&7, &dZ&8:&5" + guildGenerator.getLocationCornerFirst().getZ())
                            .addLore("")
                            .addLore("&5Lewy &8- &fAby rozpocząć prace generatora.")

                    .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI1ODcxYzcyOTg3MjY2ZTE1ZjFiZTQ5YjFlYzMzNGVmNmI2MThlOTY1M2ZiNzhlOTE4YWJkMzk1NjNkYmI5MyJ9fX0=").toItemStack());


            inventory.setItem(24, new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                    .setName("&cZakończ prace").setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2VmMTE5ZjA4ODUxYTcyYTVmMTBmYmMzMjQ3ZDk1ZTFjMDA2MzYwZDJiNGY0MTJiMjNjZTA1NDA5Mjc1NmIwYyJ9fX0=").toItemStack());

            this.onClick(player, event -> {
                event.setCancelled(true);

                switch (event.getSlot()) {

                    case 31:{
                        switch (this.guildGenerator.getGeneratorType()){
                            case OBSIDIAN:{
                                player.closeInventory();
                                player.sendTitle("", MessageHelper.translateText("&aWyznacz teren."));
                                player.getInventory().addItem(GuildsPlugin.getInstance().getCustomItemService().find("axe_guild_obsidian"));
                                break;
                            }
                            case SAND:{
                                player.closeInventory();
                                player.sendTitle("", MessageHelper.translateText("&aWyznacz teren."));
                                player.getInventory().addItem(GuildsPlugin.getInstance().getCustomItemService().find("axe_guild_sand"));
                                break;
                            }
                            case AIR:{
                                player.closeInventory();
                                player.sendTitle("", MessageHelper.translateText("&aWyznacz teren."));
                                player.getInventory().addItem(GuildsPlugin.getInstance().getCustomItemService().find("axe_guild_air"));
                                break;
                            }
                        }
                        break;
                    }

                    case 24:{
                        player.closeInventory();
                        guildGenerator.setActiveTime(0);
                        guildGenerator.setBlocks(0);
                        guildGenerator.setSuccessBlocks(0);
                        guildGenerator.setSkippedBlocks(0);
                        guildGenerator.setLocationCornerSecond(new LocationSerializer("world", 0, 80, 0, 0));
                        guildGenerator.setLocationCornerFirst(new LocationSerializer("world", 0, 80, 0, 0));
                        guild.sendMessage("&5&lGILDIA&8(&dGENERATOR&8) &fGenerator w twojej gildii został pomyślnie wyłączony przez&8: &d" + player.getName());
                        break;
                    }
                    case 22: {
                        if(guild.findGuildGeneratorIsActive().isPresent()){
                            player.closeInventory();
                            player.sendTitle("", MessageHelper.colored("&cNaprawdę chcesz okraść własną gildię podczas generacji?"));
                            return;
                        }
                        new GuildGeneratorMagazineInventory(guild, this.guildGenerator).show(player);
                        break;
                    }
                    case 20: {
                        if(guild.getRegeneration().getTimeLeft() > System.currentTimeMillis()){
                            player.closeInventory();
                            player.sendTitle("", MessageHelper.translateText("&cGildia jest w trakcie regeneracji."));
                            return;
                        }

                        if(guild.findGuildGeneratorIsActive().isPresent()){
                            player.closeInventory();
                            player.sendTitle("", MessageHelper.translateText("&cJakiś generator już jest uruchomiony."));
                            return;
                        }
                        if(!guildGenerator.hasSetLocationFirst() || !guildGenerator.hasSetLocationSecond()){
                            player.closeInventory();
                            player.sendTitle("", MessageHelper.translateText("&cMusisz ustawić lokacje."));
                            return;
                        }

                        if(!guildGenerator.isActive()){
                            player.closeInventory();
                            player.sendTitle("", MessageHelper.translateText("&cGenerator już jest uruchomiony."));
                            return;
                        }
                        guildGenerator.setBlocks(0);
                        guildGenerator.setSkippedBlocks(0);
                        guildGenerator.setSuccessBlocks(0);

                        GeneratorPlayer generatorPlayer = new GeneratorPlayer();
                        generatorPlayer.setFirstCorner(new Location(Bukkit.getWorld("world"), guildGenerator.getLocationCornerFirst().getX(), guildGenerator.getLocationCornerFirst().getY(), guildGenerator.getLocationCornerFirst().getZ()));
                        generatorPlayer.setSecondCorner(new Location(Bukkit.getWorld("world"), guildGenerator.getLocationCornerSecond().getX(), guildGenerator.getLocationCornerSecond().getY(), guildGenerator.getLocationCornerSecond().getZ()));
                        Generator generator = new RegionGenerator(generatorPlayer.farmerRegion(), guildGenerator, Material.valueOf(guildGenerator.getGeneratorType().name()));
                        generator.start();

                        long delta = Math.abs(generator.getRegionGeneratorFiller().getCount() - generatorPlayer.farmerRegion().area() * 100);
                        long time = TimeUnit.SECONDS.toMillis(delta);
                        guildGenerator.setActiveTime(System.currentTimeMillis() + time / 100);



                        player.closeInventory();
                        player.getInventory().remove(new ItemStack(Material.GOLD_AXE));
                        break;
                    }
                }
            });

        });
    }
}
