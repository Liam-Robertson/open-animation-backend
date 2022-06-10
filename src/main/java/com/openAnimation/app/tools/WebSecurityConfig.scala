//package com.openAnimation.tools
//
//import org.springframework.context.annotation.{Bean, Configuration}
//import org.springframework.security.config.annotation.web.builders.{HttpSecurity, WebSecurity}
//import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter}
//import org.springframework.security.web.firewall.{DefaultHttpFirewall, HttpFirewall, HttpStatusRequestRejectedHandler, RequestRejectedHandler, StrictHttpFirewall};
//
//@EnableWebSecurity
//@Configuration
//  class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//  @Override
//  override def configure(http: HttpSecurity) {
//
//        http
//          .authorizeRequests()
//          .anyRequest()
//          .authenticated()
//          .and()
//          .httpBasic();
//        http.cors();
//  }
//
//  @Bean
//  def allowUrlEncodedSlashHttpFirewall(): HttpFirewall = {
//    //    val firewall: StrictHttpFirewall = new StrictHttpFirewall()
//    //    firewall.setAllowUrlEncodedSlash(true)
//    //    firewall
//        new DefaultHttpFirewall
//  }
//
//  @Override
//  override def configure(web: WebSecurity) {
//    super.configure(web);
//    web.httpFirewall(allowUrlEncodedSlashHttpFirewall())
//  }
//
//  @Bean
//  def requestRejectedHandler(): RequestRejectedHandler = {
//    new HttpStatusRequestRejectedHandler()
//  }
//
//}