package com.openAnimation.tools

import org.apache.catalina.Context
import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.descriptor.web.{SecurityCollection, SecurityConstraint}
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean

class SSLConfig {

    @Bean
    def servletContainer(): ServletWebServerFactory = {
      val tomcat: TomcatServletWebServerFactory = new TomcatServletWebServerFactory() {

        @Override
        override def postProcessContext(context: Context): Unit = {
          val securityConstraint = new SecurityConstraint();
          securityConstraint.setUserConstraint("CONFIDENTIAL");
          val collection = new SecurityCollection();
          collection.addPattern("/*");
          securityConstraint.addCollection(collection);
          context.addConstraint(securityConstraint);
        }
      }
      tomcat.addAdditionalTomcatConnectors(getHttpConnector)
      tomcat
    }

    def getHttpConnector: Connector = {
      val connector: Connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL)
      connector.setScheme("http")
      connector.setPort(8080)
      connector.setSecure(false)
      connector.setRedirectPort(8443)
      connector
    }
}
