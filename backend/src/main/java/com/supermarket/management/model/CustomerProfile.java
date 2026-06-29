package com.supermarket.management.model;

public class CustomerProfile {
    private String mobile;
    private String name;
    private Integer points;

    public CustomerProfile() {
    }

    public CustomerProfile(String mobile, String name, Integer points) {
        this.mobile = mobile;
        this.name = name;
        this.points = points;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
