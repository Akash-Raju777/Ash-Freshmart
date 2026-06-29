package com.supermarket.management.model;

public class Product {
    private String id;
    private String name;
    private String brand;
    private String photoUrl;
    private String mfgDate;
    private String expDate;
    private String arrivingDate;
    private Integer quantity;
    private Double price;
    
    // New Fields
    private String sku;
    private String barcode;
    private String category;
    private Double costPrice;
    private Double gst;
    private String supplier;
    private String batchNumber;
    private String productStatus;

    public Product() {
    }

    public Product(String id, String name, String brand, String photoUrl, String mfgDate, String expDate, String arrivingDate, Integer quantity, Double price) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.photoUrl = photoUrl;
        this.mfgDate = mfgDate;
        this.expDate = expDate;
        this.arrivingDate = arrivingDate;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(String mfgDate) {
        this.mfgDate = mfgDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getArrivingDate() {
        return arrivingDate;
    }

    public void setArrivingDate(String arrivingDate) {
        this.arrivingDate = arrivingDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // New Getters & Setters
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Double getGst() {
        return gst;
    }

    public void setGst(Double gst) {
        this.gst = gst;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private String id;
        private String name;
        private String brand;
        private String photoUrl;
        private String mfgDate;
        private String expDate;
        private String arrivingDate;
        private Integer quantity;
        private Double price;
        private String sku;
        private String barcode;
        private String category;
        private Double costPrice;
        private Double gst;
        private String supplier;
        private String batchNumber;
        private String productStatus;

        public ProductBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public ProductBuilder photoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public ProductBuilder mfgDate(String mfgDate) {
            this.mfgDate = mfgDate;
            return this;
        }

        public ProductBuilder expDate(String expDate) {
            this.expDate = expDate;
            return this;
        }

        public ProductBuilder arrivingDate(String arrivingDate) {
            this.arrivingDate = arrivingDate;
            return this;
        }

        public ProductBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public ProductBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public ProductBuilder sku(String sku) {
            this.sku = sku;
            return this;
        }

        public ProductBuilder barcode(String barcode) {
            this.barcode = barcode;
            return this;
        }

        public ProductBuilder category(String category) {
            this.category = category;
            return this;
        }

        public ProductBuilder costPrice(Double costPrice) {
            this.costPrice = costPrice;
            return this;
        }

        public ProductBuilder gst(Double gst) {
            this.gst = gst;
            return this;
        }

        public ProductBuilder supplier(String supplier) {
            this.supplier = supplier;
            return this;
        }

        public ProductBuilder batchNumber(String batchNumber) {
            this.batchNumber = batchNumber;
            return this;
        }

        public ProductBuilder productStatus(String productStatus) {
            this.productStatus = productStatus;
            return this;
        }

        public Product build() {
            Product p = new Product(id, name, brand, photoUrl, mfgDate, expDate, arrivingDate, quantity, price);
            p.setSku(sku);
            p.setBarcode(barcode);
            p.setCategory(category);
            p.setCostPrice(costPrice);
            p.setGst(gst);
            p.setSupplier(supplier);
            p.setBatchNumber(batchNumber);
            p.setProductStatus(productStatus);
            return p;
        }
    }
}
