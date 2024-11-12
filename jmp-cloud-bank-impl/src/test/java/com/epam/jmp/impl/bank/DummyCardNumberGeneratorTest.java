package com.epam.jmp.impl.bank;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DummyCardNumberGeneratorTest {

    DummyCardNumberGenerator sut = new DummyCardNumberGenerator();

    @Test
    void should_generate_card_number() {
        int length = 16;
        String res = sut.generate(length);
        assertNotNull(res);
        assertEquals(length, res.length());
    }

}