package net.moremc.proxy.auth.plugin.helper;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

public class ChatHelper
{

    private static final Splitter NEW_LINE_SPLITTER = Splitter.on('\n');


    public static String colored(String text){
        return text.replace("&", "§");
    }

    public static List<String> colored(List<String> textList){
        return textList.stream().map(ChatHelper::colored).collect(Collectors.toList());
    }

    public static List<String> splitLinesToList(String s) {
        return NEW_LINE_SPLITTER.splitToList(s);
    }
}