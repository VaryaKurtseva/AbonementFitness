package com.example.Abonement_demo_rest.graphql.types;

public class CreateUserInputGql {
    private String firstName;
    private String lastName;
    private String email;
    private String subscriptionActivation;
    private String endOfSubscription;
    private Integer visitsHall;
    private String numberPhone;

    // Конструктор по умолчанию (обязателен для Jackson/DGS)
    public CreateUserInputGql() {}

    // Конструктор со всеми полями
    public CreateUserInputGql(String firstName, String lastName, String email,
                              String subscriptionActivation, String endOfSubscription,
                              Integer visitsHall, String numberPhone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.subscriptionActivation = subscriptionActivation;
        this.endOfSubscription = endOfSubscription;
        this.visitsHall = visitsHall;
        this.numberPhone = numberPhone;
    }

    // Геттеры
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getSubscriptionActivation() { return subscriptionActivation; }
    public String getEndOfSubscription() { return endOfSubscription; }
    public Integer getVisitsHall() { return visitsHall; }
    public String getNumberPhone() { return numberPhone; }

    // Сеттеры
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setSubscriptionActivation(String subscriptionActivation) { this.subscriptionActivation = subscriptionActivation; }
    public void setEndOfSubscription(String endOfSubscription) { this.endOfSubscription = endOfSubscription; }
    public void setVisitsHall(Integer visitsHall) { this.visitsHall = visitsHall; }
    public void setNumberPhone(String numberPhone) { this.numberPhone = numberPhone; }

    @Override
    public String toString() {
        return "CreateUserInputGql{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", subscriptionActivation='" + subscriptionActivation + '\'' +
                ", endOfSubscription='" + endOfSubscription + '\'' +
                ", visitsHall=" + visitsHall +
                ", numberPhone='" + numberPhone + '\'' +
                '}';
    }
}