package net.moremc.api.entity.repository;


import net.moremc.api.entity.ban.Ban;
import net.moremc.api.mysql.repository.json.JsonRepository;

public class BanRepository extends JsonRepository<String, Ban> {

    public BanRepository() {
        super("nickName", Ban.class, "bans");
    }
}
