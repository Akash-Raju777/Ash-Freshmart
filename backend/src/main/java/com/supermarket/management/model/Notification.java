package com.supermarket.management.model;

public class Notification {
    private String notificationId;
    private String type;
    private String message;
    private String productId;
    private String timestamp;
    private Boolean readStatus;

    public Notification() {
    }

    public Notification(String notificationId, String type, String message, String productId, String timestamp, Boolean readStatus) {
        this.notificationId = notificationId;
        this.type = type;
        this.message = message;
        this.productId = productId;
        this.timestamp = timestamp;
        this.readStatus = readStatus;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Boolean readStatus) {
        this.readStatus = readStatus;
    }

    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }

    public static class NotificationBuilder {
        private String notificationId;
        private String type;
        private String message;
        private String productId;
        private String timestamp;
        private Boolean readStatus;

        public NotificationBuilder notificationId(String notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        public NotificationBuilder type(String type) {
            this.type = type;
            return this;
        }

        public NotificationBuilder message(String message) {
            this.message = message;
            return this;
        }

        public NotificationBuilder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public NotificationBuilder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public NotificationBuilder readStatus(Boolean readStatus) {
            this.readStatus = readStatus;
            return this;
        }

        public Notification build() {
            return new Notification(notificationId, type, message, productId, timestamp, readStatus);
        }
    }
}
