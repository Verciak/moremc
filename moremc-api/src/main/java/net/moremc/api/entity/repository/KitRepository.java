package net.moremc.api.entity.repository;

import net.moremc.api.entity.kit.Kit;
import net.moremc.api.mysql.repository.json.JsonRepository;

public class KitRepository extends JsonRepository<String, Kit> {

    public KitRepository() {
        super("name", Kit.class, "kits");
    }
}
