package net.moremc.api.entity.repository;

import net.moremc.api.entity.server.Server;
import net.moremc.api.mysql.repository.json.JsonRepository;

public class ServerRepository extends JsonRepository<String, Server> {

    public ServerRepository() {
        super("id", Server.class, "server");
    }
}
