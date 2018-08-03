package com.example.app.products.account;

import static java.util.function.Predicate.isEqual;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.app.products.queries.AccountQuery;

//@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@InjectMocks
	private AccountService accountService = new AccountService();

	//@Mock
	private PasswordEncoder passwordEncoder;

	//@Rule
	public ExpectedException thrown = ExpectedException.none();

	//@Test
	public void shouldInitializeWithTwoDemoUsers() {
		// act
		accountService.initialize();
	}

	//@Test
	public void shouldThrowExceptionWhenUserNotFound() {
		// arrange
		thrown.expect(UsernameNotFoundException.class);
		thrown.expectMessage("user not found");

		when(AccountQuery.INSTANCE.findAccountByEmail("boripe2000@gmail.com")).thenReturn(null);
		// act
		accountService.loadUserByUsername("boripe2000@gmail.com");
	}

	//@Test
	public void shouldReturnUserDetails() {
		// arrange
		Account demoUser = new Account("boripe2000@gmail.com", "demo", "ROLE_USER");
		when(AccountQuery.INSTANCE.findAccountByEmail("boripe2000@gmail.com")).thenReturn(demoUser);

		// act
		UserDetails userDetails = accountService.loadUserByUsername("boripe2000@gmail.com");

		// assert
		assertThat(demoUser.getEmail()).isEqualTo(userDetails.getUsername());
		assertThat(demoUser.getPassword()).isEqualTo(userDetails.getPassword());
		assertThat(hasAuthority(userDetails, demoUser.getRole())).isTrue();
	}

	private boolean hasAuthority(UserDetails userDetails, String role) {
		return userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(isEqual(role));
	}
}
