/*
 * Copyright (C) 2022 Lingu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.lingu.imagehosting.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import space.lingu.imagehosting.properties.WebUrlsProperties;
import space.lingu.imagehosting.service.user.UserDetailsServiceImpl;

/**
 * @author RollW
 */
@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
// @EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfiguration {
    private final UserDetailsServiceImpl userDetailsService;

    private final WebUrlsProperties urlsProperties;


    public WebSecurityConfiguration(UserDetailsServiceImpl userDetailsService,
                                    WebUrlsProperties urlsProperties) {
        this.userDetailsService = userDetailsService;
        this.urlsProperties = urlsProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.formLogin()
                .permitAll()
                .loginPage(urlsProperties.getFrontendUrl() + "/login")
                .loginProcessingUrl("/api/user/login");
        security.logout()
                .permitAll()
                .logoutUrl("/api/user/logout")
                .deleteCookies("lingu_image_hosting");
        security.authorizeRequests()
                .antMatchers("/api/user/login/**").permitAll()
                .antMatchers("/api/user/login").permitAll()
                .antMatchers("/api/user/test").permitAll()
                .antMatchers("/anonymous*").anonymous()
                .antMatchers("/image/**").permitAll()
                .antMatchers("/api/user/register/**").permitAll()
                .antMatchers("/api/common/**").permitAll()
                .antMatchers("/api/user/message/**").permitAll()
                .antMatchers("/api/user/logout/").permitAll()
                .antMatchers("/api/user/admin/**").hasRole("ADMIN");
        security
                .userDetailsService(userDetailsService);

//                .and()
//                    .rememberMe()
//                    .key("Remember@ Don;t 1eAve me-")
        security
                .csrf().disable();

        security
                .authorizeRequests()
                .anyRequest().authenticated();
        return security.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/css/**")
                .antMatchers("/404.html")
                .antMatchers("/500.html")
                .antMatchers("/error.html")
                .antMatchers("/html/**")
                .antMatchers("/js/**")
                .antMatchers("/api/user/login");// 特别放行
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
