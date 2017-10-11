package com.ehs.gateway;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ehs.gateway.config.CorsFilter;
import com.ehs.gateway.config.CustomHttpClientRibbonCommandFactory;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@SpringBootApplication
@EnableResourceServer
@EnableZuulProxy
public class ZuulApplication {
	
	//测试更新自动构建
	@Autowired(required = false)
    private Set<ZuulFallbackProvider> zuulFallbackProviders = Collections.emptySet();

    @Bean
    @ConditionalOnMissingBean
    public RibbonCommandFactory<?> ribbonCommandFactory(
            SpringClientFactory clientFactory, ZuulProperties zuulProperties) {
        return new CustomHttpClientRibbonCommandFactory(clientFactory, zuulProperties, zuulFallbackProviders);
    }
    
  public static void main(String[] args) {
    SpringApplication.run(ZuulApplication.class, args);
  }
  @Bean
  LoadBalancerInterceptor loadBalancerInterceptor(LoadBalancerClient loadBalance) {
      return new LoadBalancerInterceptor(loadBalance);
  }
  
  private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		return corsConfiguration;
	}
  
  /**
	 * 跨域过滤器
	 * @return
	 */
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig()); // 4
		return new CorsFilter(source);
	}
}
