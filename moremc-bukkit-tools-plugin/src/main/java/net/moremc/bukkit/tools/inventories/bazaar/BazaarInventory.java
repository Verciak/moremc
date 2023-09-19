package net.moremc.bukkit.tools.inventories.bazaar;

import net.moremc.api.API;
import net.moremc.api.entity.bazaar.Bazaar;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.nats.packet.bazaar.request.BazaarPlayerBuyRequestPacket;
import net.moremc.api.service.entity.BazaarService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.bulider.ItemBulider;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.api.serializer.ItemSerializer;
import net.moremc.bukkit.api.utilities.SignEditorUtilities;
import net.moremc.bukkit.tools.utilities.ItemUtilities;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BazaarInventory extends InventoryHelperImpl {

    private final BazaarService bazaarService = API.getInstance().getBazaarService();
    private final UserService userService = API.getInstance().getUserService();
    private final int pageNumber;

    public BazaarInventory(int pageNumber) {
        super("&dBazar", 54);
        this.pageNumber = pageNumber;
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {

        this.onClick(player, event -> {
            event.setCancelled(true);

            switch (event.getSlot()) {
                case 4:
                    SignEditorUtilities.openSignEditorToPlayer(player, "", "^^^^^", "&dWpisz nick", " ");
                    break;
                case 16: {
                    if(player.getItemInHand().getType().equals(Material.AIR) || player.getItemInHand() == null){
                        player.closeInventory();
                        player.sendMessage(MessageHelper.colored("&cMusisz trzymać przedmiot w ręku."));
                        return;
                    }
                    SignEditorUtilities.openSignEditorToPlayer(player, "", "^^^^^", "&dPodaj kwote", " ");
                    break;
                }

                case 47: {
                    BazaarInventory previousPageInventory = new BazaarInventory(pageNumber - 1);
                    if (!previousPageInventory.show(player)) {
                        player.sendMessage(MessageHelper.colored("&cBrak poprzedniej strony w bazarze!"));
                        return;
                    }
                    previousPageInventory.show(player);
                    break;
                }

                case 51: {
                    BazaarInventory previousPageInventory = new BazaarInventory(pageNumber + 1);
                    if (!previousPageInventory.show(player)) {
                        player.sendMessage(MessageHelper.colored("&cBrak nastepnej strony w bazarze!"));
                        return;
                    }
                    previousPageInventory.show(player);
                    break;
                }
            }
            ItemStack itemStack = event.getCurrentItem();

            if (itemStack == null || itemStack.getType().equals(Material.AIR)) return;
            if (!ItemIdentityHelper.compareIdentity(itemStack, "bazaar-id")) return;

            int countInInventoryPlayerGold = ItemUtilities.getAmountOf(player, Material.GOLD_INGOT, (short) 0);
            int id = Integer.parseInt(ItemIdentityHelper.getItemIdentityTagName(itemStack, "bazaar-id"));

            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller", new BazaarPlayerBuyRequestPacket(id, countInInventoryPlayerGold, player.getName()));
        });
    }

    @Override
    public boolean tryInitializeInventoryWithResult(Player player, Inventory inventory) {
        Integer[] magmaGlassSlots = new Integer[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 27, 35, 48, 50};
        Integer[] grayGlassSlots = new Integer[]{0, 8, 45, 53};
        Integer[] purpleGlassSlots = new Integer[]{2, 6, 18, 26, 36, 44, 46, 52};

        Arrays.stream(magmaGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(purpleGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 10).setName(" ").toItemStack()));
        Arrays.stream(grayGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        this.setBackPageItem(inventory, 47);
        this.setNextPageItem(inventory, 51);

        userService.findByValueOptional(player.getName()).ifPresent(user -> {

            inventory.setItem(4, new ItemBulider(Material.SIGN)
                    .setName("&d&lWYSZUKAJ BAZAR INNEGO GRACZA")
                    .setLore(Arrays.asList(
                            "",
                            "         &fKliknij tutaj oraz podaj nick gracza",
                            "",
                            "&fKliknij &dLEWY &fżeby otworzyć &dbazar &fwybranego gracza")
                    ).toItemStack()
            );
            inventory.setItem(10, new ItemHelper(Material.BARRIER).setName("&c&lNIEBAWEM").toItemStack());

            inventory.setItem(12, new ItemBulider(Material.HOPPER)
                    .setName("&d&lWYPLATA")
                    .setLore(Arrays.asList(
                            "",
                            " &fObecnie posiadasz: &d" + user.getDepositCount("bazaar_amount") + " złota.",
                            "")
                    ).toItemStack());

            inventory.setItem(14, new ItemBulider(Material.MAP)
                    .setName("&d&lLIMT OGŁOSZEŃ")
                    .setLore(Arrays.asList(
                            "",
                            "&fNa serwerze obowiazuje limit ogloszeń:",
                            " &8>> &d&lVIP &8( &d5 przedmiotów &8)",
                            " &8>> &5&lSVIP &8( &d10 przedmiotów &8)",
                            "")
                    ).toItemStack());

            inventory.setItem(16, new ItemBulider(Material.ANVIL).visibleFlag().addEnchant(Enchantment.ARROW_DAMAGE, 10)
                    .setName("&d&lWystaw przedmiot ")
                    .setLore(Arrays.asList(
                            "",
                            " &7Musisz posiadać przedmiot w ręce",
                            " &7Twój przedmiot zostanie wystawiony",
                            " &7na czas &55 godziny",
                            "",
                            " &7Aby ustalić cenę sprzedaży &fkliknij",
                            " &7dzięki temu ustawisz cenę oraz twój przedmiot",
                            " &7znajdzie się na liście kupna")
                    ).toItemStack());
        });

        return this.initialize(player, inventory, pageNumber);
    }
    private boolean initialize(Player player, Inventory inventory, int pageNumber) {
        List<Bazaar> bazaarList = new ArrayList<>(this.bazaarService.getMap().values());
        int fromIndex = pageNumber * 21;

        if(fromIndex < 0)return false;
        if (bazaarList.size() <= fromIndex) {
            return !(pageNumber > 0);
        }
        List<Bazaar> paginatedBazaarList = bazaarList.subList(fromIndex, Math.min(fromIndex + 21, bazaarList.size()));
        if (paginatedBazaarList.isEmpty()) {
            return false;
        }

        for (Bazaar bazaar : paginatedBazaarList) {
            int countInInventoryPlayerGold = ItemUtilities.getAmountOf(player, Material.GOLD_INGOT, (short) 0);
            ItemStack item = ItemSerializer.encodeItem(bazaar.getSerializedItem())[0];

            ItemHelper bazaarItem = new ItemHelper(item)
                    .setName("&dOferta: &f" + bazaar.getID())
                    .setIdentifyItem("bazaar-id", String.valueOf(bazaar.getId()))
                    .setLore(Arrays.asList(
                            "",
                            " &fSprzedający: &d" + bazaar.getNickName(),
                            " &fKwota: &d" + bazaar.getSellCount() + " złota.",
                            " &fWygasa: &d" + DataHelper.getTimeToString(bazaar.getActiveTime()),
                            "",
                            "" + (countInInventoryPlayerGold >= bazaar.getSellCount() ? "&dLewy &8- &fAby zakupic ten przedmiot" : "&cNie stać cię na ten przedmiot.")));

            inventory.addItem(bazaarItem.toItemStack());
        }

        return true;
    }
}