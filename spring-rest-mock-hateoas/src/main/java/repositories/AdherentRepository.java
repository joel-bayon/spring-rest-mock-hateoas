package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import entities.Adherent;

public interface AdherentRepository extends JpaRepository<Adherent, Integer> {
	 List<Adherent> findByPrenom(String lastName);
	 Adherent findByNomAndPrenomAndEmail(String nom, String prenom, String email);
}
