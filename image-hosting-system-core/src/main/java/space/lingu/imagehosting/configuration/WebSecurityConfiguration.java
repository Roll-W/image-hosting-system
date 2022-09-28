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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import space.lingu.imagehosting.service.user.UserService;

/**
 * @author RollW
 */
@Configuration
public class WebSecurityConfiguration {
    public WebSecurityConfiguration(@Autowired UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.csrf().disable()
                .userDetailsService(userService)
//                .authorizeRequests()
//                .antMatchers("/anonymous*").anonymous()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login.html")
//                .loginProcessingUrl("/api/user/login")
//                .failureUrl("/login.html?error=A0001")
//                .and()
//                .logout().deleteCookies("")
//                .and().rememberMe()
//                .key("Remember@ Don;t 1eAve me-")
        ;

        return security.build();
    }

}
