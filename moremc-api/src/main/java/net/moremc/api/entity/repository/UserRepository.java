package net.moremc.api.entity.repository;

import net.moremc.api.entity.user.User;
import net.moremc.api.mysql.repository.json.JsonRepository;

public class UserRepository extends JsonRepository<String, User> {

    public UserRepository() {
        super("nickName", User.class, "users");
    }
}
