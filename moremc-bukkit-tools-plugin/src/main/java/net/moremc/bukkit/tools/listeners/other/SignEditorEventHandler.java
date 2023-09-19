package net.moremc.bukkit.tools.listeners.other;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.bazaar.Bazaar;
import net.moremc.api.entity.user.User;
import net.moremc.api.nats.packet.bazaar.BazaarSynchronizePacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.BazaarService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.serializer.ItemSerializer;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.bazaar.BazaarPlayerInventory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Optional;

public class SignEditorEventHandler implements Listener
{
    private final UserService userService = API.getInstance().getUserService();
    private final BazaarService bazaarService = API.getInstance().getBazaarService();

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
        }.runTaskLater(ToolsPlugin.getInstance(), 10L);

        if(lines[2].equalsIgnoreCase("Wpisz nick")) {
            Optional<User> user = userService.findByValueOptional(lines[0]);
            if(!user.isPresent()) {
                player.closeInventory();
                player.sendMessage(MessageHelper.colored(MessageHelper.colored("&cGracz &7" + lines[0] + " &cnie został znaleziony w bazie danych!")));
                return;
            }
            Optional<Bazaar> bazaar = bazaarService.findByNick(lines[0]);
            if(!bazaar.isPresent()) {
                player.closeInventory();
                player.sendMessage(MessageHelper.colored(MessageHelper.colored("&cGracz &7" + lines[0] + " &cnie posiada żadnych ogłoszeń")));
                return;
            }
            new BazaarPlayerInventory(user.get().getNickName()).show(player);
            return;
        }
        if(lines[2].equalsIgnoreCase("Podaj kwote")) {
            try {
                Integer.parseInt(lines[0]);
            } catch (Exception e) {
                player.sendMessage(MessageHelper.colored("&cTo nie jest liczba."));
                return;
            }
            int amount = Integer.parseInt(lines[0]);
            if (amount <= 0) {
                player.sendTitle(MessageHelper.colored("&5&lBazar"),
                        MessageHelper.colored("&cPodaj liczbę większą niż 0."));
                return;
            }

            if(player.getItemInHand().getType().equals(Material.AIR) || player.getItemInHand() == null){
                player.closeInventory();
                player.sendTitle(MessageHelper.colored("&5&lBazar"), MessageHelper.colored("&cMusisz trzymać przedmiot w ręku."));
                return;
            }
            int idLatest = API.getInstance().getBazaarService().getMap().values().size() - 1;
            int countId = 1;
            if(idLatest >= 0) {
                Bazaar bazaarLatest = new ArrayList<>(API.getInstance().getBazaarService().getMap().values()).get(idLatest);
                if (bazaarLatest != null) {
                    countId = bazaarLatest.getId() + 1;
                }
            }
            Bazaar bazaar = new Bazaar(countId, player.getName(), ItemSerializer.decodeItems(new ItemStack[]{player.getItemInHand()}), amount);

            player.getInventory().removeItem(player.getItemInHand());
            player.closeInventory();
            player.sendTitle(MessageHelper.colored("&d&lBazar"), MessageHelper.colored("&aPomyślnie wystawiono przedmiot na sprzedaż."));

            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller", new BazaarSynchronizePacket(countId, new Gson().toJson(bazaar), SynchronizeType.CREATE));
        }
    }

}
