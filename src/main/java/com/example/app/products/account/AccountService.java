package com.example.app.products.account;

import java.util.Collections;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.app.products.queries.AccountQuery;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountService implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct	
	protected void initialize() {
		save(new Account("boripe2000@gmail.com", "demo", "ROLE_USER"));
		save(new Account("boripe2006@gmail.com", "admin", "ROLE_ADMIN"));	
	}

	public Account save(Account account) {
		if(AccountQuery.INSTANCE.exists(account.getEmail())) {
			return account;
		}
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		Long id = AccountQuery.INSTANCE.findId();
		if (id != null) {
			account.setId(id+1);
		} else {
			account.setId(new Long(1));
		}
		
		account = AccountQuery.INSTANCE.saveAccount(account);
		return account;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = AccountQuery.INSTANCE.findAccountByEmail(username);
		if(account == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return createUser(account);
	}
	
	public void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}
	
	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null, Collections.singleton(createAuthority(account)));		
	}
	
	private User createUser(Account account) {
		return new User(account.getEmail(), account.getPassword(), Collections.singleton(createAuthority(account)));
	}

	private GrantedAuthority createAuthority(Account account) {
		return new SimpleGrantedAuthority(account.getRole());
	}

}
