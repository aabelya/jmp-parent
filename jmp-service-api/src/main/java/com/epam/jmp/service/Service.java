package com.epam.jmp.service;

import com.epam.jmp.dto.BankCard;
import com.epam.jmp.dto.Subscription;
import com.epam.jmp.dto.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public interface Service {

    void subscribe(BankCard bankCard);

    Subscription getSubscriptionByBankCardNumber(String cardNumber);

    List<User> getAllUsers();

    List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> condition);

    default double getAverageUsersAge() {
        List<User> allUsers = getAllUsers();
        Objects.requireNonNull(allUsers, "getAllUsers returned null");
        var today = LocalDate.now();
        return allUsers.stream().filter(Objects::nonNull)
                .map(User::getBirthday).filter(Objects::nonNull)
                .mapToDouble(bd -> ChronoUnit.YEARS.between(bd, today))
                .average()
                .orElse(0d);
    }

    static boolean isPayableUser(User user) {
        Objects.requireNonNull(user, "user is null");
        var birthday = Optional.ofNullable(user.getBirthday())
                .orElseThrow(() -> new IllegalArgumentException("user age is unknown"));
        var payableAgeLimit = 18;
        return ChronoUnit.YEARS.between(birthday, LocalDate.now()) >= payableAgeLimit;
    }
}
