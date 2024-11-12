package com.epam.jmp.impl;

import com.epam.jmp.dto.BankCard;
import com.epam.jmp.dto.Subscription;
import com.epam.jmp.dto.User;
import com.epam.jmp.service.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class ServiceImpl implements Service {

    final List<Subscription> subscriptions = new ArrayList<>();
    final Set<User> users = new HashSet<>();

    @Override
    public void subscribe(BankCard bankCard) {
        Objects.requireNonNull(bankCard, "bankCard is null");
        var subscription = new Subscription(bankCard.getNumber(), LocalDate.now());
        users.add(bankCard.getUser());
        subscriptions.add(subscription);
    }

    @Override
    public Subscription getSubscriptionByBankCardNumber(String cardNumber) {
        Objects.requireNonNull(cardNumber, "cardNumber is null");
        return subscriptions.stream()
                .filter(s -> cardNumber.equals(s.getBankcard()))
                .reduce((s1, s2) -> {
                    throw new IllegalArgumentException(String.format("More than one subscription found for card # '%s'", cardNumber));
                }).orElseThrow(SubscriptionNotFoundException::new);
    }

    @Override
    public List<User> getAllUsers() {
        return users.stream()
                .sorted(Comparator.comparing(User::getSurname).thenComparing(User::getName).thenComparing(User::getBirthday))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> condition) {
        Objects.requireNonNull(condition, "condition is null");
        return subscriptions.stream().filter(condition).toList();
    }
}
