package com.hessati.hessati.config;

import com.hessati.hessati.entities.Role;
import com.hessati.hessati.entities.SessionExercise;
import com.hessati.hessati.entities.SupportTicket;
import com.hessati.hessati.entities.User;
import com.hessati.hessati.entities.WorkoutSession;
import com.hessati.hessati.repositories.RoleRepository;
import com.hessati.hessati.repositories.SupportTicketRepository;
import com.hessati.hessati.repositories.UserRepository;
import com.hessati.hessati.repositories.WorkoutSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SupportTicketRepository ticketRepository;
    @Autowired private WorkoutSessionRepository sessionRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        seedRoles();
        seedUsers();
        seedTickets();
        seedWorkoutSessions();
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
            userRepository.save(buildUser("Nadia", "Benali", "nadia@email.com", "123456", clientRole, "Pro", 64, 2760, 9, 30));
            userRepository.save(buildUser("Karim", "Idrissi", "karim@email.com", "123456", clientRole, "Starter", 9, 360, 0, 4));
            userRepository.save(buildUser("Yasmine", "Chraibi", "yasmine@email.com", "123456", clientRole, "Elite", 103, 4680, 31, 31));
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
            userRepository.findByUsername("nadia@email.com").ifPresent(user -> {
                ticketRepository.save(buildTicket("Demande de changement de plan", "Nadia Benali", "in_progress", "low", 3, user));
            });
            userRepository.findByUsername("karim@email.com").ifPresent(user -> {
                ticketRepository.save(buildTicket("Problème de connexion après inscription", "Karim Idrissi", "resolved", "medium", 5, user));
            });
            userRepository.findByUsername("yasmine@email.com").ifPresent(user -> {
                ticketRepository.save(buildTicket("Suggestion : export PDF des séances", "Yasmine Chraibi", "open", "low", 1, user));
            });
        }
    }

    private void seedWorkoutSessions() {
        if (sessionRepository.count() > 0) return;

        int[] daysAgo = {1, 3, 5, 8, 12, 16, 21, 28, 35, 45, 55, 70, 85, 100, 120, 145, 170};
        String[] usernames = {"client@fitai.com", "marc@email.com", "julie@email.com", "lucas@email.com", "nadia@email.com", "yasmine@email.com"};

        for (String username : usernames) {
            userRepository.findByUsername(username).ifPresent(user -> {
                for (int i = 0; i < daysAgo.length; i++) {
                    SessionTemplate template = TEMPLATES[i % TEMPLATES.length];
                    LocalDateTime startTime = LocalDateTime.now().minusDays(daysAgo[i]).withHour(18).withMinute(30);
                    seedCompletedSession(user, template, startTime, i);
                }
            });
        }
    }

    private void seedCompletedSession(User user, SessionTemplate template, LocalDateTime startTime, int variantSeed) {
        WorkoutSession session = new WorkoutSession();
        session.setUser(user);
        session.setWorkoutPlanName(template.name);
        session.setWorkoutPlanType(template.type);
        session.setWorkoutPlanLevel(template.level);
        session.setDurationMinutes(template.duration);
        session.setCaloriesBurned(template.calories);
        session.setEndTime(startTime.plusMinutes(template.duration));
        session.setStatus(WorkoutSession.SessionStatus.COMPLETED);

        List<SessionExercise> exercises = new ArrayList<>();
        for (int i = 0; i < template.exercises.length; i++) {
            ExerciseSpec spec = template.exercises[i];
            SessionExercise se = new SessionExercise();
            se.setSession(session);
            se.setName(spec.name);
            se.setPlannedSets(spec.sets);
            boolean lastExerciseUnderPerformed = (i == template.exercises.length - 1) && (variantSeed % 2 == 0);
            se.setCompletedSets(lastExerciseUnderPerformed ? Math.max(spec.sets - 1, 0) : spec.sets);
            se.setReps(spec.reps);
            se.setWeight("");
            se.setCompleted(!lastExerciseUnderPerformed);
            exercises.add(se);
        }
        session.setExercises(exercises);

        WorkoutSession saved = sessionRepository.save(session);
        jdbcTemplate.update("UPDATE workout_session SET start_time = ? WHERE id = ?",
                Timestamp.valueOf(startTime), saved.getId());
    }

    private record ExerciseSpec(String name, int sets, String reps) {}

    private record SessionTemplate(String name, String type, String level, int duration, int calories, ExerciseSpec[] exercises) {}

    private static final SessionTemplate[] TEMPLATES = {
        new SessionTemplate("Push Day", "Force & Hypertrophie", "Intermédiaire", 60, 420, new ExerciseSpec[]{
            new ExerciseSpec("Développé couché barre", 4, "8-10"),
            new ExerciseSpec("Développé militaire haltères", 3, "10-12"),
            new ExerciseSpec("Extensions triceps poulie", 3, "12"),
            new ExerciseSpec("Dips", 3, "12")
        }),
        new SessionTemplate("Pull Day", "Hypertrophie", "Intermédiaire", 55, 380, new ExerciseSpec[]{
            new ExerciseSpec("Rowing barre", 4, "8-10"),
            new ExerciseSpec("Tractions", 3, "max"),
            new ExerciseSpec("Curl haltères", 3, "12"),
            new ExerciseSpec("Gainage", 3, "45s")
        }),
        new SessionTemplate("Leg Day", "Force & Hypertrophie", "Intermédiaire", 60, 450, new ExerciseSpec[]{
            new ExerciseSpec("Squat barre", 4, "8-10"),
            new ExerciseSpec("Fentes haltères", 3, "12/jambe"),
            new ExerciseSpec("Leg press", 3, "12"),
            new ExerciseSpec("Mollets debout", 3, "20")
        }),
        new SessionTemplate("Full Body Express", "Full Body", "Débutant", 45, 300, new ExerciseSpec[]{
            new ExerciseSpec("Squat haltères", 3, "12"),
            new ExerciseSpec("Pompes", 3, "12-15"),
            new ExerciseSpec("Rowing haltère", 3, "12/côté"),
            new ExerciseSpec("Planche", 3, "45s")
        }),
        new SessionTemplate("Cardio & Core", "Endurance", "Intermédiaire", 30, 350, new ExerciseSpec[]{
            new ExerciseSpec("Burpees", 4, "30s"),
            new ExerciseSpec("Mountain climbers", 4, "30s"),
            new ExerciseSpec("Crunch", 3, "20"),
            new ExerciseSpec("Gainage", 3, "45s")
        })
    };

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
