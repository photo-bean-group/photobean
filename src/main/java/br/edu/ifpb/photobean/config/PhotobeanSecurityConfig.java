package br.edu.ifpb.photobean.config;

import javax.sql.DataSource;

import br.edu.ifpb.photobean.model.Photographer;
import br.edu.ifpb.photobean.repository.PhotographerRepository;
import br.edu.ifpb.photobean.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class PhotobeanSecurityConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/img/**", "/home/**", "/auth/signup/**").permitAll()
                        .requestMatchers("/photographers/list/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll())

                .logout((logout) -> {
                    logout.logoutUrl("/auth/logout");
                    logout.logoutSuccessUrl("/home");
                    logout.invalidateHttpSession(true);
                    logout.clearAuthentication(true);
                    logout.deleteCookies("JSESSIONID");
                });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails dev = User.withUsername("dev").password(passwordEncoder().encode("dev")).roles("CLIENTE")
                .build();
        UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("123"))
                .roles("CLIENTE", "ADMIN").build();

        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        if (!users.userExists(dev.getUsername())) {
            users.createUser(dev);
            users.createUser(admin);
            createPhotographer(dev.getUsername());
            createPhotographer(admin.getUsername());
        }
        return users;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotographerRepository photographerRepository;

    private void createPhotographer(String username) {
        Optional<br.edu.ifpb.photobean.model.User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            br.edu.ifpb.photobean.model.User user = userOptional.get();
            Photographer photographer = new Photographer();
            photographer.setUser(user);
            photographer.setName(username);
            photographer.setEmail(username + "@email.com");
            photographerRepository.save(photographer);
        }
    }
}
