package com.example.client;
 
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.example.entity.Person;
import com.example.model.AuthTokenInfo;
 
public class SpringRestClient {
 
	// http://localhost:8080/springBootTemplateServer/oauth/token?grant_type=password&username=user&password=user
	// http://localhost:8080/springBootTemplateServer/persons?access_token=d6c3dfe9-bdf2-4978-b008-1f1ffbb78ad3
	// http://localhost:8080/springBootTemplateServer/oauth/token?grant_type=refresh_token&refresh_token=dfea499c-0eb8-4394-8c25-a7af5b1aa3d9
	 
	
    public static final String REST_SERVICE_URI = "http://localhost:8080/springBootTemplateServer";
    
    public static final String AUTH_SERVER_URI = "http://localhost:8080/springBootTemplateServer/oauth/token";
    
    public static final String QPM_PASSWORD_GRANT = "?grant_type=password&username=user&password=user";
    
    public static final String QPM_ACCESS_TOKEN = "?access_token=";

	    public static void main(String args[]){
	    	AuthTokenInfo tokenInfo = sendTokenRequest();
	    	listAllPersons(tokenInfo);
	    	getPerson(tokenInfo);
	    }
    
    /*
     * Prepare HTTP Headers.
     */
    private static HttpHeaders getHeaders(){
    	HttpHeaders headers = new HttpHeaders();
    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    	return headers;
    }
    
    /*
     * Add HTTP Authorization header, using Basic-Authentication to send client-credentials.
     */
    private static HttpHeaders getHeadersWithClientCredentials(){
    	String plainClientCredentials="root:q";
    	String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));
    	
    	HttpHeaders headers = getHeaders();
    	headers.add("Authorization", "Basic " + base64ClientCredentials);
    	return headers;
    }    
    
    /*
     * Send a POST request [on /oauth/token] to get an access-token, which will then be send with each request.
     */
    @SuppressWarnings({ "unchecked"})
	public static AuthTokenInfo sendTokenRequest(){
        RestTemplate restTemplate = new RestTemplate(); 
        
        HttpEntity<String> request = new HttpEntity<String>(getHeadersWithClientCredentials());
        ResponseEntity<Object> response = restTemplate.exchange(AUTH_SERVER_URI+QPM_PASSWORD_GRANT, HttpMethod.POST, request, Object.class);
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>)response.getBody();
        AuthTokenInfo tokenInfo = null;
        
        if(map!=null){
        	tokenInfo = new AuthTokenInfo();
        	tokenInfo.setAccess_token((String)map.get("access_token"));
        	tokenInfo.setToken_type((String)map.get("token_type"));
        	tokenInfo.setRefresh_token((String)map.get("refresh_token"));
        	tokenInfo.setExpires_in((int)map.get("expires_in"));
        	tokenInfo.setScope((String)map.get("scope"));
        	System.out.println(tokenInfo);
        }else{
            System.out.println("No person exist----------");
            
        }
        return tokenInfo;
    }
    
    /*
     * Send a GET request to get list of all persons.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String listAllPersons(AuthTokenInfo tokenInfo){
    	Assert.notNull(tokenInfo, "Authenticate first please......");
    	System.out.println("\nTesting listAllPersons API-----------");
        RestTemplate restTemplate = new RestTemplate(); 
        String person = "";
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        ResponseEntity<List> response = restTemplate.exchange(REST_SERVICE_URI+"/persons/"+QPM_ACCESS_TOKEN+tokenInfo.getAccess_token(),
        		HttpMethod.GET, request, List.class);
        List<LinkedHashMap<String, Object>> personsMap = (List<LinkedHashMap<String, Object>>)response.getBody();
        
        if(personsMap!=null){
            for(LinkedHashMap<String, Object> map : personsMap){
                System.out.println();
                person += "Person : id="+map.get("id")+", Name="+map.get("name")+", Age="+map.get("age")+", Salary="+map.get("salary");
            }
        }else{
            System.out.println("No person exist----------");
        }
        return person.toString();
    }
    
     
    /*
     * Send a GET request to get a specific person.
     */
    public static String getPerson(AuthTokenInfo tokenInfo){
    	Assert.notNull(tokenInfo, "Authenticate first please......");
        System.out.println("\nTesting getPerson API----------");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        ResponseEntity<Person> response = restTemplate.exchange(REST_SERVICE_URI+"/persons/1"+QPM_ACCESS_TOKEN+tokenInfo.getAccess_token(),
        		HttpMethod.GET, request, Person.class);
        Person person = response.getBody();
        System.out.println(person);
        return person.toString();
    }
   
}