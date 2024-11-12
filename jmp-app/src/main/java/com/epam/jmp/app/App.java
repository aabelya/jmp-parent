package com.epam.jmp.app;

import com.epam.jmp.bank.Bank;
import com.epam.jmp.dto.BankCard;
import com.epam.jmp.dto.BankCardType;
import com.epam.jmp.dto.Subscription;
import com.epam.jmp.dto.User;
import com.epam.jmp.service.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

/**
 * Hello world!
 *
 */
public class App 
{
    final Bank bank;
    final Service service;

    private App() {
        ServiceLoader<Bank> banks = ServiceLoader.load(Bank.class);
        bank = banks.stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Bank implementation not loaded")).get();

        ServiceLoader<Service> services = ServiceLoader.load(Service.class);
        service = services.stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Service implementation not loaded")).get();
    }

    private void doBanking() {
        System.out.println("\nLet's do some banking!\n");
        List<BankCard> issuedCards = createCardsAndSubscriptions();
        Map<User, List<BankCard>> issuedCardsPerUser = issuedCards.stream()
                .collect(groupingBy(BankCard::getUser));

        List<User> allUsers = service.getAllUsers();
        System.out.printf("Users registered: %d. Average age: %.3f\n", allUsers.size(), service.getAverageUsersAge());
        for (User user : allUsers) {
            System.out.printf("User: %s,%s; DOB: %s, Is payable: %s\n",
                    user.getSurname(), user.getName(), user.getBirthday().format(ISO_DATE), Service.isPayableUser(user)? "YES" : "NO");
            issuedCardsPerUser.getOrDefault(user, Collections.emptyList())
                    .forEach(card -> System.out.printf("\t- Card no: %s; Type: %s; Subscription Date: %s\n",
                            card.getNumber(), card.getClass().getSimpleName(), getSubscriptionDate(card).format(ISO_DATE)));
        }

        System.out.println();
        getSubscriptionsByCardType(issuedCards).forEach((type, subs) ->
                        System.out.printf("Subscriptions for %s type: %d\n", type, subs.size()));
        System.out.println("\nBanking succeeded!\n");
    }

    private List<BankCard> createCardsAndSubscriptions() {
        User johnDoe = new User("John", "Doe",
                LocalDate.now().minusYears(40).minusMonths(3).minusDays(10));
        User janeDoe = new User("Jane", "Doe",
                LocalDate.now().minusYears(37).minusMonths(5).minusDays(7));
        User tomThumb = new User("Tom", "Thumb",
                LocalDate.now().minusYears(15).minusMonths(8).minusDays(20));

        List<BankCard> issuedCards = new ArrayList<>();
        Stream.of(johnDoe, janeDoe, tomThumb)
                .map(usr -> bank.createBankCard(usr, BankCardType.DEBIT)).peek(issuedCards::add).forEach(service::subscribe);
        Stream.of(johnDoe, janeDoe)
                .map(usr -> bank.createBankCard(usr, BankCardType.CREDIT)).peek(issuedCards::add).forEach(service::subscribe);
        return issuedCards;
    }

    private LocalDate getSubscriptionDate(BankCard card) {
        return service.getSubscriptionByBankCardNumber(card.getNumber()).getStartDate();
    }

    private Map<String, List<Subscription>> getSubscriptionsByCardType(List<BankCard> issuedCards) {
        var cardsNumbersByType = issuedCards.stream().collect(
                groupingBy(BankCard::getClass, mapping(BankCard::getNumber, Collectors.toSet())));
        return cardsNumbersByType.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey().getSimpleName(),
                entry -> service.getAllSubscriptionsByCondition(cardNumberIn(entry.getValue()))
        ));
    }

    private Predicate<Subscription> cardNumberIn(Collection<String> collection) {
        return s ->  Optional.ofNullable(s).map(Subscription::getBankcard)
                .filter(collection::contains).isPresent() ;
    }


    public static void main(String[] args )
    {
        new App().doBanking();
    }


}
