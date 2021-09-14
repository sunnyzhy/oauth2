package org.springframework.security.oauth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsUtils;

import java.util.Arrays;

/**
 * @author zhouyi
 * @date 2021/1/12 15:07
 * 认证服务配置
 */
@EnableWebSecurity
@Configuration
public class AuthenticationServerConfig extends WebSecurityConfigurerAdapter {
    @Value("${login.page}")
    private String loginPage;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthenticationProviderImpl authenticationProvider;
    @Autowired
    private ForwardAuthenticationSuccessHandler forwardAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandlerImpl authenticationFailureHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 允许 cors(跨站资源共享) 和 csrf(跨站请求伪造)
                .cors().and().csrf().disable()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/oauth2/keys").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                // 自定义登录页 url,默认为 /login
//                .loginPage(loginPage)
//                .usernameParameter("username").passwordParameter("password")
                // 自定义后台处理登录请求的接口，默认是 loginPage 的 url
                // 当设置了 loginProcessingUrl，例如 loginProcessingUrl("/auth/login")，并不是真的需要一个 /auth/login 接口，而只是需要你将自定义的前端登录页请求的后台 url 改成 /auth/login 即可
                // 过滤器 UsernamePasswordAuthenticationFilter 仅由 loginProcessingUrl 中指定的 url 调用，并将使用 username-parameter 和 password-parameter 的值作为请求中的用户名和密码
//                .loginProcessingUrl("/login")
//        .successForwardUrl("http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://20.0.0.106")
                // 认证成功处理器
//                .successHandler(authenticationSuccessHandler)
                .successHandler(forwardAuthenticationSuccessHandler)
                // 认证失败处理器
                .failureHandler(authenticationFailureHandler).permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 注册自定义的userDetailsService
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        // 注册自定义认证
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        // 指定authenticationManager的成员是自定义AuthenticationProviderImpl的，否则会因为用户名、密码错误而再调用默认的DaoAuthenticationProvider，从而抛出"UserDetailsService returned null, which is an interface contract violation"
        ProviderManager authenticationManager = new ProviderManager(Arrays.asList(authenticationProvider));
        //不擦除认证密码，擦除会导致TokenBasedRememberMeServices因为找不到Credentials再调用UserDetailsService而抛出UsernameNotFoundException
//        authenticationManager.setEraseCredentialsAfterAuthentication(false);
        return authenticationManager;
    }
}
