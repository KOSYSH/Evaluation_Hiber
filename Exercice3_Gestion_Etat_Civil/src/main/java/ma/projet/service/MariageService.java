package ma.projet.service;

import ma.projet.beans.*;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MariageService extends AbstractFacade<Mariage> {
    public MariageService() {
        super(Mariage.class);
    }

    public Mariage findById(MariagePK pk) {
        if (pk == null) throw new IllegalArgumentException("Cle obligatoire");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Mariage.class, pk);
        }
    }

    @Override
    public Mariage findById(int id) {
        throw new IllegalArgumentException("Utiliser findById(MariagePK)");
    }

    private void verifierMariage(Mariage mariage) {
        if (mariage == null || mariage.getPk() == null || mariage.getHomme() == null
                || mariage.getFemme() == null || mariage.getHomme().getId() == 0
                || mariage.getFemme().getId() == 0 || mariage.getDateDebut() == null) {
            throw new IllegalArgumentException("Mariage incomplet");
        }
        if (mariage.getPk().getHommeId() != mariage.getHomme().getId()
                || mariage.getPk().getFemmeId() != mariage.getFemme().getId()) {
            throw new IllegalArgumentException("La cle ne correspond pas aux personnes");
        }
        if (mariage.getDateFin() != null) {
            verifierPeriode(mariage.getDateDebut(), mariage.getDateFin());
        }
        if (mariage.getNbrEnfant() < 0) {
            throw new IllegalArgumentException("Nombre enfants incorrect");
        }
    }

    @Override
    public boolean create(Mariage mariage) {
        verifierMariage(mariage);
        return super.create(mariage);
    }

    @Override
    public boolean update(Mariage mariage) {
        verifierMariage(mariage);
        return super.update(mariage);
    }

    public List<Mariage> findByHomme(int hommeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Mariage.parHomme", Mariage.class)
                    .setParameter("hommeId", hommeId).getResultList();
        }
    }

    public void afficherMariages(int hommeId) {
        Homme homme = new HommeService().findById(hommeId);
        if (homme == null) {
            System.out.println("Homme introuvable");
            return;
        }
        List<Mariage> mariages = findByHomme(hommeId);
        DateTimeFormatter dates = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Nom : " + homme);
        System.out.println("Mariages En Cours :");
        int numero = 1;
        for (Mariage mariage : mariages) {
            if (mariage.getDateFin() == null) {
                System.out.println(numero++ + ". Femme : " + mariage.getFemme()
                        + "   Date Debut : " + mariage.getDateDebut().format(dates)
                        + "   Nbr Enfants : " + mariage.getNbrEnfant());
            }
        }
        System.out.println("\nMariages echoues :");
        numero = 1;
        for (Mariage mariage : mariages) {
            if (mariage.getDateFin() != null) {
                System.out.println(numero++ + ". Femme : " + mariage.getFemme()
                        + "   Date Debut : " + mariage.getDateDebut().format(dates)
                        + "   Date Fin : " + mariage.getDateFin().format(dates)
                        + "   Nbr Enfants : " + mariage.getNbrEnfant());
            }
        }
    }
}
