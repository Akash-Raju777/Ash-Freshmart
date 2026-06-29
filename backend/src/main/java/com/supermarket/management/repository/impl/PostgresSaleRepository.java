package com.supermarket.management.repository.impl;

import com.supermarket.management.model.Sale;
import com.supermarket.management.repository.SaleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Profile("rds")
public class PostgresSaleRepository implements SaleRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostgresSaleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Sale> rowMapper = new RowMapper<Sale>() {
        @Override
        public Sale mapRow(ResultSet rs, int rowNum) throws SQLException {
            Sale s = new Sale();
            s.setBillId(rs.getString("billId"));
            s.setProductId(rs.getString("productId"));
            s.setQuantitySold(rs.getInt("quantitySold"));
            s.setSaleDate(rs.getString("saleDate"));
            s.setTotalAmount(rs.getDouble("totalAmount"));
            return s;
        }
    };

    @Override
    public List<Sale> findAll() {
        return jdbcTemplate.query("SELECT billId, productId, quantitySold, saleDate, totalAmount FROM sales", rowMapper);
    }

    @Override
    public List<Sale> findByBillId(String billId) {
        return jdbcTemplate.query(
                "SELECT billId, productId, quantitySold, saleDate, totalAmount FROM sales WHERE billId = ?",
                rowMapper,
                billId
        );
    }

    @Override
    public Sale save(Sale sale) {
        // Since billId + productId is the primary key:
        int count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sales WHERE billId = ? AND productId = ?",
                Integer.class,
                sale.getBillId(),
                sale.getProductId()
        );

        if (count > 0) {
            jdbcTemplate.update(
                    "UPDATE sales SET quantitySold = ?, saleDate = ?, totalAmount = ? WHERE billId = ? AND productId = ?",
                    sale.getQuantitySold(),
                    sale.getSaleDate(),
                    sale.getTotalAmount(),
                    sale.getBillId(),
                    sale.getProductId()
            );
        } else {
            jdbcTemplate.update(
                    "INSERT INTO sales (billId, productId, quantitySold, saleDate, totalAmount) VALUES (?, ?, ?, ?, ?)",
                    sale.getBillId(),
                    sale.getProductId(),
                    sale.getQuantitySold(),
                    sale.getSaleDate(),
                    sale.getTotalAmount()
            );
        }
        return sale;
    }
}
