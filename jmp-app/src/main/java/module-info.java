import com.epam.jmp.bank.Bank;

module jmp.app {
    requires jmp.service.api;
    uses com.epam.jmp.service.Service;

    requires jmp.bank.api;
    requires jmp.dto;
    uses com.epam.jmp.bank.Bank;
}