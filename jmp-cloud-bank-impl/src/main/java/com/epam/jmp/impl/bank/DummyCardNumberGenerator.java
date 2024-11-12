package com.epam.jmp.impl.bank;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DummyCardNumberGenerator implements CardNumberGenerator {

    private final Random random = new Random(System.currentTimeMillis());

    public String generate(int length) {
        return Stream.generate(() -> "" + random.nextInt(10))
                .limit(length).collect(Collectors.joining());
    }
}
