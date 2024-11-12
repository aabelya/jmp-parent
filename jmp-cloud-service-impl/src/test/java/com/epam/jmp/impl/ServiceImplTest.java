package com.epam.jmp.impl;

import com.epam.jmp.dto.BankCard;
import com.epam.jmp.dto.Subscription;
import com.epam.jmp.dto.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ServiceImplTest {

    ServiceImpl sut = new ServiceImpl();

    @Test
    void should_fail_to_subscribe_if_bank_card_is_null() {
        assertThrows(NullPointerException.class, () -> sut.subscribe(null));
    }

    @Test
    void should_get_subscription_by_bank_card_number() {
        // given
        final String bankCardNo = "bank_card_no";
        sut.subscribe(mockCard(bankCardNo));
        // when
        Subscription actual = sut.getSubscriptionByBankCardNumber(bankCardNo);
        // then
        assertNotNull(actual);
        assertEquals(bankCardNo, actual.getBankcard());
    }

    @Test
    void should_fail_to_get_subscription_by_bank_card_number_if_more_than_one_found() {
        // given
        final String bankCardNo = "bank_card_no";
        sut.subscribe(mockCard(bankCardNo)); //subscribe twice
        sut.subscribe(mockCard(bankCardNo));
        // when/// then
       assertThrows(IllegalArgumentException.class, () -> sut.getSubscriptionByBankCardNumber(bankCardNo));
    }

    @Test
    void should_fail_to_get_subscription_by_bank_card_number_if_more_than_not_found() {
        assertThrows(SubscriptionNotFoundException.class, () -> sut.getSubscriptionByBankCardNumber("bank_card_no"));
    }

    @Test
    void should_get_all_users() {
        // given
        User userA = mock(User.class);
        User userB = mock(User.class);
        sut.subscribe(mockCard(userA));
        sut.subscribe(mockCard(userB));
        // when
        List<User> actual = sut.getAllUsers();
        // then
        List<User> expected = List.of(userA, userB);
        assertEquals(expected.size(), actual.size());
        assertTrue(actual.containsAll(expected));
    }

    @Test
    void should_get_all_subscriptions_by_condition() {
        // given
        List<String> cardNumbers = IntStream.range(0, 10).mapToObj(i -> "card_number_" + i).toList();
        List<String> matchingCardNumbers = cardNumbers.subList(0, cardNumbers.size() / 2);
        cardNumbers.stream().map(this::mockCard).forEach(sut::subscribe);
        // when
        Predicate<Subscription> condition = s -> matchingCardNumbers.contains(s.getBankcard());
        List<Subscription> actual = sut.getAllSubscriptionsByCondition(condition);
        // then
        assertEquals(matchingCardNumbers.size(), actual.size());
        assertTrue(actual.stream().allMatch(condition));
    }

    @Test
    void should_fail_to_get_all_subscriptions_by_condition_if_condition_is_null() {
        assertThrows(NullPointerException.class, () -> sut.getAllSubscriptionsByCondition(null));
    }

    private BankCard mockCard(String cardNo) {
        return mockCard(cardNo, mock(User.class));
    }

    private BankCard mockCard(User user) {
        return mockCard("bank_card_no", user);
    }

    private BankCard mockCard(String cardNo, User user) {
        BankCard cardMock = mock(BankCard.class);
        Mockito.when(cardMock.getNumber()).thenReturn(cardNo);
        Mockito.when(cardMock.getUser()).thenReturn(user);
        return cardMock;
    }
}