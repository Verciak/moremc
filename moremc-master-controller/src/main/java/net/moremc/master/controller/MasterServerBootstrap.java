package net.moremc.master.controller;

import java.util.concurrent.locks.LockSupport;

public class MasterServerBootstrap {

    public static void main(String[] args) {
        new MasterServerController();
        LockSupport.park();
    }

}
/*
iptables -A INPUT -p tcp --dport 25565 -m state --state NEW -j ACCEPT
iptables -A INPUT -p tcp --dport 25565 -m state --state NEW -j ACCEPT
iptables -A INPUT -p udp --dport 25565 -m state --state NEW -j ACCEPT
iptables -A OUTPUT -p tcp --dport 25565 -m state --state NEW -j ACCEPT
iptables -A OUTPUT -p udp --dport 25565 -m state --state NEW -j ACCEPT
 */