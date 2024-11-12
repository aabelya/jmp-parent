package com.epam.jmp.service;

import com.epam.jmp.dto.BankCard;
import com.epam.jmp.dto.Subscription;
import com.epam.jmp.dto.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ServiceTest {

    @Test
    void should_calculate_average_users_age() {
        // given
        int[] ages = { 13, 24, 35, 46, 57, 68 };
        var today = LocalDate.now();
        List<User> users = IntStream.of(ages)
                .mapToObj(age -> mockUserWithAge(today, age))
                .collect(Collectors.toList());
        Service sut = createServiceWithUsers(users);
        // when
        double actual = sut.getAverageUsersAge();
        //then
        double expected = IntStream.of(ages).average().orElse(0d);
        assertEquals(expected, actual);
    }

    @Test
    void should_ignore_nulls_when_calculating_average_users_age() {
        // given
        int[] ages = { 13, 24, 35, 46, 57, 68 };
        var today = LocalDate.now();
        List<User> users = IntStream.of(ages)
                .mapToObj(age -> mockUserWithAge(today, age))
                .collect(Collectors.toList());
        users.add(users.size()/2, null);
        users.add(users.size()/2, Mockito.mock(User.class)); //null birthday user
        Service sut = createServiceWithUsers(users);
        // when
        double actual = sut.getAverageUsersAge();
        // then
        double expected = IntStream.of(ages).average().orElse(0d);
        assertEquals(expected, actual);
    }

    @Test
    void should_fail_calculate_average_users_age_when_getAllUsers_returns_null() {
        // given
        Service sut = createServiceWithUsers(null);
        // when/then
        assertThrows(NullPointerException.class, sut::getAverageUsersAge);
    }

    @Test
    void should_return_0_when_calculating_average_users_age_if_getAllUsers_returns_empty_list() {
        // given
        Service sut = createServiceWithUsers(Collections.emptyList());
        // when
        double actual = sut.getAverageUsersAge();
        // then
        assertEquals(0d, actual);
    }

    @Test
    void test_isPayableUser_true() {
        User user = mockUserWithAge(LocalDate.now(), 18);
        assertTrue(Service.isPayableUser(user));
    }

    @Test
    void test_isPayableUser_false() {
        User user = mockUserWithAge(LocalDate.now(), 15);
        assertFalse(Service.isPayableUser(user));
    }

    @Test
    void should_fail_is_payable_user_check_if_user_is_null() {
        assertThrows(NullPointerException.class, () -> Service.isPayableUser(null));
    }

    @Test
    void should_fail_is_payable_user_check_if_user_doesnt_have_birthday() {
        assertThrows(IllegalArgumentException.class, () -> Service.isPayableUser(Mockito.mock(User.class)));
    }


    private User mockUserWithAge(LocalDate date, int age) {
        User mock = Mockito.mock(User.class);
        LocalDate birthday = date.minusYears(age);
        when(mock.getBirthday()).thenReturn(birthday);
        return mock;
    }

    private Service createServiceWithUsers(List<User> users) {
        return new AbstractServiceImpl() {

            @Override
            public List<User> getAllUsers() {
                return users;
            }
        };
    }

    private abstract static class AbstractServiceImpl implements Service {

        @Override
        public void subscribe(BankCard bankCard) {
        }

        @Override
        public Subscription getSubscriptionByBankCardNumber(String cardNumber) {
            return null;
        }

        @Override
        public List<User> getAllUsers() {
            return List.of();
        }

        @Override
        public List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> condition) {
            return List.of();
        }
    }

}