package com.hessati.hessati.services;

import com.hessati.hessati.entities.WorkoutExercise;
import com.hessati.hessati.entities.WorkoutPlan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutService {

    public List<WorkoutPlan> generateWorkouts(String goal, String level, int duration) {
        List<WorkoutPlan> plans = new ArrayList<>();

        switch (goal) {
            case "muscle" -> plans.addAll(generateMassePlans(level, duration));
            case "strength" -> plans.addAll(generateForcePlans(level, duration));
            case "endurance" -> plans.addAll(generateEndurancePlans(level, duration));
            case "weight-loss" -> plans.addAll(generateWeightLossPlans(level, duration));
            default -> plans.addAll(generateMassePlans(level, duration));
        }

        return plans;
    }

    private List<WorkoutPlan> generateMassePlans(String level, int duration) {
        List<WorkoutPlan> plans = new ArrayList<>();

        WorkoutPlan ppl = new WorkoutPlan();
        ppl.setName("Push / Pull / Legs");
        ppl.setType("Force & Hypertrophie");
        ppl.setLevel(capitalize(level));
        ppl.setDuration(Math.min(duration, 60));
        ppl.setSessions(6);
        ppl.setCalories(duration == 30 ? 280 : duration == 45 ? 350 : 450);
        ppl.setGoal("muscle");
        ppl.setExercises(List.of(
            exercise("Développé couché barre", 4, "8-10", "90s"),
            exercise("Rowing barre", 4, "8-10", "90s"),
            exercise("Squat barre", 4, "10-12", "120s"),
            exercise("Développé militaire haltères", 3, "10-12", "60s"),
            exercise("Tirage horizontal", 3, "12", "60s"),
            exercise("Fentes haltères", 3, "12/jambe", "60s")
        ));
        plans.add(ppl);

        WorkoutPlan upperlower = new WorkoutPlan();
        upperlower.setName("Upper / Lower Split");
        upperlower.setType("Hypertrophie");
        upperlower.setLevel(capitalize(level));
        upperlower.setDuration(duration);
        upperlower.setSessions(4);
        upperlower.setCalories(duration == 30 ? 220 : duration == 45 ? 300 : 380);
        upperlower.setGoal("muscle");
        upperlower.setExercises(List.of(
            exercise("Développé incliné haltères", 4, "10-12", "75s"),
            exercise("Tractions", 4, "max", "90s"),
            exercise("Leg press", 4, "12", "90s"),
            exercise("Curl haltères", 3, "12", "45s"),
            exercise("Extensions triceps poulie", 3, "12", "45s")
        ));
        plans.add(upperlower);

        WorkoutPlan fullBody = new WorkoutPlan();
        fullBody.setName("Full Body Express");
        fullBody.setType("Full Body");
        fullBody.setLevel("Débutant");
        fullBody.setDuration(Math.min(duration, 45));
        fullBody.setSessions(3);
        fullBody.setCalories(duration == 30 ? 200 : 260);
        fullBody.setGoal("muscle");
        fullBody.setExercises(List.of(
            exercise("Squat haltères", 3, "12", "60s"),
            exercise("Pompes", 3, "10-15", "45s"),
            exercise("Rowing haltère", 3, "12/côté", "45s"),
            exercise("Développé épaules", 3, "12", "45s"),
            exercise("Planche", 3, "30-45s", "30s")
        ));
        plans.add(fullBody);

        return plans;
    }

    private List<WorkoutPlan> generateForcePlans(String level, int duration) {
        List<WorkoutPlan> plans = new ArrayList<>();

        WorkoutPlan stronglifts = new WorkoutPlan();
        stronglifts.setName("StrongLifts 5×5");
        stronglifts.setType("Force Pure");
        stronglifts.setLevel(capitalize(level));
        stronglifts.setDuration(duration);
        stronglifts.setSessions(3);
        stronglifts.setCalories(duration == 30 ? 250 : duration == 45 ? 320 : 400);
        stronglifts.setGoal("strength");
        stronglifts.setExercises(List.of(
            exercise("Squat barre", 5, "5", "3-5min"),
            exercise("Développé couché", 5, "5", "3min"),
            exercise("Soulevé de terre", 1, "5", "5min"),
            exercise("Développé militaire", 5, "5", "3min"),
            exercise("Rowing Pendlay", 5, "5", "3min")
        ));
        plans.add(stronglifts);

        WorkoutPlan powerlifting = new WorkoutPlan();
        powerlifting.setName("Programme Powerlifting");
        powerlifting.setType("Powerlifting");
        powerlifting.setLevel(capitalize(level));
        powerlifting.setDuration(duration);
        powerlifting.setSessions(4);
        powerlifting.setCalories(duration == 30 ? 280 : duration == 45 ? 360 : 440);
        powerlifting.setGoal("strength");
        powerlifting.setExercises(List.of(
            exercise("Squat compétition", 5, "3-5", "4-5min"),
            exercise("Développé couché compétition", 5, "3-5", "4min"),
            exercise("Soulevé de terre", 4, "3-4", "5min"),
            exercise("Romanian Deadlift", 3, "8", "90s"),
            exercise("Dips lestés", 3, "8", "2min")
        ));
        plans.add(powerlifting);

        WorkoutPlan conjugate = new WorkoutPlan();
        conjugate.setName("Conjugate Method");
        conjugate.setType("Force & Vitesse");
        conjugate.setLevel("Avancé");
        conjugate.setDuration(duration);
        conjugate.setSessions(4);
        conjugate.setCalories(duration == 30 ? 300 : duration == 45 ? 380 : 480);
        conjugate.setGoal("strength");
        conjugate.setExercises(List.of(
            exercise("Squat effort maximal", 5, "1-3", "5min"),
            exercise("Box squat dynamique", 8, "2", "45s"),
            exercise("Good morning", 4, "5", "2min"),
            exercise("Développé couché maximal", 5, "1-3", "5min"),
            exercise("Développé couché dynamique", 8, "3", "45s")
        ));
        plans.add(conjugate);

        return plans;
    }

    private List<WorkoutPlan> generateEndurancePlans(String level, int duration) {
        List<WorkoutPlan> plans = new ArrayList<>();

        WorkoutPlan hiit = new WorkoutPlan();
        hiit.setName("HIIT Cardio Intensif");
        hiit.setType("Cardio HIIT");
        hiit.setLevel(capitalize(level));
        hiit.setDuration(Math.min(duration, 30));
        hiit.setSessions(3);
        hiit.setCalories(duration == 30 ? 350 : 400);
        hiit.setGoal("endurance");
        hiit.setExercises(List.of(
            exercise("Burpees", 4, "30s", "15s"),
            exercise("Mountain climbers", 4, "30s", "15s"),
            exercise("Jump squats", 4, "30s", "15s"),
            exercise("High knees", 4, "30s", "15s"),
            exercise("Jumping jacks", 4, "30s", "15s")
        ));
        plans.add(hiit);

        WorkoutPlan circuit = new WorkoutPlan();
        circuit.setName("Circuit Training");
        circuit.setType("Cardio-Muscu");
        circuit.setLevel(capitalize(level));
        circuit.setDuration(duration);
        circuit.setSessions(4);
        circuit.setCalories(duration == 30 ? 300 : duration == 45 ? 380 : 450);
        circuit.setGoal("endurance");
        circuit.setExercises(List.of(
            exercise("Pompes", 3, "15", "30s"),
            exercise("Squat sauté", 3, "15", "30s"),
            exercise("Dips sur chaise", 3, "12", "30s"),
            exercise("Fentes alternées", 3, "12/jambe", "30s"),
            exercise("Planche", 3, "45s", "30s"),
            exercise("Corde à sauter", 3, "1min", "30s")
        ));
        plans.add(circuit);

        WorkoutPlan running = new WorkoutPlan();
        running.setName("Programme Running + Renfo");
        running.setType("Endurance & Force");
        running.setLevel(capitalize(level));
        running.setDuration(duration);
        running.setSessions(5);
        running.setCalories(duration == 30 ? 320 : duration == 45 ? 400 : 500);
        running.setGoal("endurance");
        running.setExercises(List.of(
            exercise("Course fractionnée 400m", 6, "×", "2min"),
            exercise("Fentes coureurs", 3, "12/jambe", "45s"),
            exercise("Mollets debout", 4, "20", "30s"),
            exercise("Gainage latéral", 3, "30s/côté", "20s"),
            exercise("Hip bridge", 3, "15", "30s")
        ));
        plans.add(running);

        return plans;
    }

    private List<WorkoutPlan> generateWeightLossPlans(String level, int duration) {
        List<WorkoutPlan> plans = new ArrayList<>();

        WorkoutPlan fatBurn = new WorkoutPlan();
        fatBurn.setName("Fat Burn Express");
        fatBurn.setType("Perte de Poids HIIT");
        fatBurn.setLevel(capitalize(level));
        fatBurn.setDuration(Math.min(duration, 30));
        fatBurn.setSessions(4);
        fatBurn.setCalories(duration == 30 ? 400 : 480);
        fatBurn.setGoal("weight-loss");
        fatBurn.setExercises(List.of(
            exercise("Burpees", 5, "30s", "10s"),
            exercise("Sprint sur place", 5, "30s", "10s"),
            exercise("Box jumps", 4, "10", "30s"),
            exercise("Battle ropes", 4, "30s", "15s"),
            exercise("Kettlebell swing", 4, "15", "30s")
        ));
        plans.add(fatBurn);

        WorkoutPlan bodyWeight = new WorkoutPlan();
        bodyWeight.setName("Poids de Corps Complet");
        bodyWeight.setType("Cardio-Musculation");
        bodyWeight.setLevel(capitalize(level));
        bodyWeight.setDuration(duration);
        bodyWeight.setSessions(4);
        bodyWeight.setCalories(duration == 30 ? 320 : duration == 45 ? 400 : 480);
        bodyWeight.setGoal("weight-loss");
        bodyWeight.setExercises(List.of(
            exercise("Pompes variées", 3, "15", "30s"),
            exercise("Squat jump", 3, "15", "30s"),
            exercise("Fentes bulgares", 3, "10/jambe", "30s"),
            exercise("Dips", 3, "12", "30s"),
            exercise("Russian twist", 3, "20", "20s"),
            exercise("Leg raises", 3, "15", "20s")
        ));
        plans.add(bodyWeight);

        WorkoutPlan hiitMuscu = new WorkoutPlan();
        hiitMuscu.setName("HIIT + Musculation");
        hiitMuscu.setType("Hybride Perte de Poids");
        hiitMuscu.setLevel(capitalize(level));
        hiitMuscu.setDuration(duration);
        hiitMuscu.setSessions(5);
        hiitMuscu.setCalories(duration == 30 ? 360 : duration == 45 ? 450 : 520);
        hiitMuscu.setGoal("weight-loss");
        hiitMuscu.setExercises(List.of(
            exercise("Squat haltères", 3, "15", "30s"),
            exercise("Développé couché", 3, "15", "30s"),
            exercise("Rowing haltère", 3, "15/côté", "30s"),
            exercise("Rowing haltère", 3, "15/côté", "30s"),
            exercise("Mountain climbers 20s sprint", 5, "20s", "10s"),
            exercise("Burpees 20s sprint", 5, "20s", "10s")
        ));
        plans.add(hiitMuscu);

        return plans;
    }

    private WorkoutExercise exercise(String name, int sets, String reps, String rest) {
        WorkoutExercise ex = new WorkoutExercise();
        ex.setName(name);
        ex.setSets(sets);
        ex.setReps(reps);
        ex.setRest(rest);
        return ex;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return switch (s) {
            case "beginner" -> "Débutant";
            case "intermediate" -> "Intermédiaire";
            case "advanced" -> "Avancé";
            default -> s.substring(0, 1).toUpperCase() + s.substring(1);
        };
    }
}
