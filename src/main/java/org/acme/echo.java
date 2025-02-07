package org.acme;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController

public class echo {

	private static final Logger LOG = LoggerFactory.getLogger(echo.class); 

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(path = "/**", consumes = MediaType.ALL_VALUE, produces = 
		MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET, RequestMethod.POST,
		RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})

	public ResponseEntity<JsonPayload> echoBack(@RequestBody(required = false) byte[] rawBody) {

		final Map<String, String> headers  = Collections.list(request.getHeaderNames()).stream()
			.collect(Collectors.toMap(Function.identity(), request::getHeader));

		final JsonPayload response = new JsonPayload();
		response.set(JsonPayload.PROTOCOL, request.getProtocol());
		response.set(JsonPayload.METHOD, request.getMethod());
		response.set(JsonPayload.HEADERS, headers);
		response.set(JsonPayload.COOKIES, request.getCookies());
		response.set(JsonPayload.PARAMETERS, request.getParameterMap());
		response.set(JsonPayload.PATH, request.getServletPath());
		response.set(JsonPayload.BODY, rawBody != null ? Base64.getEncoder().encodeToString(rawBody) : null);
		response.set(JsonPayload.SERVER, request.getServerName() + ":" + request.getServerPort());

		LOG.info("Server "+ request.getServerName() + ":" + request.getServerPort());
		LOG.info("Path "+ request.getServletPath());
        LOG.info("Method "+ request.getMethod());
        LOG.info("Headers "+ headers);
		LOG.info("REQUEST: {}", request.getParameterMap().toString());

		//return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}
	
}