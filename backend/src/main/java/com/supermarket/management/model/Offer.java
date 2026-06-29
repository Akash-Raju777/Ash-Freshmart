package com.supermarket.management.model;

public class Offer {
    private String offerId;
    private String productId;
    private String offerType;
    private Double discount;
    private Boolean active;
    private String startDate;
    private String endDate;

    public Offer() {
    }

    public Offer(String offerId, String productId, String offerType, Double discount, Boolean active, String startDate, String endDate) {
        this.offerId = offerId;
        this.productId = productId;
        this.offerType = offerType;
        this.discount = discount;
        this.active = active;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public static OfferBuilder builder() {
        return new OfferBuilder();
    }

    public static class OfferBuilder {
        private String offerId;
        private String productId;
        private String offerType;
        private Double discount;
        private Boolean active;
        private String startDate;
        private String endDate;

        public OfferBuilder offerId(String offerId) {
            this.offerId = offerId;
            return this;
        }

        public OfferBuilder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public OfferBuilder offerType(String offerType) {
            this.offerType = offerType;
            return this;
        }

        public OfferBuilder discount(Double discount) {
            this.discount = discount;
            return this;
        }

        public OfferBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public OfferBuilder startDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public OfferBuilder endDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public Offer build() {
            return new Offer(offerId, productId, offerType, discount, active, startDate, endDate);
        }
    }
}
