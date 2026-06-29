package com.supermarket.management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsUtilities;
import software.amazon.awssdk.services.rds.model.GenerateAuthenticationTokenRequest;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

@Configuration
public class RdsPostgresConfig {

    @Value("${aws.accessKeyId:}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey:}")
    private String secretAccessKey;

    @Bean
    public CommandLineRunner testRdsConnection() {
        return args -> {
            loadDotEnv();

            String rdsHost = System.getProperty("RDSHOST");
            String rdsPortStr = System.getProperty("RDS_PORT", "5432");
            String rdsDbName = System.getProperty("RDS_DBNAME", "postgres");
            String rdsUser = System.getProperty("RDS_USER", "postgres");
            String rdsRegion = System.getProperty("RDS_REGION", "ap-northeast-3");
            String sslMode = System.getProperty("RDS_SSLMODE", "verify-full");
            String sslRootCert = System.getProperty("RDS_SSLROOTCERT", "./global-bundle.pem");

            if (rdsHost == null || rdsHost.trim().isEmpty()) {
                System.out.println("AWS RDS host is not configured in .env file. Skipping connection test.");
                return;
            }

            int rdsPort = 5432;
            try {
                rdsPort = Integer.parseInt(rdsPortStr);
            } catch (NumberFormatException e) {
                // Ignore, use default 5432
            }

            System.out.println("----------------------------------------------------------------");
            System.out.println("AWS RDS PostgreSQL Connection Diagnostics");
            System.out.println("Host: " + rdsHost);
            System.out.println("Port: " + rdsPort);
            System.out.println("Database: " + rdsDbName);
            System.out.println("User: " + rdsUser);
            System.out.println("Region: " + rdsRegion);
            System.out.println("----------------------------------------------------------------");

            // 1. Resolve AWS Credentials: check properties first, then default provider
            software.amazon.awssdk.auth.credentials.AwsCredentialsProvider credentialsProvider;
            if (accessKeyId != null && !accessKeyId.trim().isEmpty() &&
                secretAccessKey != null && !secretAccessKey.trim().isEmpty()) {
                credentialsProvider = software.amazon.awssdk.auth.credentials.StaticCredentialsProvider.create(
                    software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create(accessKeyId.trim(), secretAccessKey.trim())
                );
                System.out.println("Using static AWS credentials from properties.");
            } else {
                credentialsProvider = software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider.create();
                System.out.println("Using standard AWS DefaultCredentialsProvider.");
            }

            // 2. Generate or use pre-configured RDS IAM Database Authentication Token
            String token = System.getProperty("RDS_PASSWORD");
            if (token != null && !token.trim().isEmpty()) {
                System.out.println("Using pre-configured AWS RDS IAM DB Auth Token from .env!");
            } else {
                try {
                    RdsUtilities rdsUtilities = RdsUtilities.builder()
                            .region(Region.of(rdsRegion))
                            .credentialsProvider(credentialsProvider)
                            .build();

                    GenerateAuthenticationTokenRequest tokenRequest = GenerateAuthenticationTokenRequest.builder()
                            .credentialsProvider(credentialsProvider)
                            .hostname(rdsHost)
                            .port(rdsPort)
                            .username(rdsUser)
                            .build();

                    token = rdsUtilities.generateAuthenticationToken(tokenRequest);
                    System.out.println("Successfully generated AWS RDS IAM DB Auth Token!");
                } catch (Exception e) {
                    System.err.println("Failed to generate AWS RDS IAM DB Auth Token: " + e.getMessage());
                    return;
                }
            }

            // 3. Attempt to connect via JDBC using dynamic IAM token as password
            String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", rdsHost, rdsPort, rdsDbName);
            
            Properties props = new Properties();
            props.setProperty("user", rdsUser);
            props.setProperty("password", token);
            props.setProperty("ssl", "true");
            props.setProperty("sslmode", sslMode);
            props.setProperty("sslrootcert", sslRootCert);

            System.out.println("Connecting to RDS PostgreSQL database via JDBC...");
            try (Connection conn = DriverManager.getConnection(jdbcUrl, props)) {
                System.out.println("SUCCESS: Successfully connected to AWS RDS PostgreSQL database!");
                
                System.out.println("Executing RDS 'db push' schema initialization...");
                try (java.sql.Statement stmt = conn.createStatement()) {
                    stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                            "id VARCHAR(100) PRIMARY KEY, " +
                            "name VARCHAR(255), " +
                            "brand VARCHAR(255), " +
                            "photoUrl VARCHAR(1024), " +
                            "mfgDate VARCHAR(50), " +
                            "expDate VARCHAR(50), " +
                            "arrivingDate VARCHAR(50), " +
                            "quantity INTEGER, " +
                            "price DOUBLE PRECISION)");
                    System.out.println("-> Table 'products' verified/created.");

                    stmt.execute("CREATE TABLE IF NOT EXISTS offers (" +
                            "offerId VARCHAR(100) PRIMARY KEY, " +
                            "productId VARCHAR(100), " +
                            "offerType VARCHAR(100), " +
                            "discount DOUBLE PRECISION, " +
                            "active BOOLEAN, " +
                            "startDate VARCHAR(50), " +
                            "endDate VARCHAR(50))");
                    System.out.println("-> Table 'offers' verified/created.");

                    stmt.execute("CREATE TABLE IF NOT EXISTS sales (" +
                            "billId VARCHAR(100), " +
                            "productId VARCHAR(100), " +
                            "quantitySold INTEGER, " +
                            "saleDate VARCHAR(50), " +
                            "totalAmount DOUBLE PRECISION, " +
                            "PRIMARY KEY (billId, productId))");
                    System.out.println("-> Table 'sales' verified/created.");

                    stmt.execute("CREATE TABLE IF NOT EXISTS notifications (" +
                            "notificationId VARCHAR(100) PRIMARY KEY, " +
                            "type VARCHAR(100), " +
                            "message VARCHAR(1024), " +
                            "productId VARCHAR(100), " +
                            "timestamp VARCHAR(100), " +
                            "readStatus BOOLEAN)");
                    System.out.println("-> Table 'notifications' verified/created.");

                    stmt.execute("CREATE TABLE IF NOT EXISTS customer_profiles (" +
                            "mobile VARCHAR(100) PRIMARY KEY, " +
                            "name VARCHAR(255), " +
                            "points INTEGER)");
                    System.out.println("-> Table 'customer_profiles' verified/created.");

                    stmt.execute("CREATE TABLE IF NOT EXISTS user_accounts (" +
                            "username VARCHAR(100) PRIMARY KEY, " +
                            "password VARCHAR(255), " +
                            "role VARCHAR(50))");
                    System.out.println("-> Table 'user_accounts' verified/created.");
                    
                    System.out.println("SUCCESS: RDS 'db push' schema initialization completed successfully!");
                } catch (Exception e) {
                    System.err.println("DB PUSH ERROR: Failed to create tables in PostgreSQL: " + e.getMessage());
                }
            } catch (Exception e) {
                System.err.println("CONNECTION ERROR: Failed to connect to AWS RDS PostgreSQL database.");
                System.err.println("Details: " + e.getMessage());
                System.err.println("Tip: Check if your local public IP is allowed in the RDS Security Group inbound rules for port " + rdsPort + ".");
            }
            System.out.println("----------------------------------------------------------------");
        };
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
