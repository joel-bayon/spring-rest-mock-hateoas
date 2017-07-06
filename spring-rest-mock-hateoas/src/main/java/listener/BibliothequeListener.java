package listener;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;

import entities.Adherent;
import repositories.AdherentRepository;
import util.SpringContextProvider;


/**
 * Application Lifecycle Listener implementation class BibliothequeListener
 *
 */
public class BibliothequeListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public BibliothequeListener() {
        // TODO Auto-generated constructor stub 
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) { 
    	ServletContext ctx = event.getServletContext();
    		System.out.println(ctx.getContextPath());
    		ApplicationContext context  = SpringContextProvider.getApplicationContext();
    		AdherentRepository repository = context.getBean(AdherentRepository.class);
    		Adherent ad1 = new Adherent("Durant", "Pascal", "0240563412", "pascal.durant@free.fr");
    		Adherent ad2 = new Adherent("Martin", "Jean", "0240992345", "jean.martin@laposte.fr");
    		
    		repository.save(ad1);
    		repository.save(ad2);
    		repository.save(new Adherent("nom", "prenom", "tel", "email"));
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {}
	
}
