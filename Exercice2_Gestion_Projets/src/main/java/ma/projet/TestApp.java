package ma.projet;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TestApp {
    public static void main(String[] args) {
        EmployeService es = new EmployeService();
        ProjetService ps = new ProjetService();
        TacheService ts = new TacheService();
        EmployeTacheService ets = new EmployeTacheService();
        DateTimeFormatter dates = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            System.out.println("Gestion de projets  BOUDAD Yasser  Bouarfa");

            Employe yasser = new Employe("BOUDAD", "Yasser", "0600000001");
            Employe hamid = new Employe("BOUDAD", "Hamid", "0600000002");
            Employe salma = new Employe("BOUDAD", "Salma", "0600000003");
            es.create(yasser);
            es.create(hamid);
            es.create(salma);

            Projet stock = new Projet("Gestion de stock", LocalDate.of(2026, 10, 1),
                    LocalDate.of(2026, 10, 31), yasser);
            Projet site = new Projet("Site Bouarfa", LocalDate.of(2026, 11, 1),
                    LocalDate.of(2026, 11, 30), hamid);
            yasser.addProjet(stock);
            hamid.addProjet(site);
            ps.create(stock);
            ps.create(site);

            Tache analyse = new Tache("Analyse", LocalDate.of(2026, 10, 1),
                    LocalDate.of(2026, 10, 7), 1200, stock);
            Tache conception = new Tache("Conception", LocalDate.of(2026, 10, 8),
                    LocalDate.of(2026, 10, 15), 1000, stock);
            Tache developpement = new Tache("Developpement", LocalDate.of(2026, 10, 16),
                    LocalDate.of(2026, 10, 25), 5000, stock);
            Tache tests = new Tache("Tests", LocalDate.of(2026, 10, 26),
                    LocalDate.of(2026, 10, 31), 800, stock);
            Tache maquette = new Tache("Maquette", LocalDate.of(2026, 11, 1),
                    LocalDate.of(2026, 11, 10), 1500, site);
            Tache integration = new Tache("Integration", LocalDate.of(2026, 11, 11),
                    LocalDate.of(2026, 11, 20), 2000, site);
            for (Tache tache : new Tache[]{analyse, conception, developpement, tests, maquette, integration}) {
                tache.getProjet().addTache(tache);
                ts.create(tache);
            }

            EmployeTache analyseYasser = new EmployeTache(new EmployeTachePK(yasser.getId(), analyse.getId()),
                    LocalDate.of(2026, 10, 2), LocalDate.of(2026, 10, 5), yasser, analyse);
            EmployeTache analyseHamid = new EmployeTache(new EmployeTachePK(hamid.getId(), analyse.getId()),
                    LocalDate.of(2026, 10, 3), LocalDate.of(2026, 10, 6), hamid, analyse);
            EmployeTache conceptionHamid = new EmployeTache(new EmployeTachePK(hamid.getId(), conception.getId()),
                    LocalDate.of(2026, 10, 9), LocalDate.of(2026, 10, 12), hamid, conception);
            EmployeTache developpementSalma = new EmployeTache(new EmployeTachePK(salma.getId(), developpement.getId()),
                    LocalDate.of(2026, 10, 17), LocalDate.of(2026, 10, 24), salma, developpement);
            EmployeTache testsYasser = new EmployeTache(new EmployeTachePK(yasser.getId(), tests.getId()),
                    LocalDate.of(2026, 10, 26), null, yasser, tests);
            EmployeTache maquetteHamid = new EmployeTache(new EmployeTachePK(hamid.getId(), maquette.getId()),
                    LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 8), hamid, maquette);
            for (EmployeTache affectation : new EmployeTache[]{analyseYasser, analyseHamid, conceptionHamid,
                    developpementSalma, testsYasser, maquetteHamid}) {
                affectation.getEmploye().addAffectation(affectation);
                affectation.getTache().addAffectation(affectation);
                ets.create(affectation);
            }

            System.out.println("\nListe des employes :");
            for (Employe employe : es.findAll()) {
                System.out.println(employe);
            }
            System.out.println("Nombre de projets : " + ps.findAll().size());
            System.out.println("Nombre de taches : " + ts.findAll().size());
            System.out.println("Nombre des affectations : " + ets.findAll().size());

            System.out.println("\nTaches realisees par BOUDAD Yasser :");
            es.afficherTachesRealisees(yasser.getId());
            System.out.println("\nProjets geres par BOUDAD Yasser :");
            es.afficherProjetsGeres(yasser.getId());
            System.out.println("\nTaches planifiees pour Gestion de stock :");
            ps.afficherTachesPlanifiees(stock.getId());
            System.out.println("\nTaches realisees avec les dates reelles :");
            ps.afficherTachesRealisees(stock.getId());
            System.out.println("\nTaches de prix superieur a 1000 DH :");
            ts.afficherPrixSuperieurA1000();
            System.out.println("\nTaches realisees entre le 01/10/2026 et le 31/10/2026 :");
            ts.afficherRealiseesEntreDates(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31));
            System.out.printf(Locale.FRANCE, "%nCout global : %.2f DH%n", ps.calculerCoutGlobal(stock.getId()));
            System.out.println("Temps cumule : " + ps.calculerTempsCumuleJours(stock.getId()) + " jours");

            System.out.println("\nModification et recherche :");
            yasser.setTelephone("0612345678");
            es.update(yasser);
            System.out.println("Telephone trouve : " + es.findById(yasser.getId()).getTelephone());
            stock.setNom("Gestion de stock Bouarfa");
            ps.update(stock);
            System.out.println("Projet trouve : " + ps.findById(stock.getId()));
            tests.setPrix(900);
            ts.update(tests);
            System.out.println("Tache trouvee : " + ts.findById(tests.getId()));
            testsYasser.setDateFinReelle(LocalDate.of(2026, 10, 30));
            ets.update(testsYasser);
            EmployeTachePK pk = new EmployeTachePK(yasser.getId(), tests.getId());
            System.out.println("Date fin trouvee : " + ets.findById(pk).getDateFinReelle().format(dates));

            System.out.println("\nSuppression :");
            ets.delete(testsYasser);
            ts.delete(tests);
            ps.delete(site);
            ets.delete(developpementSalma);
            es.delete(salma);
            System.out.println("Employes restants : " + es.findAll().size());
            System.out.println("Projets restants : " + ps.findAll().size());
            System.out.println("Taches restantes : " + ts.findAll().size());
            System.out.println("Affectations restantes : " + ets.findAll().size());
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
