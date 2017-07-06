package client;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import entities.Adherent;

public class ClientRest {
	public static void main(String[] args) {
		
		try {
		RestTemplate template = new RestTemplate();
		String url = "http://localhost:8080//spring4-mvc-rest/rest/adherents/";
		
		HttpEntity<Adherent> request = new HttpEntity<>(new Adherent("Martin", "Jean", "01233412", "j.m@gmail.com"));
		Adherent a = template.postForObject(url, request, Adherent.class);
		assertThat(a, notNullValue());assertThat(a.getNom(), is("Martin"));
		
		String result = template.getForObject(url+a.getId(), String.class);
		System.out.println(result);
		a = template.getForObject(url+a.getId(), Adherent.class);
		System.out.println(a);
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> re   = template.exchange(url+a.getId(), HttpMethod.GET, entity, String.class);
		System.out.println(re.getBody());
		a.setPrenom("Patrick");
		HttpEntity<Adherent> requestUpdate = new HttpEntity<>(a, headers);
		ResponseEntity<Void> response = template.exchange(url+a.getId(), HttpMethod.PUT, requestUpdate, Void.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		
		template.delete(url+a.getId());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
