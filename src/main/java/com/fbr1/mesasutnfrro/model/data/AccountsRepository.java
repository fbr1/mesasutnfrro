package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.Account;
import org.springframework.data.repository.Repository;


public interface AccountsRepository extends Repository<Account, Long> {

    Account save(Account account);

    Account findByName(String name);
}
