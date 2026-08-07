package com.workshop.vehicle_service.init;

import com.workshop.vehicle_service.auth.entity.Utilisateur;
import com.workshop.vehicle_service.auth.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
        if(utilisateurRepository.count() == 0) {
            // Création du compte conseiller (ROLE_USER)
            Utilisateur conseiller = new Utilisateur();
            conseiller.setUsername("Hayat SAIFI C");
            conseiller.setPassword(passwordEncoder.encode("1234")); // ChiffrementBCrypt
            conseiller.setRole("ROLE_USER");
            utilisateurRepository.save(conseiller);

            // Création du compte Responsable (ROLE_MANAGER)
            Utilisateur manager = new Utilisateur();
            manager.setUsername("HAYAT SAIFI M");
            manager.setPassword(passwordEncoder.encode("admin123")); // ChiffrementBCrypt
            manager.setRole("ROLE_MANAGER");
            utilisateurRepository.save(manager);
            System.out.println("Comptes fictifs crées avec succes");
        }

    }
}
