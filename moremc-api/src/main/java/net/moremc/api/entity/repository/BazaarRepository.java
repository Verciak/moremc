package net.moremc.api.entity.repository;

import net.moremc.api.entity.bazaar.Bazaar;
import net.moremc.api.mysql.repository.json.JsonRepository;

public class BazaarRepository extends JsonRepository<String, Bazaar> {

    public BazaarRepository() {
        super("id", Bazaar.class, "bazaars");
    }
}
