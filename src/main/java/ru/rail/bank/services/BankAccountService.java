package ru.rail.bank.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rail.bank.models.BankAccount;
import ru.rail.bank.repositories.BankAccountRepository;


import java.util.List;

@Service
@Slf4j
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public BankAccount createAccount(String name, String pin) {
        BankAccount account = new BankAccount();
        account.setName(name);
        account.setPin(pin);
        return bankAccountRepository.save(account);
    }

    @Transactional
    public BankAccount deposit(Long accountNumber, Double amount, String pin) {
        log.info("Attempting to deposit {} to account {}", amount, accountNumber);
        BankAccount account = bankAccountRepository.findById(accountNumber).orElseThrow();


        if (pin != null && !account.getPin().equals(pin)) {
            log.error("Incorrect PIN provided for account {}", accountNumber);
            throw new RuntimeException("Incorrect PIN!");
        }

        account.setBalance(account.getBalance() + amount);
        log.info("Deposited {} to account {}. New balance: {}", amount, accountNumber, account.getBalance());
        return bankAccountRepository.save(account);
    }

    @Transactional
    public BankAccount withdraw(Long accountNumber, Double amount, String pin) {
        BankAccount account = bankAccountRepository.findById(accountNumber).orElseThrow();
        log.debug("Checking PIN. Entered: {}, Stored: {}", pin, account.getPin());
        if (!account.getPin().equals(pin)) {
            throw new RuntimeException("Incorrect PIN!");
        }
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds!");
        }
        account.setBalance(account.getBalance() - amount);
        return bankAccountRepository.save(account);
    }

    @Transactional
    public void transfer(Long fromAccountNumber, Long toAccountNumber, Double amount, String pin) {
        log.info("Attempting to transfer {} from account {} to account {}", amount, fromAccountNumber, toAccountNumber);
        withdraw(fromAccountNumber, amount, pin);
        deposit(toAccountNumber, amount, null);
        log.info("Successfully transferred {} from account {} to account {}", amount, fromAccountNumber, toAccountNumber);
    }


    public List<BankAccount> getAllAccounts() {
        return bankAccountRepository.findAll();
    }
}
