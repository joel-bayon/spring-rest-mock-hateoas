package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*; //pour méthode static linkTo

import entities.Adherent;
import jaxb.AdherentList;
import repositories.AdherentRepository;

@RestController
@EnableEntityLinks
@ExposesResourceFor(Adherent.class)
@EnableHypermediaSupport(type = { HypermediaType.HAL})
@RequestMapping("/adherents")
public class AdherentController {
	
	@Autowired
	AdherentRepository repository;
	
	@Autowired
	EntityLinks entityLinks;
	
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<?> getAdherent(@PathVariable Integer id ) {
		System.out.println("/adherent/"+id);
		Adherent adherent   =  repository.findOne(id);
		if(adherent == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(adherent, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/hateoas/{id}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getAdherent(@PathVariable Integer id, HttpServletRequest request ) {
		System.out.println("/adherent/"+id);
		Adherent adherent   =  repository.findOne(id);
		if(adherent == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	   Resource<Adherent> resource = new Resource<>(adherent, linkTo(AdherentController.class).slash("/hateoas/"+id).withSelfRel());
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="", method=RequestMethod.POST, 
			produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, 
			consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})

	public ResponseEntity<?> createAdherent(@RequestBody Adherent adherent) {
	
			System.out.println(adherent);
			Adherent old = repository.findByNomAndPrenomAndEmail(adherent.getNom(), adherent.getPrenom(), adherent.getEmail());
			if(old != null)
				return new ResponseEntity<>("Adherent already present in database",HttpStatus.CONFLICT);
			adherent = repository.save(adherent);
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(ServletUriComponentsBuilder
					.fromCurrentRequest().path("/{id}")
					.buildAndExpand(adherent.getId()).toUri());
			return  new ResponseEntity<>(adherent, httpHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT, 
			consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Void> updateAdherent(@PathVariable Integer id,   @RequestBody Adherent adherent) {
			if(!repository.exists(id))
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			//return new ResponseEntity<>(HttpStatus.CONFLICT);
			repository.save(adherent);
			return  new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteAdherent(@PathVariable Integer id) {
			if(!repository.exists(id))
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			repository.delete(id);
			return  new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}/short", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ShortAdherent getShortAdherent(@PathVariable Integer id ) {
		System.out.println("/adherent/{id}");
		ShortAdherent sa= new ShortAdherent(repository.findOne(id));
		Link link  = linkTo(AdherentController.class).slash(id).withSelfRel();
		sa.add(link);
		sa.add(linkTo(AdherentController.class).slash("/").withRel("list"));
		return sa;
		
	}
	
	
	@RequestMapping(value="", method=RequestMethod.GET, produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	
	@ResponseBody public List<Adherent> getAdherentAll() {
		System.out.println("/adherent");
		return repository.findAll();
	}
	
	@RequestMapping(value="/hateoas", produces={MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public Resources<?>showAdherentAll() {
		System.out.println("/hateoas");
		List<Adherent> adherents  = repository.findAll();
		List<Link> links = new ArrayList<Link>();
		for(Adherent a : adherents) 
			links.add(linkTo(AdherentController.class).slash("hateoas").slash(a.getId()).withSelfRel());
		Resources<?> resources = new Resources<>(adherents,links);
	    return resources;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String responseError(Exception e) {
		System.out.println(e.getMessage());
		return e.getMessage();
		
	}
	
	@SuppressWarnings("serial")
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	class MediaTypeException extends RuntimeException {
		public MediaTypeException() {
			super("Invalid media type");
			System.out.println("MediaTypeException");
		}
	}
	
	@SuppressWarnings("serial")
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	class ControllerException extends RuntimeException {
		public ControllerException(Throwable e) {
			super("Internal server error", e.getCause());
			//...
		}
	}
	
	@XmlRootElement
	public static class ShortAdherent extends ResourceSupport {
	 
		@JsonProperty
		@XmlElement
		Integer id;
		@JsonProperty
		@XmlElement
		String nomPrenom;
	   public ShortAdherent(Adherent adherent) {
		   id = adherent.getId();
		   nomPrenom   = adherent.getNom() + " " + adherent.getPrenom();
	   }
	   public ShortAdherent() { }
	}
}
