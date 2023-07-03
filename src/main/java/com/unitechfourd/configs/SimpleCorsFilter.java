package com.unitechfourd.configs;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {
	private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "allow-origin";
	private static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
	private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	private static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
	private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	private static final String ORIGIN = "origin";
	private static final String METHOD_OPTIONS = "OPTIONS";
	private static final String STATUS_OK = "OK";
	private static final int MAX_AGE = 3600;


	@Value("${api.cors.allow}")
	private String ALLOW;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		// Converte os objetos de solicitação e resposta para seus tipos específicos
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		// Obtém a origem da solicitação
		final String origin = request.getHeader(ORIGIN);

		// Configura os cabeçalhos de resposta para permitir o CORS
		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, isEmpty(origin) ? ALLOW : origin);
		response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
		response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, DELETE, PUT, PATCH");
		response.setHeader(ACCESS_CONTROL_MAX_AGE, Integer.toString(MAX_AGE));
		response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, "X-Requested-With, Content-Type, Authorization, x-csrf-token, X-XSRF-TOKEN");
		response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

		if (METHOD_OPTIONS.equalsIgnoreCase(request.getMethod())) {
			// Se a solicitação for do tipo OPTIONS, envia uma resposta com o status OK
			response.getWriter().print(STATUS_OK);
			response.getWriter().flush();
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			// Chama o próximo filtro na cadeia (ou o servlet correspondente)
			chain.doFilter(req, res);
		}
	}

	public static boolean isEmpty(String string){
		return string==null || string.isEmpty();
	}
}
