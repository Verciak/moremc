package net.moremc.api.entity.repository;

import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.mysql.repository.json.JsonRepository;

public class GuildRepository extends JsonRepository<String, GuildImpl> {

    public GuildRepository() {
        super("name", GuildImpl.class, "guilds");
    }
}
