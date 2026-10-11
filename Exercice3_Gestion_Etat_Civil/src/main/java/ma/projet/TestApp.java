package ma.projet;

import ma.projet.beans.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestApp {
    public static void main(String[] args) {
        HommeService hs = new HommeService();
        FemmeService fs = new FemmeService();
        MariageService ms = new MariageService();
        DateTimeFormatter dates = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            System.out.println("Gestion de etat civil  BOUDAD Yasser  Bouarfa");
            Homme yasser = new Homme("BOUDAD", "Yasser", "06245687772", "Bouarfa", LocalDate.of(1998, 3, 10));
            Homme hamid = new Homme("BOUDAD", "Hamid", "617864665", "Bouarfa", LocalDate.of(1980, 5, 2));
            Homme said = new Homme("BOUDAD", "Said", "0788882282", "Oujda", LocalDate.of(1960, 7, 4));
            Homme omar = new Homme("BOUDAD", "Omar", "06245687772", "Bouarfa", LocalDate.of(1985, 9, 1));
            Homme karim = new Homme("BOUDAD", "Karim", "617864665", "Marrakech", LocalDate.of(1990, 11, 8));
            for (Homme homme : new Homme[]{yasser, hamid, said, omar, karim}) {
                hs.create(homme);
            }

            Femme hafida = new Femme("TAHRI", "Hafida", "0788882282", "Bouarfa", LocalDate.of(1960, 1, 1));
            Femme fatima = new Femme("TAHRI", "Fatima", "06245687772", "Oujda", LocalDate.of(1982, 2, 2));
            Femme nassira = new Femme("TAHRI", "Nassira", "617864665", "Bouarfa", LocalDate.of(1988, 3, 3));
            Femme fouzia = new Femme("TAHRI", "Fouzia", "0788882282", "Bouarfa", LocalDate.of(1990, 4, 4));
            Femme khadija = new Femme("TAHRI", "Khadija", "06245687772", "Oujda", LocalDate.of(1980, 5, 5));
            Femme aicha = new Femme("TAHRI", "Aicha", "617864665", "Bouarfa", LocalDate.of(1992, 6, 6));
            Femme samira = new Femme("TAHRI", "Samira", "0788882282", "Oujda", LocalDate.of(1987, 7, 7));
            Femme meryem = new Femme("TAHRI", "Meryem", "06245687772", "Marrakech", LocalDate.of(1996, 8, 8));
            Femme houda = new Femme("TAHRI", "Houda", "617864665", "Bouarfa", LocalDate.of(1994, 9, 9));
            Femme amina = new Femme("TAHRI", "Amina", "0788882282", "Marrakech", LocalDate.of(1995, 10, 10));
            for (Femme femme : new Femme[]{hafida, fatima, nassira, fouzia, khadija, aicha, samira, meryem, houda, amina}) {
                fs.create(femme);
            }

            Mariage ancienYasser = new Mariage(yasser, fatima, LocalDate.of(2018, 9, 3), LocalDate.of(2020, 9, 3), 1);
            Mariage mariageFatima = new Mariage(yasser, fatima, LocalDate.of(2021, 9, 3), null, 2);
            Mariage mariageNassira = new Mariage(yasser, nassira, LocalDate.of(2022, 1, 1), null, 1);
            Mariage mariageFouzia = new Mariage(yasser, fouzia, LocalDate.of(2023, 1, 1), null, 0);
            Mariage mariageAicha = new Mariage(yasser, aicha, LocalDate.of(2024, 1, 1), null, 1);
            for (Mariage mariage : new Mariage[]{ancienYasser, mariageFatima, mariageNassira, mariageFouzia, mariageAicha}) {
                mariage.getHomme().addMariage(mariage);
                mariage.getFemme().addMariage(mariage);
                ms.create(mariage);
            }

            System.out.println("Nombre des femmes : " + fs.findAll().size());
            System.out.println("Nombre des hommes : " + hs.findAll().size());
            System.out.println("Nombre des mariages : " + ms.findAll().size());
            System.out.println("\nListe des femmes :");
            for (Femme femme : fs.findAll()) {
                System.out.println(femme + "   Date naissance : " + femme.getDateNaissance().format(dates));
            }
            Femme plusAgee = fs.findPlusAgee();
            System.out.println("\nFemme la plus agee : " + plusAgee);
            System.out.println("Date naissance : " + plusAgee.getDateNaissance().format(dates));

            LocalDate debut = LocalDate.of(2024, 1, 1);
            LocalDate fin = LocalDate.of(2026, 12, 31);
            System.out.println("\nEpouses de BOUDAD Yasser entre le 01/01/2024 et le 31/12/2026 :");
            hs.afficherEpousesEntreDates(yasser.getId(), debut, fin);
            System.out.println("\nEnfants de TAHRI Fatima entre le 01/01/2019 et le 31/12/2026 :");
            fs.afficherNombreEnfantsEntreDates(fatima.getId(), LocalDate.of(2019, 1, 1), fin);
            System.out.println("\nFemmes mariees deux fois ou plus :");
            fs.afficherMarieesAuMoinsDeuxFois();
            System.out.println("\nHommes maries a quatre femmes entre le 01/01/2024 et le 31/12/2026 :");
            hs.afficherHommesMariesAQuatreFemmes(debut, fin);
            System.out.println("\nDetails des mariages :");
            ms.afficherMariages(yasser.getId());

            System.out.println("\nModification et recherche :");
            yasser.setTelephone("617864665");
            hs.update(yasser);
            System.out.println("Telephone trouve : " + hs.findById(yasser.getId()).getTelephone());
            fatima.setAdresse("Bouarfa");
            fs.update(fatima);
            System.out.println("Adresse trouvee : " + fs.findById(fatima.getId()).getAdresse());
            mariageFatima.setNbrEnfant(3);
            ms.update(mariageFatima);
            MariagePK pk = new MariagePK(yasser.getId(), fatima.getId(), LocalDate.of(2021, 9, 3));
            System.out.println("Nombre enfants trouve : " + ms.findById(pk).getNbrEnfant());

            System.out.println("\nSuppression :");
            ms.delete(ancienYasser);
            hs.delete(karim);
            fs.delete(amina);
            System.out.println("Femmes restantes : " + fs.findAll().size());
            System.out.println("Hommes restants : " + hs.findAll().size());
            System.out.println("Mariages restants : " + ms.findAll().size());
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
