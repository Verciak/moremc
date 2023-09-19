package net.moremc.proxy.auth.plugin.helper;


import net.moremc.api.entity.account.Account;
import org.mindrot.jbcrypt.BCrypt;

public class HashHelper {

    public static String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(Account account, String password) {
        return (!BCrypt.checkpw(password, account.getPassword()));
    }
}
