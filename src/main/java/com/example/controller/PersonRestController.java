/**
 * 
 */
package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.client.SpringRestClient;
import com.example.model.AuthTokenInfo;

/**
 * @author Ittipol
 *
 */
@RestController
@RequestMapping("/")
public class PersonRestController {
	
	@RequestMapping("/client")
	public String listPersons(){
		SpringRestClient springRestClient = new SpringRestClient();
		AuthTokenInfo tokenInfo = springRestClient.sendTokenRequest();
		String result = "ListAllPersons : "+springRestClient.listAllPersons(tokenInfo);
		result += "<br /> GetPerson 1: "+springRestClient.getPerson(tokenInfo);
		
		
		return result;
	}
	
}
