package com.epam.jmp.dto;

import java.time.LocalDate;
import java.util.Objects;

public class Subscription {
    private String bankcard;
    private LocalDate startDate;

    public Subscription(String bankcard, LocalDate startDate) {
        this.bankcard = bankcard;
        this.startDate = startDate;
    }

    public String getBankcard() {
        return bankcard;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(bankcard, that.bankcard) && Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankcard, startDate);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "bankcard='" + bankcard + '\'' +
                ", startDate=" + startDate +
                '}';
    }
}
