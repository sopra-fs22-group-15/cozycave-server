package ch.uzh.ifi.fs22.sel.group15.cozycave.server.config;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationEntryPoint;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final int PASSWORD_MIN_LENGTH = 8;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/v1/auth/login",
                        "/v1/auth/login/",
                        "/v1/auth/register",
                        "/v1/auth/register/",
                        "/v1/listings",
                        "/v1/listings/",
                        "/v1/listings/{id}",
                        "/v1/listings/{id}/",
                        "/v1/pictures",
                        "/v1/pictures/",
                        "/v1/pictures/{id}",
                        "/v1/pictures/{id}/",
                        "/v1/pictures/{id}/view",
                        "/v1/pictures/{id}/view/",
                        "/v1/pictures/listings/{listingId}/floorplan",
                        "/v1/pictures/listings/{listingId}/floorplan/",
                        "/v1/pictures/listings/{listingId}",
                        "/v1/pictures/listings/{listingId}/",
                    "/v1/pictures/listings",
                    "/v1/pictures/listings/",
                    "/v1/pictures/users",
                    "/v1/pictures/users/",
                    "/v1/pictures/users/{userId}",
                    "/v1/pictures/users/{userId}/",
                    "/v1/listings/{listingId}",
                    "/v1/listings/{listingId}/",
                    "/v1/gathertogether/**"
                ).permitAll()
            .anyRequest().authenticated()
                .and()
                .formLogin().disable();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
