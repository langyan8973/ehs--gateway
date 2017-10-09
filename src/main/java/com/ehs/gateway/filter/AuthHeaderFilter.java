package com.ehs.gateway.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class AuthHeaderFilter extends ZuulFilter {

	@Override
	public Object run() {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.addZuulRequestHeader("X-AUTH-ID",authentication.getPrincipal().toString());
        return null;
	}

	@Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

}
