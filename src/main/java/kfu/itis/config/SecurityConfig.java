package kfu.itis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //todo: включить на последнем этапе
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                                        "/",
                                        "/register",
                                        "/masters",
                                        "/masters/**",
                                        "/specializations/**",
                                        "/css/**",
                                        "/js/**",
                                        "/images/**",
                                        "/webjars/**",
                                        "/error"
                                ).permitAll()

                                .requestMatchers(
                                        "/orders/new",
                                        "/orders/my",
                                        "/orders/{id}",
                                        "/favorites/**",
                                        "/reviews/create/**"
                                ).hasRole("CUSTOMER")

                                .requestMatchers(
                                        "/orders/available",
                                        "/orders/assigned",
                                        "/orders/{id}/accept",
                                        "/orders/{id}/start",
                                        "/orders/{id}/complete"
                                ).hasRole("MASTER")

                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                .anyRequest().authenticated()
                )

                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/", true)
                                .permitAll()
                )

                .logout(
                        logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                )

                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .maximumSessions(1)
                                .expiredUrl("/login?expired")
                );

        return http.build();

    }
}
