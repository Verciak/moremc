package net.moremc.bukkit.tools.utilities;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SkinUtilities {

    public void changeSkin(Player playerTarget) {
        GameProfile gameProfile = ((CraftPlayer) playerTarget).getHandle().getProfile();

        PlayerConnection playerConnection = ((CraftPlayer) playerTarget).getHandle().playerConnection;
        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) playerTarget).getHandle()));

        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", getSkin(0));

        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) playerTarget).getHandle()));
    }
    public void removeSkin(Player playerTarget) {

        GameProfile gameProfile = ((CraftPlayer) playerTarget).getHandle().getProfile();
        gameProfile.getProperties().removeAll("textures");


        PlayerConnection playerConnection = ((CraftPlayer) playerTarget).getHandle().playerConnection;
        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) playerTarget).getHandle()));
        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) playerTarget).getHandle()));
    }

    private static Property getSkin(int number) {
        switch (number) {
            case 0:
                return new Property("textures", "eyJ0aW1lc3RhbXAiOjE1MTQ1NTk4OTMyNzQsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE3ZDJkZDg5NTNkYzE5MTJiMWQ3YzhkOWU5Zjc3MjEzZjc5NDQzOTg1YmVlMjM1YmYxOWFlMGIyZWZkIn19fQ==",
                        "rzUFpcy03vQJyGSUaf2OZG/eJE30o8LScG7eTK4faSbY9/aXGz5chS/xw5LwtSFnp8RPB8CVcMaAh/zBJoyl/iVKuri6i2XmVbD6SYJH/PAKfyzXKskvsU+sBsuYTXBKlxzndVQ+e4AjJdNBBasg0gH09rI6EMvOab4lsLmqFoXnoeOTmYngcXSl2EEdsnuYDDPz9YAJY6CILfNhYhWrs+JYmDdKzGxF1elpHZuN7ryUvrzicV+MCP2DpLbmSywihQxZSDWbleIx8dML6MnKM/E5lKhEfBdfFHIAs5P6Lk6CoyhXTBkwgmVLpRyGlUzdxY3rq5G28hAZ8pqN6VHHekBwXp/E8hTkSklbhbJDbTFFXWHxKeehV2ZcjBGmEEOBq1ySkzBVJAJxZT8b8/bdFsAEdAm1b1Qj7+FsuODebO3OqtCH4QWQI+KnTpmFJKY9CRZx8FyY3ral/Dhj4X4N+Goa7RE3+oQ0dx1EyIBxzP8JuC2cM5HIDC2fSblHSGfbu8rIYqOIitXjILQ0VwcIrTlXWsf4sCmwthdM+OkH4yGFbZfwc34ipyiOym/8fK1gSzua/FkZJl0ubW577Gc6h+2FMXqqM88vW9Cs78vX5d8HxQIOTJGiE05r7YUZ346KCZ+c/39I0x/FzWTu8AnYjfmlmNzsIUzc2yyMnfkbbis=");
            case 1:
                return new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYwODI1NDU1NDk0OSwKICAicHJvZmlsZUlkIiA6ICIzM2ViZDMyYmIzMzk0YWQ5YWM2NzBjOTZjNTQ5YmE3ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJEYW5ub0JhbmFubm9YRCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kYWQzM2FiNjRjODFiNDNiZjRiZjUyNjFkZmRkODRiODEyOTE1MmRkN2IxMjI2MWY1ZGFjYjc3MzYyOTIyYTVkIgogICAgfQogIH0KfQ==", "NWkfC4LjPAHCY5mvfjZ/QmtnvAJ8GOQnlg8U6k10si6e545TSNi5XpNIXS1cTuRdSoZ3mzWm4a00Cob6bOxb7G5o115UcX2rFzAT6nRNnAIkDkdYi3NxFcdip+pNDdOV+y+QR4J4VpZAwoMaGCsKyIMoT8CYeyexIO+ifYMpwZsEsLRlWaz+GNOes7Ql6+3aONsZuU9qTiRCRXyqBlDAWV3DyNToJDoudQ0Hl0L5KGbR4hUB913JHTlEuBfcRT3mHNmOrzMp9Zdr08q5SA2Fn1IZ9gMr+Dj1K+NEjkzyyRbZkjHR+CSzXajBvIztgGsdb0ZOPDckuPabVBvIQrHVj9Wq4A7u1z8H2cUroVOaH0ePIwi5YSCkeXW3sw8dyS4XSd7x6upjhAJaJ4hkrw8FFTeKYo96LbqROkO57A5fr8d/f+l0pfR4uSJtKtSMeeSkqPmJQsPWYx9tWeqSDEsr3HGekxO/hcuBrlSmyXjhmzdIO1zTUmWTxqaCfFxDx6JFan8QcbVCBxFm9Ro8KnTIOfcc+np82N4oFo67wZwZYOo27oV8NzKbwvRukdQGSNfT4xHwWMLR8wNsBUcOvoTco+lZDNL1Er1a4nKTigURPiokOVsnwvGR1B8nuE5xAil5F22644Yt8NQkzO06CcLFShw9FrLfbb3Znexp1VC2ztg=");
            case 2:
                return new Property("textures", "eyJ0aW1lc3RhbXAiOjE1NzUzMzg1MjYxMjUsInByb2ZpbGVJZCI6IjJjY2I1NDYxYmNhMDQ4YzE4ZGU4MjIwYWMzMjQ4ZWY3IiwicHJvZmlsZU5hbWUiOiJSZWluZGVlciIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjNiYjBjMzQ5M2M4YzdkOWE3NDAxNTRmOTMyMjhmM2JjMjkzZTA1ZWZlZTkyMzQwMTk2YzI0MTMzMGUxYTJkMiJ9fX0=", "LB3VTkXygcg9/8RrWZLjtz0vf+KeDHGiJIyX47KDScVNSvvOOykFy18ncKioGV1yXdXyPlyIa5b9vu/Mfhn+21ykDaMtzbwfgQ4K0VSyXYyH7aLpHGB3izE26ghYULfqUKMEAH6uz3SGrFW9UbsOv6YdNI4tSxvBMetgsRU46vf2QcSRRgw+7ap3ZBNRNG8ca62u+a4HiC+5bMVgRwOM6YOSrw7MXtJjNlz3bsS0we8okUhCMq+my2mcl1XTF34UIogCYFk2R7LgDP2Fzkh/f1srt2TMNjMtFoKlmXy3/GVMW1sM3plKGVcfARMJYTpVI+Tsq4Wrze+yzK89dvm/x0N71MpkDgdZxTnTFu7EsFfml1nyM4RwE2h0LU2K6BNm8UBShb5plD8PnuBNVuQJzG0IvVoRJWKqRjGAJL4qgLxSjgVWjsAbXBEgqsx8b2MIFWHSXkiycIW7ZZBhP/LAtIAsLaxFbnidpPBR7rdBQ7ERyqrGTDc1DLBRUpGTtQb4Q4kF0y0wh5gZA05wvn85fh+qAu8HxEo0vE5I+Wa+v5b8Uo0Udsby0yqEZXxGQERNXEYK6BPmu1CKu5OZJJ8DOe0JWZs8Lu0NYWnvyQkSRlhLZqePW8B9nXCHyWFTaSRoO3FB5VisTpzayMBP4v86fLI3CUbyb0hEPdZe0TV77Zs=");
            default:
                return new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYwNjc5MDYyMTExOCwKICAicHJvZmlsZUlkIiA6ICJlZDUzZGQ4MTRmOWQ0YTNjYjRlYjY1MWRjYmE3N2U2NiIsCiAgInByb2ZpbGVOYW1lIiA6ICI0MTQxNDE0MWgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODlhNDFmMGRiYzcyOTYyZWFlNzExODcwZTkyYTc3YmNkMDFjZGNjNmE2ZDQwZTExZTk2NDYzOWMyMWIyYjVmOSIKICAgIH0KICB9Cn0=", "pegOuK3kV049MM5A0yKTwMxUu2f9jOB14FMkWkXs9GG7f7KsIssOj1eRt/39j7aW9U+M4pULYM2CG6rlpyih6Qofw26IHubG4P594ybhIKxtMbSEbOBIl7xkZI86hul/3odPvwkghgngsp3aY1jfgTNVjfROF9rOVGfugkWdXvYX/+CJs+pKkwdkKZpeUmSSwlB7p0MGvKCY8LuBms4/g0k8ipTOfEK4upOLHD/vBA01GHX+Epspd8tKvucIJLD+VANu12DmT0d2JZKrIyga4zuH4phFqnlg3JoNBurgJhAsDSXWuOnQRtcDqv8KOg/W8KRuvmAP4TZB1amAyPND72nmteF9YinN0KH1QkBKb6d4gvkP/0RKz562Z9+srZnlxBBrJ5V7JpT7kshQHYHP4qy6zE8De/vomJa23GB0T1lQai2a/p0uOCMk+oXu7aKuf0MrxaFptEN+q2VYJBz+9l6VRxRyJJZhFA0Yc0o2eD9zeqEQNTulbY17VP1bIU06Fxuo+S2AMjdV3a+IMaYo8FGx6I5H5rGXrL3Uqnu5IJFXAFN1eHW2RulOpvWO1SUvUYQUTCIestFB87quWCINUFk/DjUGysEZ+7E6q5kNM75SaTCFsMmDI0wFCCRPD6Hv6kcHNu+xx7Bezse75kUsdSiNVyNC4B3mVNMBuBFn/Nw=");
        }
    }
}
