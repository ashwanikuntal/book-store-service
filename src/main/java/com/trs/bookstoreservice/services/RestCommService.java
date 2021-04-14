package com.trs.bookstoreservice.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trs.bookstoreservice.exception.BadRequestException;

public class RestCommService {
	
	@Autowired
	private RestTemplate restTemplate;

	public JsonNode executeJson(HttpMethod method, String url, String token, Map<String, Object> requestBody,
			Map<String, String> additionalHeaders) {
		
		HttpHeaders headers = new HttpHeaders();
		
		String json = null;
		
		if(requestBody != null) {
			headers.set("Content-Type", "application/json");
			ObjectMapper mapper = new ObjectMapper();
			try {
				json = mapper.writeValueAsString(requestBody);
			} catch(Exception e) {
				throw new BadRequestException("Invalid request body");
				// Todo: Replace badRequestException with ServerException
			}	
		}
		
		try {
			headers.set("Authorization", token);
			if(additionalHeaders != null) {
				for(Map.Entry<String, String> entry : additionalHeaders.entrySet()) {
					headers.add(entry.getKey(), entry.getValue());
				}
			}
			
			HttpEntity<String> entity = new HttpEntity<String>(json, headers);
			ResponseEntity<String> resp = this.restTemplate.exchange(url, method, entity, String.class);
			
			if(!resp.getStatusCode().is2xxSuccessful()) {
				throw new BadRequestException("Call failed");
				//Todo: Replace badRequestException with ServerException
			}
			return (resp.getBody() == null) ? null : new ObjectMapper().readTree(resp.getBody());
		} catch (Exception e) {
			throw new BadRequestException("Error calling url");
			// Todo: Replace badRequestException with ServerException
		}
	}
}
