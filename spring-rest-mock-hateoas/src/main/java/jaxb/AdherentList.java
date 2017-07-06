package jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import entities.Adherent;


@XmlRootElement(name="adherents")
public class AdherentList {
	
	List<Adherent> adherents;
	
	public AdherentList(List<Adherent> adherents) {
		this.adherents = adherents;
	}
	
	public AdherentList() {}

	public List<Adherent> getAdherents() {
		return adherents;
	}
	@XmlElement(name="adherent")
	public void setAdherents(List<Adherent> adherents) {
		this.adherents = adherents;
	}

}
