package net.moremc.api.mysql.repository;

import net.moremc.api.mysql.Identifiable;

import java.util.Collection;

public interface CrudRepository<ID,T extends Identifiable<ID>> extends Repository<ID, T>{

    void create(ID fieldEntity, T entity);

    void update(ID fieldEntity, T entity);

    void delete(ID fieldEntity, T entity);

    Collection<T> getEntities();

}
