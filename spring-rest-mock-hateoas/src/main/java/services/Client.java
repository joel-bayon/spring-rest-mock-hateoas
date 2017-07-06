package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import entities.Adherent;
import repositories.AdherentRepository;

@Component
public class Client {
	
	@Autowired
	private AdherentRepository repository;
	
	@Transactional
	public void doSomething() {
		
		for(Adherent adherent : repository.findByPrenom("Jean")) {
			System.out.println(adherent);
			adherent.setPrenom("Paul");
		};
		
	}
	

	public static void main(String[] args) {
		ConfigurableApplicationContext spring = new ClassPathXmlApplicationContext("spring.xml");
		spring.getBean(Client.class).doSomething();
		spring.close();

	}

}
