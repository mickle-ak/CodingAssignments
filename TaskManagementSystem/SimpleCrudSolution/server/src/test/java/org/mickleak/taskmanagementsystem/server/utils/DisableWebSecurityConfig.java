package org.mickleak.taskmanagementsystem.server.utils;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.mickleak.taskmanagementsystem.server.configuration.WebSecurityConfig.TMS_SECURITY_FILTER_CHAIN_BEAN_NAME;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@TestConfiguration
public class DisableWebSecurityConfig {

	@Bean
	public SecurityFilterChain disableSecurityFilterChain( HttpSecurity http ) throws Exception {
		return http
			.authorizeHttpRequests( authorizeRequests -> authorizeRequests.anyRequest().permitAll() )
			.cors( Customizer.withDefaults() )
			.csrf( AbstractHttpConfigurer::disable )
			.formLogin( AbstractHttpConfigurer::disable )
			.httpBasic( AbstractHttpConfigurer::disable )
			.sessionManagement( sessionManagement -> sessionManagement.sessionCreationPolicy( STATELESS ) )
			.build();
	}


	/**
	 * To disable the security filter chain defined in the "main" code and
	 * to allow all requests without any restrictions.
	 */
	@Bean
	public BeanFactoryPostProcessor disableStandardSecurityBeanFactoryPostProcessor() {
		return beanFactory -> {
			BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
			if( beanDefinitionRegistry.containsBeanDefinition( TMS_SECURITY_FILTER_CHAIN_BEAN_NAME ) ) {
				beanDefinitionRegistry.removeBeanDefinition( TMS_SECURITY_FILTER_CHAIN_BEAN_NAME );
			}
		};
	}
}
