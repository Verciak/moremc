package net.moremc.guilds.inventory.visual;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.entity.guild.heart.type.GuildHeartEggType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;

public class GuildVisualEggSelectEditorInventory extends InventoryHelperImpl {

    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildVisualEggSelectEditorInventory() {
        super("&dWybierz serce", 54);
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 4, 18, 26, 27, 35, 47, 51};

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {

            for (GuildHeartEggType eggType : GuildHeartEggType.values()) {
                ItemHelper headItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                        .setIdentifyItem("eggId", eggType.name())
                        .setName("&aWybierz to jajko.")
                        .setOwnerUrl(eggType.getUrl());

                inventory.addItem(headItem.toItemStack());
            }
            this.onClick(player, event -> {
                event.setCancelled(true);

                ItemStack itemStack = event.getCurrentItem();
                if(itemStack == null)return;

                if(!ItemIdentityHelper.compareIdentity(itemStack, "eggId"))return;

                GuildHeartEggType eggType = GuildHeartEggType.valueOf(ItemIdentityHelper.getItemIdentityTagName(itemStack, "eggId"));
                guild.getHeart().setEggType(eggType);
                guild.synchronize(SynchronizeType.UPDATE);
            });
        });
    }
}
