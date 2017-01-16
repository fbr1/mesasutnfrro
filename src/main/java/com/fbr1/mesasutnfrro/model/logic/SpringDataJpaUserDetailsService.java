package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.data.AccountsRepository;
import com.fbr1.mesasutnfrro.model.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class SpringDataJpaUserDetailsService implements UserDetailsService{



    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Account account = this.repository.findByName(name);
        return new org.springframework.security.core.userdetails.User(account.getName(), account.getPassword(),
                AuthorityUtils.createAuthorityList(account.getRoles()));
    }

    @Autowired
    private AccountsRepository repository;
}
