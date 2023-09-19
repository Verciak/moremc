package net.moremc.api.entity.repository;

import net.moremc.api.entity.account.Account;
import net.moremc.api.mysql.repository.json.JsonRepository;

public class AccountRepository extends JsonRepository<String, Account> {

    public AccountRepository() {
        super("nickName", Account.class, "accounts");
    }
}
