package org.example.pojo;

import java.util.List;

public class OrderCreateRequest {
    private String firstName = "Михаил";
    private String lastName = "Дрожжин";
    private String address = "Москва, ул. Пушкина, д.1, кв.100";
    private int metroStation = 1;
    private String phone = "8 900 123 45 67";
    private String rentTime = "1";
    private String deliveryDate = "2025-01-01";
    private String comment = "Пожалуйста, доставьте чистый самокат";
    private List<String> color;

    public OrderCreateRequest (List<String> color) {
        this.color = color;
    }

    public OrderCreateRequest () {}

    public int getMetroStation() {
        return metroStation;
    }

    public void setMetroStation(int metroStation) {
        this.metroStation = metroStation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRentTime() {
        return rentTime;
    }

    public void setRentTime(String rentTime) {
        this.rentTime = rentTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

}
