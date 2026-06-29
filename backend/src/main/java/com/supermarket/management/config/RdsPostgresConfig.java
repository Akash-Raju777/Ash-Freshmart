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

            // 1. Resolve AWS Credentials
            StaticCredentialsProvider credentialsProvider = null;
            if (accessKeyId != null && !accessKeyId.trim().isEmpty() &&
                secretAccessKey != null && !secretAccessKey.trim().isEmpty()) {
                credentialsProvider = StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKeyId.trim(), secretAccessKey.trim())
                );
            }

            // 2. Generate RDS IAM Database Authentication Token
            String token = "";
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
