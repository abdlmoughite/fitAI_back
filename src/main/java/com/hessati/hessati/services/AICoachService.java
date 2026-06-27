package com.hessati.hessati.services;

import org.springframework.stereotype.Service;

@Service
public class AICoachService {

    public String getResponse(String message) {
        if (message == null || message.isBlank()) {
            return defaultResponse();
        }
        String lower = message.toLowerCase();

        if (containsAny(lower, "programme", "séance", "entraîn", "workout", "program")) {
            return programmeResponse();
        }
        if (containsAny(lower, "masse", "muscle", "volume", "hypertrophie", "prise")) {
            return masseResponse();
        }
        if (containsAny(lower, "poids", "maigrir", "perte", "cardio", "brûler", "calories")) {
            return pertePoidResponse();
        }
        if (containsAny(lower, "force", "strength", "powerlifting", "1rm", "max")) {
            return forceResponse();
        }
        if (containsAny(lower, "nutrition", "manger", "protéine", "protein", "alimentation", "régime")) {
            return nutritionResponse();
        }
        if (containsAny(lower, "conseil", "astuce", "tip", "aide", "help", "récup", "recovery", "repos")) {
            return conseilResponse();
        }
        if (containsAny(lower, "abdos", "core", "ventre", "gainage")) {
            return abdosResponse();
        }
        if (containsAny(lower, "jambe", "squat", "fess", "cuisse")) {
            return jambesResponse();
        }
        if (containsAny(lower, "dos", "back", "rowing", "traction", "dorsaux")) {
            return dosResponse();
        }
        if (containsAny(lower, "bras", "bicep", "tricep", "curl")) {
            return brasResponse();
        }

        return defaultResponse();
    }

    private boolean containsAny(String text, String... keywords) {
        for (String kw : keywords) {
            if (text.contains(kw)) return true;
        }
        return false;
    }

    private String programmeResponse() {
        return "**Programme recommandé — Push/Pull/Legs :**\n\n" +
               "**Jour 1 — Push** (Pectoraux, Épaules, Triceps)\n" +
               "• Développé couché : 4×8-10\n" +
               "• Développé militaire : 3×10-12\n" +
               "• Extensions triceps : 3×12\n\n" +
               "**Jour 2 — Pull** (Dos, Biceps)\n" +
               "• Rowing barre : 4×8-10\n" +
               "• Tractions : 3×max\n" +
               "• Curl haltères : 3×12\n\n" +
               "**Jour 3 — Legs**\n" +
               "• Squat : 4×8-10\n" +
               "• Fentes : 3×12/jambe\n" +
               "• Soulevé de terre : 3×8\n\n" +
               "Ça te convient ? Je peux ajuster selon ton niveau ! 💪";
    }

    private String masseResponse() {
        return "**Prise de masse sèche — Plan recommandé :**\n\n" +
               "**Nutrition :**\n" +
               "• Surplus calorique modéré : +200-300 kcal/jour\n" +
               "• Protéines : 2g/kg de poids corporel\n" +
               "• Glucides complexes : riz, patates douces, avoine\n" +
               "• Lipides sains : avocat, noix, huile d'olive\n\n" +
               "**Entraînement :**\n" +
               "• Focus sur les exercices polyarticulaires\n" +
               "• 4-6 séances/semaine\n" +
               "• 8-12 répétitions par série\n" +
               "• Progression de charge hebdomadaire\n\n" +
               "Prêt à commencer ce programme ? 🔥";
    }

    private String pertePoidResponse() {
        return "**Programme perte de poids — Approche optimale :**\n\n" +
               "**Nutrition :**\n" +
               "• Déficit calorique : -300 à -500 kcal/jour\n" +
               "• Maintenir les protéines élevées : 2g/kg\n" +
               "• Réduire les glucides raffinés\n" +
               "• Boire 2.5-3L d'eau par jour\n\n" +
               "**Entraînement :**\n" +
               "• 3-4 séances muscu/semaine pour préserver le muscle\n" +
               "• 2-3 séances cardio (HIIT ou LISS)\n" +
               "• Marche quotidienne : 8000-10000 pas\n\n" +
               "• La clé : la régularité sur la durée ! 🏃";
    }

    private String forceResponse() {
        return "**Programme Force — Méthode 5×5 :**\n\n" +
               "**Exercices principaux (3 séances/semaine) :**\n" +
               "• Squat : 5×5 progressif\n" +
               "• Développé couché : 5×5 progressif\n" +
               "• Soulevé de terre : 1×5 progressif\n" +
               "• Développé militaire : 5×5\n" +
               "• Rowing Pendlay : 5×5\n\n" +
               "**Principe :** Ajoute 2.5kg à chaque séance sur les membres supérieurs, 5kg sur les inférieurs.\n\n" +
               "**Repos :** 3-5 minutes entre les séries lourdes. Force avant hypertrophie ! 🏋️";
    }

