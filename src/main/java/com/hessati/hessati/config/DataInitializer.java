package com.hessati.hessati.config;

import com.hessati.hessati.entities.Role;
import com.hessati.hessati.entities.SupportTicket;
import com.hessati.hessati.entities.User;
import com.hessati.hessati.repositories.RoleRepository;
import com.hessati.hessati.repositories.SupportTicketRepository;
import com.hessati.hessati.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SupportTicketRepository ticketRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedRoles();
        seedUsers();
        seedTickets();
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(roleOf("client"));
            roleRepository.save(roleOf("admin"));
            roleRepository.save(roleOf("super_admin"));
        }
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            Role clientRole = roleRepository.findByRoleName("client").orElseThrow();
            Role adminRole = roleRepository.findByRoleName("admin").orElseThrow();
            Role superRole = roleRepository.findByRoleName("super_admin").orElseThrow();

            userRepository.save(buildUser("Alexandre", "Martin", "client@fitai.com", "123456", clientRole, "Pro", 128, 4320, 12, 45));
            userRepository.save(buildUser("Sophie", "Laurent", "admin@fitai.com", "123456", adminRole, "Pro", 56, 2100, 5, 21));
            userRepository.save(buildUser("Thomas", "Dubois", "superadmin@fitai.com", "123456", superRole, "Enterprise", 0, 0, 0, 0));

            // Extra client users
            userRepository.save(buildUser("Marc", "Dupont", "marc@email.com", "123456", clientRole, "Pro", 42, 1890, 3, 15));
            userRepository.save(buildUser("Julie", "Moreau", "julie@email.com", "123456", clientRole, "Starter", 18, 720, 1, 8));
            userRepository.save(buildUser("Lucas", "Petit", "lucas@email.com", "123456", clientRole, "Elite", 87, 3600, 22, 60));
        }
    }

    private void seedTickets() {
        if (ticketRepository.count() == 0) {
            userRepository.findByUsername("marc@email.com").ifPresent(user -> {
                ticketRepository.save(buildTicket("Impossible de générer un programme", "Marc Dupont", "open", "high", 4, user));
            });
            userRepository.findByUsername("julie@email.com").ifPresent(user -> {
                ticketRepository.save(buildTicket("Bug sur le chronomètre", "Julie Moreau", "in_progress", "medium", 6, user));
            });
            userRepository.findByUsername("lucas@email.com").ifPresent(user -> {
                ticketRepository.save(buildTicket("Question sur l'abonnement Elite", "Lucas Petit", "open", "low", 2, user));
                ticketRepository.save(buildTicket("Données d'analytiques incorrectes", "Lucas Petit", "resolved", "high", 8, user));
            });
        }
    }

    private Role roleOf(String name) {
        Role r = new Role();
        r.setRoleName(name);
        return r;
    }

    private User buildUser(String firstname, String lastname, String email, String password,
                           Role role, String plan, int workouts, int minutes, int streak, int bestStreak) {
        User u = new User();
        u.setFirstname(firstname);
        u.setLastname(lastname);
        u.setEmail(email);
        u.setUsername(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setRole(role);
        u.setPlan(plan);
        u.setTotalWorkouts(workouts);
        u.setTotalMinutes(minutes);
        u.setCurrentStreak(streak);
        u.setBestStreak(bestStreak);
        return u;
    }

    private SupportTicket buildTicket(String subject, String userName, String status, String priority, int messages, User user) {
        SupportTicket t = new SupportTicket();
        t.setSubject(subject);
        t.setUserName(userName);
        t.setStatus(status);
        t.setPriority(priority);
        t.setMessageCount(messages);
        t.setUser(user);
        return t;
    }
}
