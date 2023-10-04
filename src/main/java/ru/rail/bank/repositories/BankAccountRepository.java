package ru.rail.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rail.bank.models.BankAccount;


@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
