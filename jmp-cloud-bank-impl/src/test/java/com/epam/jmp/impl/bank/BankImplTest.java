package com.epam.jmp.impl.bank;

import com.epam.jmp.dto.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class BankImplTest {

    @Mock
    CardNumberGenerator cardNumberGenerator;

    @InjectMocks
    BankImpl sut;

    @Test
    void should_create_credit_bank_card() {
        // given
        String cardNo = "card_number";
        Mockito.when(cardNumberGenerator.generate(anyInt())).thenReturn(cardNo);
        User user = Mockito.mock(User.class);
        // when
        BankCard actual = sut.createBankCard(user, BankCardType.CREDIT);
        // then
        assertInstanceOf(CreditBankCard.class, actual);
        assertEquals(cardNo, actual.getNumber());
        assertEquals(user, actual.getUser());
    }

    @Test
    void should_create_debit_bank_card() {
        // given
        String cardNo = "card_number";
        Mockito.when(cardNumberGenerator.generate(anyInt())).thenReturn(cardNo);
        User user = Mockito.mock(User.class);
        // when
        BankCard actual = sut.createBankCard(user, BankCardType.DEBIT);
        // then
        assertInstanceOf(DebitBankCard.class, actual);
        assertEquals(cardNo, actual.getNumber());
        assertEquals(user, actual.getUser());
    }

    @Test
    void should_fail_if_card_type_is_null() {
        assertThrows(NullPointerException.class, () -> sut.createBankCard(Mockito.mock(User.class), null));
        Mockito.verifyNoInteractions(cardNumberGenerator);
    }

    @Test
    void should_fail_if_user_is_null() {
        assertThrows(NullPointerException.class, () -> sut.createBankCard(null, BankCardType.CREDIT));
        Mockito.verifyNoInteractions(cardNumberGenerator);
    }

}