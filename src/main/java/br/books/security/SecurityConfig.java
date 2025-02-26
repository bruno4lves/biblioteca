package br.books.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Autowired
    private CustomUserDetailsService userDetailsService;
	
	@Autowired
    private LoginSucesso loginSucesso;

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userDetailsService);

		return provider;
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/login", "/usuario/cadastro").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/biblio/**").hasAuthority("ROLE_BIBLIO")
                .requestMatchers("/biblio/**").hasAuthority("ROLE_BIBLIO")
                .anyRequest().authenticated()
                
    		    //hasAnyAuthority
    			//hasAuthority
    			//hasRole
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()

//              .formLogin(form -> 
//				 form.successHandler(loginSucesso)
//				.loginPage("/login").permitAll()
//              .loginPage("/home").permitAll()
        		)

            .logout(logout -> logout.permitAll());

        return http.build();
    }
}