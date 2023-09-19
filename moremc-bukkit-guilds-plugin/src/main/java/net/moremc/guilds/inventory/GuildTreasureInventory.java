package net.moremc.guilds.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.api.serializer.ItemSerializer;

public class GuildTreasureInventory extends InventoryHelperImpl {


    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildTreasureInventory() {
        super("&dSkarbiec", 54);
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            guild.getTreasure().setOpen(true);

            if(guild.getTreasure().getSerializedItems().equalsIgnoreCase("null")){
                guild.getTreasure().setSerializedItems(ItemSerializer.decodeItems(new ItemStack[54]));
            }
            inventory.setContents(ItemSerializer.encodeItem(guild.getTreasure().getSerializedItems()));


            this.onClose(player, event -> {
                Inventory inventoryClose = event.getInventory();
                guild.getTreasure().setOpen(false);
                guild.getTreasure().setSerializedItems(ItemSerializer.decodeItems(inventoryClose.getContents()));
                guild.synchronize(SynchronizeType.UPDATE);
            });

            player.openInventory(inventory);
        });
    }
}
