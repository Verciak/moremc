package net.moremc.bukkit.api.serializer;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemSerializer {

    public static String decodeItems(ItemStack[] itemStacks){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(itemStacks);
            String encoded = Base64Coder.encodeLines(outputStream.toByteArray());
            outputStream.close();
            dataOutput.close();
            return encoded;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save itemstack array", e);
        }
    }

    public static ItemStack[] encodeItem(String encode) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(encode));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] read = (ItemStack[]) dataInput.readObject();
            inputStream.close();
            dataInput.close();
            return read;
        } catch (ClassNotFoundException | IOException e) {
        }
        return new ItemStack[]{};
    }
}
