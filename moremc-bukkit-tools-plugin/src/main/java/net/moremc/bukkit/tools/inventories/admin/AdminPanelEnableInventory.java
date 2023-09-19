package net.moremc.bukkit.tools.inventories.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.entity.server.Server;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.ServerService;
import net.moremc.bukkit.api.bulider.ItemBulider;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Optional;

public class AdminPanelEnableInventory extends InventoryHelperImpl
{
    private final API api = API.getInstance();
    private final ServerService service = API.getInstance().getServerService();

    private final String enableName;

    public AdminPanelEnableInventory(String enableName) {
        super("&dZarządzaj: &f" + enableName, 9);
        this.enableName = enableName;
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        Optional<Server> server = service.findByValueOptional(1);

        if(!server.isPresent()) {
            player.closeInventory();
            return;
        }
        inventory.setItem(3, new ItemBulider(Material.SKULL_ITEM, 1, (short) 3).setName("&aWłącz").setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI1ODcxYzcyOTg3MjY2ZTE1ZjFiZTQ5YjFlYzMzNGVmNmI2MThlOTY1M2ZiNzhlOTE4YWJkMzk1NjNkYmI5MyJ9fX0=").toItemStack());
        inventory.setItem(5, new ItemBulider(Material.SKULL_ITEM, 1, (short) 3).setName("&cWyłącz").setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2VmMTE5ZjA4ODUxYTcyYTVmMTBmYmMzMjQ3ZDk1ZTFjMDA2MzYwZDJiNGY0MTJiMjNjZTA1NDA5Mjc1NmIwYyJ9fX0=").toItemStack());

        onClick(player, event -> {
            event.setCancelled(true);

            if(event.getSlot() == 3) {
                player.closeInventory();

                server.get().setEnable(enableName, true);
                service.synchronize(server.get(), SynchronizeType.UPDATE);

                api.getNatsMessengerAPI().sendPacket("moremc_global_channel", new ClientSendMessagePacket("&fAdministrator &d" + player.getName() + " &awłączył &fmożliwość &d " + formated(enableName) + " &f!", SendMessageReceiverType.ALL, SendMessageType.CHAT));
            }
            if(event.getSlot() == 5) {
                player.closeInventory();

                server.get().setEnable(enableName, false);
                service.synchronize(server.get(), SynchronizeType.UPDATE);

                api.getNatsMessengerAPI().sendPacket("moremc_global_channel", new ClientSendMessagePacket("&fAdministrator &d" + player.getName() + " &cwyłączył &fmożliwość &d " + formated(enableName) + " &f!", SendMessageReceiverType.ALL, SendMessageType.CHAT));
            }
        });
    }
    private String formated(String replaced) {
        return replaced
                .replace("kits", "używania kitów ")
                .replace("guilds", "zakładania gildi")
                .replace("case", "otwierania skrzynek")
                .replace("diamond_items", "tworzenia daminetowych rzeczy");
    }
}
