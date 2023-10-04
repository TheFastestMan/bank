package ru.rail.bank.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.rail.bank.models.BankAccount;
import ru.rail.bank.services.BankAccountService;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping
    public BankAccount createAccount(@RequestBody BankAccount account) {
        return bankAccountService.createAccount(account.getName(), account.getPin());
    }

    @PostMapping("/{accountNumber}/deposit")
    public BankAccount deposit(@PathVariable Long accountNumber, @RequestBody Map<String, Object> payload) {
        return bankAccountService.deposit(accountNumber, Double.valueOf(payload.get("amount").toString()),
                payload.get("pin").toString());
    }

    @PostMapping("/{accountNumber}/withdraw")
    public BankAccount withdraw(@PathVariable Long accountNumber, @RequestBody Map<String, Object> payload) {
        return bankAccountService.withdraw(accountNumber, Double.valueOf(payload.get("amount").toString()),
                payload.get("pin").toString());
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody Map<String, Object> payload) {
        Long fromAccount = Long.valueOf(payload.get("fromAccount").toString());
        Long toAccount = Long.valueOf(payload.get("toAccount").toString());
        Double amount = Double.valueOf(payload.get("amount").toString());
        String pin = payload.get("pin").toString();
        bankAccountService.transfer(fromAccount, toAccount, amount, pin);
    }

    @GetMapping
    public List<BankAccount> getAllAccounts() {
        return bankAccountService.getAllAccounts();
    }
}
