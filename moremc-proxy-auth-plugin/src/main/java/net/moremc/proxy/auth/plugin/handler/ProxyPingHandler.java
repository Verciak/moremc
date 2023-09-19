package net.moremc.proxy.auth.plugin.handler;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.moremc.api.API;
import net.moremc.proxy.auth.plugin.helper.ChatHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProxyPingHandler implements Listener
{

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        event.getResponse().getVersion().setProtocol(1);
        event.getResponse().setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                "     &8&m---&d&m---&8&m--> &8 &5&lPay&f&lMC.pl &8&m <--&d&m---&8&m---&8\n" +
                        "    &fNasze sociale&8: &dfb.paymc.pl &8&m|&8 &ddc.paymc.pl")));

        List<String> motdList = new ArrayList<>();

        motdList.add("&8&m---&5&m---&8&m--> &8 &5&lPay&f&lMC.pl &8&m <--&5&m---&8&m---&8");
        motdList.add(" ");
        motdList.add(" &d│ &fFanpage&8: &dfb.paymc.pl");
        motdList.add(" &d│ &fDiscord&8: &ddc.paymc.pl");
        motdList.add(" &d│ &fTemSpeak: &dts.paymc.pl");
        motdList.add(" &d│ &fStrona: &dwww.paymc.pl");
        motdList.add(" ");
        motdList.add(" &d│ &fAktualnie wyświetlasz &d" + API.getInstance().getSectorName());
        motdList.add(" &d│ &fLicznik nie pokaże więcej niż &d1000 &fgraczy");

        ServerPing.PlayerInfo[] playerInfos = new ServerPing.PlayerInfo[motdList.size()];
        for (short i = 0; i < playerInfos.length; i++) {
            playerInfos[i] = new ServerPing.PlayerInfo(ChatHelper.colored(motdList.get(i)), UUID.randomUUID());
        }
        event.getResponse().getPlayers().setSample(playerInfos);

    }
}