package com.openAnimation.tools

import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter};

@EnableWebSecurity
@Configuration
  class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  override def configure(http: HttpSecurity) {

        http
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic();
  }


}