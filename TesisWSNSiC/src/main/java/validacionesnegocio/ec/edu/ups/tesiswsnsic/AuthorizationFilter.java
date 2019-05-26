package validacionesnegocio.ec.edu.ups.tesiswsnsic;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthorizationFilter implements Filter {

	public AuthorizationFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {

			HttpServletRequest reqt = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			HttpSession ses = reqt.getSession(false);

			String reqURI = reqt.getRequestURI();
			if (reqURI.indexOf("/faces/homepage/home.xhtml") >= 0
			// || (ses == null && ses.getAttribute("username") == null)
			// || reqURI.indexOf("/public/") >= 0
			// || reqURI.contains("javax.faces.resource")
			) {
				chain.doFilter(request, response);

			} else if (reqURI.indexOf("/faces/login/Registrar.xhtml") >= 0
			// || (ses == null && ses.getAttribute("username") == null)
			// || reqURI.indexOf("/public/") >= 0
			// || reqURI.contains("javax.faces.resource")
			) {
				chain.doFilter(request, response);

			} else if (reqURI.indexOf("/faces/blog/blog.xhtml") >= 0
			// || (ses == null && ses.getAttribute("username") == null)
			// || reqURI.indexOf("/public/") >= 0
			// || reqURI.contains("javax.faces.resource")
			) {
				chain.doFilter(request, response);
			} else if (reqURI.indexOf("/faces/blog/blogDetalle.xhtml") >= 0
			// || (ses == null && ses.getAttribute("username") == null)
			// || reqURI.indexOf("/public/") >= 0
			// || reqURI.contains("javax.faces.resource")
			) {
				chain.doFilter(request, response);
			}  else if (reqURI.indexOf("/faces/homepage/about.xhtml") >= 0
					// || (ses == null && ses.getAttribute("username") == null)
					// || reqURI.indexOf("/public/") >= 0
					// || reqURI.contains("javax.faces.resource")
					) {
				chain.doFilter(request, response);
			} else if (reqURI.indexOf("/faces/homepage/services.xhtml") >= 0
					// || (ses == null && ses.getAttribute("username") == null)
					// || reqURI.indexOf("/public/") >= 0
					// || reqURI.contains("javax.faces.resource")
					) {
				chain.doFilter(request, response);
			} else if (reqURI.indexOf("/faces/homepage/blogs.xhtml") >= 0
					// || (ses == null && ses.getAttribute("username") == null)
					// || reqURI.indexOf("/public/") >= 0
					// || reqURI.contains("javax.faces.resource")
					) {
				chain.doFilter(request, response);
			}

			else if (reqURI.indexOf("/faces/login/Login.xhtml") >= 0
					|| (ses != null && ses.getAttribute("username") != null) || reqURI.indexOf("/public/") >= 0
					|| reqURI.contains("javax.faces.resource"))
				chain.doFilter(request, response);
			else
				resp.sendRedirect(reqt.getContextPath() + "/faces/login/Login.xhtml");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void destroy() {

	}
}
