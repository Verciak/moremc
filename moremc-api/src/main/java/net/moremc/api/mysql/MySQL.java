package net.moremc.api.mysql;

import com.zaxxer.hikari.HikariDataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class MySQL {

    private Properties properties;
    private final HikariDataSource dataSource;

    public MySQL() {

        try {this.createOrLoadProperties(); } catch (IOException exception) {exception.printStackTrace();}

        this.dataSource = new HikariDataSource();
        this.dataSource.setJdbcUrl(properties.getProperty("db.jdbc"));
        this.dataSource.setUsername(properties.getProperty("db.user"));
        this.dataSource.setPassword(properties.getProperty("db.password"));
        this.dataSource.addDataSourceProperty("cachePrepStmts", true);
        this.dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        this.dataSource.addDataSourceProperty("useServerPrepStmts", true);
        this.dataSource.addDataSourceProperty("rewriteBatchedStatements", true);
        this.dataSource.addDataSourceProperty("useSSL", false);
        this.dataSource.setConnectionTimeout(15000L);
        this.dataSource.setMaximumPoolSize(5);
    }

    public void executeUpdate(final String query) {
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(query)) {
            if (statement == null) return;

            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(final String query) {
        try (final Connection connection = this.dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createOrLoadProperties() throws IOException {
        properties = new Properties();
        final File file = new File("database.properties");

        if (file.isFile() && file.canRead()) {
            final InputStream inputStream = new FileInputStream("database.properties");

            properties.load(inputStream);
        } else {

            final OutputStream outputStream = new FileOutputStream("database.properties");

            properties.setProperty("db.jdbc", "jdbc:mysql://mysql.titanaxe.com:3306/srv247101");
            properties.setProperty("db.user", "srv247101");
            properties.setProperty("db.password", "Hy4ztCp4");

            properties.store(outputStream,null);
        }
    }
}
