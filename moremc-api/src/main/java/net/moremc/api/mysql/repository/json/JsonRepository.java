package net.moremc.api.mysql.repository.json;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.mysql.Identifiable;
import net.moremc.api.mysql.MySQL;
import net.moremc.api.mysql.repository.CrudRepository;
import net.moremc.api.service.ServiceImpl;

import java.sql.ResultSet;
import java.util.Collection;

public class JsonRepository<ID, T extends Identifiable<ID>> implements CrudRepository<ID, T> {


    private final ID fieldEntity;
    private final Class<T> entityClass;
    private final String databaseColumn;

    public JsonRepository(ID fieldEntity, Class<T> entityClass, String databaseColumn){
        this.fieldEntity = fieldEntity;
        this.entityClass = entityClass;
        this.databaseColumn = databaseColumn;
    }
    public void initializeDatabase(MySQL mySQL, ServiceImpl service){
        String stringBuilder = "CREATE TABLE IF NOT EXISTS `" + databaseColumn + "` (" +
                fieldEntity + " varchar(90)," +
                "json longText not null," +
                "primary key (" + fieldEntity + "));";
        mySQL.executeUpdate(stringBuilder);

        try {
            ResultSet resultSet = mySQL.executeQuery("SELECT * FROM `" + this.databaseColumn + "`");
            while (resultSet.next()){
                service.getMap().put(resultSet.getString(String.valueOf(fieldEntity)), new Gson().fromJson(resultSet.getString("json"), entityClass));
            }
            System.out.println("[" + this.getEntityClass().getSimpleName() + "] Loaded from database sql: " + service.getMap().values().size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void create(ID fieldEntity, T entity) {
        String stringBuilder = "INSERT INTO `" + this.databaseColumn + "` VALUES (" +
                "'" + fieldEntity + "'," +
                "'" + new Gson().toJson(entity) + "'" +
                ") ON DUPLICATE KEY UPDATE " +
                this.fieldEntity + "='" + fieldEntity + "'," +
                "json='" + new Gson().toJson(entity) + "'";
        API.getInstance().getMySQL().executeUpdate(stringBuilder);
    }

    @Override
    public void update(ID fieldEntity, T entity) {
        String stringBuilder = "INSERT INTO `" + this.databaseColumn + "` VALUES (" +
                "'" + fieldEntity + "'," +
                "'" + new Gson().toJson(entity) + "'" +
                ") ON DUPLICATE KEY UPDATE " +
                this.fieldEntity + "='" + fieldEntity + "'," +
                "json='" + new Gson().toJson(entity) + "'";
        API.getInstance().getMySQL().executeUpdate(stringBuilder);
    }

    @Override
    public void delete(ID fieldEntity, T entity) {

    }

    @Override
    public Collection<T> getEntities() {
        return null;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public ID getFieldEntity() {
        return fieldEntity;
    }

    public String getDatabaseColumn() {
        return databaseColumn;
    }
}
