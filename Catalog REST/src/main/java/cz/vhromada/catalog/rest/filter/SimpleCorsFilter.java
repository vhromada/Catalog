package cz.vhromada.catalog.rest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/**
 * <p>Filter allowing Cross-Origin Resource Sharing (CORS)</p>
 * <p>http://spring.io/guides/gs/rest-service-cors/</p>
 * <p>When frontend will run on the same port at rest, this filter won't be needed.</p>
 *
 * @author Vladimir Hromada
 */
@Component
public class SimpleCorsFilter implements Filter {

	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
		final HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:8000"); //allow calls from the same domain or that specified
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		chain.doFilter(req, res);
	}

	public void init(final FilterConfig filterConfig) {
	}

	public void destroy() {
	}

}