    private String nutritionResponse() {
        return "**Nutrition sportive — Bases essentielles :**\n\n" +
               "**Macronutriments :**\n" +
               "• Protéines : 1.6-2.2g/kg de poids (viande, poisson, œufs, légumineuses)\n" +
               "• Glucides : 3-5g/kg selon l'intensité d'entraînement\n" +
               "• Lipides : 0.8-1.2g/kg (huile d'olive, avocats, oléagineux)\n\n" +
               "**Timing :**\n" +
               "• Avant séance : glucides complexes 1-2h avant\n" +
               "• Après séance : protéines + glucides dans les 30 min\n" +
               "• Hydratation : 2.5L minimum par jour\n\n" +
               "Veux-tu un plan nutritionnel personnalisé ? 🥗";
    }

    private String conseilResponse() {
        return "**Conseils récupération & performance :**\n\n" +
               "• **Sommeil :** 7-9h par nuit — c'est là que le muscle se construit\n" +
               "• **Protéines :** 1.6-2.2g/kg de poids corporel quotidien\n" +
               "• **Hydratation :** 2.5-3L d'eau par jour minimum\n" +
               "• **Repos musculaire :** 48h entre deux séances du même groupe\n" +
               "• **Étirements :** 10 min post-séance pour réduire les courbatures\n" +
               "• **Progressivité :** augmente la charge de 2.5-5% par semaine\n\n" +
               "Des questions spécifiques sur ta récupération ? 💤";
    }

    private String abdosResponse() {
        return "**Programme Abdos & Core :**\n\n" +
               "• Planche avant : 3×30-60s\n" +
               "• Crunchs bicycle : 3×15/côté\n" +
               "• Leg raises : 3×12-15\n" +
               "• Russian twist : 3×20\n" +
               "• Mountain climbers : 3×30s\n\n" +
               "**Important :** Les abdos se voient avec un faible taux de graisse. Combine ce circuit avec un déficit calorique pour des résultats visibles ! 🎯";
    }

    private String jambesResponse() {
        return "**Programme Jambes :**\n\n" +
               "• Squat barre : 4×8-10 (exercice roi)\n" +
               "• Leg press : 3×12\n" +
               "• Fentes marchées : 3×10/jambe\n" +
               "• Leg curl (ischio) : 3×12\n" +
               "• Mollets debout : 4×15\n\n" +
               "**Conseil :** Squattez profond pour activer les fessiers. Repos 2-3 min entre les séries lourdes. N'oublie pas les ischio-jambiers ! 🦵";
    }

    private String dosResponse() {
        return "**Programme Dos :**\n\n" +
               "• Tractions (ou Lat pulldown) : 4×max (4×10-12)\n" +
               "• Rowing barre : 4×8-10\n" +
               "• Tirage horizontal câble : 3×12\n" +
               "• Face pull : 3×15 (important pour les épaules)\n" +
               "• Shrugs haltères : 3×12\n\n" +
               "**Focus :** Contracte le dos en fin de mouvement, pas les bras. Visualise tes omoplates qui se rejoignent ! 💪";
    }

    private String brasResponse() {
        return "**Programme Bras (Biceps + Triceps) :**\n\n" +
               "**Biceps :**\n" +
               "• Curl barre : 3×10-12\n" +
               "• Curl marteau : 3×12\n" +
               "• Curl concentration : 2×12/bras\n\n" +
               "**Triceps :**\n" +
               "• Dips : 3×max\n" +
               "• Extensions poulie haute : 3×12\n" +
               "• Kickback haltère : 2×12/bras\n\n" +
               "**Note :** Les triceps représentent 2/3 du volume du bras — ne les néglige pas ! 💪";
    }

    private String defaultResponse() {
        return "Salut ! Je suis ton coach IA FitAI 💪\n\n" +
               "Je peux t'aider avec :\n" +
               "• **Programmes d'entraînement** personnalisés (push/pull/legs, full body, HIIT...)\n" +
               "• **Nutrition** et plan alimentaire adapté à tes objectifs\n" +
               "• **Prise de masse** ou perte de poids\n" +
               "• **Exercices spécifiques** par groupe musculaire\n" +
               "• **Conseils récupération** et optimisation des performances\n\n" +
               "Dis-moi ton objectif et je t'aide ! 🎯";
    }
}
