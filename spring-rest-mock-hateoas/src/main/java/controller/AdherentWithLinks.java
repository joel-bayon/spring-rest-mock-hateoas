package controller;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

import entities.Adherent;

@XmlRootElement(name="Adherent")
//@JsonIgnoreProperties({ "id" })
//@JacksonXmlRootElement(localName="Adherent")
public  class AdherentWithLinks extends ResourceSupport {
	@XmlElement
	@JsonProperty
	Integer id ; 
	@XmlElement
	@JsonProperty
	String nom, prenom, tel, email;
   public AdherentWithLinks(Adherent a) {
	  id = a.getId();
	  nom = a.getNom(); prenom=a.getPrenom(); tel=a.getTel(); email=a.getEmail();
   }
   public AdherentWithLinks() { }
}
