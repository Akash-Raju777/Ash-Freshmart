package com.supermarket.management.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@Profile("rds")
public class PostgresConfig {

    @Bean
    public DataSource dataSource() {
        loadDotEnv();

        String rdsHost = System.getProperty("RDSHOST");
        String rdsPort = System.getProperty("RDS_PORT", "5432");
        String rdsDbName = System.getProperty("RDS_DBNAME", "postgres");
        String rdsUser = System.getProperty("RDS_USER", "postgres");
        String rdsPassword = System.getProperty("RDS_PASSWORD");
        String sslMode = System.getProperty("RDS_SSLMODE", "verify-full");
        String sslRootCert = System.getProperty("RDS_SSLROOTCERT", "./global-bundle.pem");

        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", rdsHost, rdsPort, rdsDbName);

        File certFile = new File(sslRootCert);
        if (!certFile.isAbsolute()) {
            if (!certFile.exists()) {
                File parentCert = new File("../" + sslRootCert);
                if (parentCert.exists()) {
                    sslRootCert = parentCert.getAbsolutePath();
                }
            } else {
                sslRootCert = certFile.getAbsolutePath();
            }
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(rdsUser);
        config.setPassword(rdsPassword);
        
        config.addDataSourceProperty("ssl", "true");
        config.addDataSourceProperty("sslmode", sslMode);
        config.addDataSourceProperty("sslrootcert", sslRootCert);
        
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);

        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    private void loadDotEnv() {
        try {
            File file = new File(".env");
            if (!file.exists()) {
                file = new File("backend/.env");
            }
            if (file.exists()) {
                java.nio.file.Files.lines(file.toPath()).forEach(line -> {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("#") && line.contains("=")) {
                        int index = line.indexOf("=");
                        String key = line.substring(0, index).trim();
                        String value = line.substring(index + 1).trim();
                        System.setProperty(key, value);
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Could not load .env file: " + e.getMessage());
        }
    }
}
