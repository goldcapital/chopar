package com.example.chopar_1.config;

import com.example.chopar_1.util.MDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity
@Configuration
    public class SpringSecurityConfig {
    public static final String [] AUTH_WHITELIST={
            "/v2/api-docs" +
                    "/v3/$%7Bserver.url",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/auth/**"

    };
        @Autowired
        private JwtTokenFilter jwtTokenFilter;
        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public AuthenticationProvider authenticationProvider() {
       /* // authentication
        String password = UUID.randomUUID().toString();
        System.out.println("User Pasword mazgi: " + password);

        UserDetails user = User.builder()
                .username("user")
                .password("{noop}" + password)
                .roles("ADMIN")
                .build();

        final DaoAuthenticationProvider
         authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(user));
        return authenticationProvider;*/

            final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService);
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return authenticationProvider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            // authorization
      /*  http.authorizeHttpRequests()
                .anyRequest()
                .authenticated();*/
            http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                authorizationManagerRequestMatcherRegistry
                        .requestMatchers(AUTH_WHITELIST).permitAll()

                      //  .requestMatchers("/region/adm/**").hasRole("ADMIN")//faqat admen kirishi mumkin
                        //hasRole faqat bita role ga ruxsat berish uchun ishlatamiz
                        .requestMatchers("/article/publish").hasAnyRole("ADMIN", "PUBLISHER")// admen va punlisher kirishi mukin
                        //hasAnyRole ber nichta role ga ruhsat berush uchun ishlatamiz
                        .requestMatchers("/profile/adm", "/profile/adm/*").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated();//qolgan metodlar bu yerga tushadi rolini farqi yu
                // faqat ruyhatan utgan bulda buldi ular kirishga ruhsati bor
            });
            // http.httpBasic(Customizer.withDefaults());//Basic ga ruxsat beradi yani password va login kiladi basic da
            http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);//
            http.csrf(AbstractHttpConfigurer::disable);//defol holda security
            http.cors(AbstractHttpConfigurer::disable);//faqat brazerdan kirishga ruhsat beradi boshqa joydan
            // murojat qilishga ruhsat bermaydi bu ikkalasi usha cheklovni olib tashlaydi


            return http.build();
        }

        public PasswordEncoder passwordEncoder() {
            return new PasswordEncoder() {
                @Override
                public String encode(CharSequence rawPassword) {
                    return rawPassword.toString();
                }

                @Override
                public boolean matches(CharSequence rawPassword, String encodedPassword) {
                    return MDUtil.encode(rawPassword.toString()).equals(encodedPassword);
                }
            };
        }
    }


