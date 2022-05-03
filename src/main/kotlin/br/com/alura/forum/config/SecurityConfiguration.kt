package br.com.alura.forum.config

import br.com.alura.forum.security.JWTAuthenticatonFilter
import br.com.alura.forum.security.JWTLoginFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val userDetailsService: UserDetailsService,
    private val jwtUtil: JWTUtil
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        //se o http n for nulo
        http?.csrf()?.disable()?.
            //autoriza a requisição
        authorizeRequests()?.antMatchers("/topicos")?.hasAuthority("LEITURA_ESCRITA")?.
            //qualquer requisição
        antMatchers(HttpMethod.POST, "/login")?.permitAll()?.anyRequest()?.
            //tem que ser autenticada
        authenticated()?.
            //e não salve a sessao
        and()
        http?.addFilterBefore(
            JWTLoginFilter(authManager = authenticationManager(), jwtUtil = jwtUtil),
            UsernamePasswordAuthenticationFilter().javaClass
        )
        http?.addFilterBefore(
            JWTAuthenticatonFilter(jwtUtil = jwtUtil),
            UsernamePasswordAuthenticationFilter().javaClass
        )
        http?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(bCryptPasswordEncoder())
    }
}