package com.epam.jmp.impl.bank;

import com.epam.jmp.dto.*;
import com.epam.jmp.bank.Bank;

import java.util.Objects;

public class BankImpl implements Bank {

    private CardNumberGenerator cardNumberGenerator;

    public BankImpl() {
        this(new DummyCardNumberGenerator());
    }

    public BankImpl(CardNumberGenerator cardNumberGenerator) {
        this.cardNumberGenerator = cardNumberGenerator;
    }

    @Override
    public BankCard createBankCard(User user, BankCardType cardType) {
        Objects.requireNonNull(user, "user is null");
        Objects.requireNonNull(cardType, "cardType is null");
        var cardNumberLength = 16;
        return switch (cardType) {
            case DEBIT -> new DebitBankCard(cardNumberGenerator.generate(cardNumberLength), user, 0d);
            case CREDIT -> new CreditBankCard(cardNumberGenerator.generate(cardNumberLength), user, 0d);
        };
    }

    public CardNumberGenerator getCardNumberGenerator() {
        return cardNumberGenerator;
    }

    public void setCardNumberGenerator(CardNumberGenerator cardNumberGenerator) {
        this.cardNumberGenerator = cardNumberGenerator;
    }
}
