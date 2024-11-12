module jmp.cloud.bank.impl {
    requires transitive jmp.bank.api;
    requires jmp.dto;

    provides com.epam.jmp.bank.Bank with com.epam.jmp.impl.bank.BankImpl;
    exports com.epam.jmp.impl.bank;
}
