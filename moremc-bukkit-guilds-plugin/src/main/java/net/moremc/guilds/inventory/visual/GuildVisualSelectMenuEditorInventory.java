package net.moremc.guilds.inventory.visual;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;

public class GuildVisualSelectMenuEditorInventory extends InventoryHelperImpl {


    private final GuildVisualCenterSelectEditorInventory centerSelectEditorInventory = new GuildVisualCenterSelectEditorInventory();
    private final GuildVisualEggSelectEditorInventory eggSelectEditorInventory = new GuildVisualEggSelectEditorInventory();
    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildVisualSelectMenuEditorInventory() {
        super("&dWybierz edycje", 54);
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

            ItemHelper changeTypeGuildItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                    .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA4MDk3MmI3YzMyZGE2NTBjYzg2ZTlhYWQ2NjVlZDhkMGQ0OGEzYmRiMGJhMjM0ZDRmY2VhYzAyNGM3OTUyZSJ9fX0=")
                    .setName("&dEdytacja serca");

            inventory.setItem(4, changeTypeGuildItem.toItemStack());

            inventory.setItem(49, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cWyjdż").toItemStack());

            ItemHelper changeEggItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                    .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2U5OGI1NTY3MWQ0ZWYyZjY0MjU5ZWZlNmFhNjcyOWZlNjVjNzdkYTE5NmMyZDQ1Njg2NjM3NmVjY2NhNjYwYiJ9fX0=")
                    .setName("&dZmiana wygłądu jajka");

            inventory.setItem(21, changeEggItem.toItemStack());

            ItemHelper changeGuildTypeItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                    .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA4MDk3MmI3YzMyZGE2NTBjYzg2ZTlhYWQ2NjVlZDhkMGQ0OGEzYmRiMGJhMjM0ZDRmY2VhYzAyNGM3OTUyZSJ9fX0=")
                    .setName("&dZmiana centrum gildyjnego");

            inventory.setItem(23, changeGuildTypeItem.toItemStack());

            this.onClick(player, event -> {
                event.setCancelled(true);

                switch (event.getSlot()){
                    case 23:{
                        this.centerSelectEditorInventory.show(player);
                        break;
                    }
                    case 21:{
                        this.eggSelectEditorInventory.show(player);
                        break;
                    }
                }

            });
        });
    }
}
