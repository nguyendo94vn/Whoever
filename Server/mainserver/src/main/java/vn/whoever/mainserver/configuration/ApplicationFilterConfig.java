package vn.whoever.mainserver.configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ApplicationFilterConfig implements Filter {

	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		System.out.println("Filtertttttttt!!!");
		try {
			filterChain.doFilter(req, res);
		} catch(Exception e) {
			
		}
	}

	public void init(FilterConfig config) throws ServletException {
		
	}

}