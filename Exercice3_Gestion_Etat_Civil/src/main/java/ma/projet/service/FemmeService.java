package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import java.time.LocalDate;
import java.util.List;

public class FemmeService extends AbstractFacade<Femme> {
    public FemmeService() {
        super(Femme.class);
    }

    public Femme findPlusAgee() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Femme> femmes = session.createNamedQuery("Femme.plusAgee", Femme.class)
                    .setMaxResults(1).getResultList();
            return femmes.isEmpty() ? null : femmes.get(0);
        }
    }

    public long nombreEnfantsEntreDates(int femmeId, LocalDate debut, LocalDate fin) {
        verifierPeriode(debut, fin);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Number nombre = (Number) session.getNamedNativeQuery("Femme.nombreEnfantsEntreDates")
                    .setParameter("femmeId", femmeId).setParameter("debut", debut)
                    .setParameter("fin", fin).getSingleResult();
            return nombre.longValue();
        }
    }

    public void afficherNombreEnfantsEntreDates(int femmeId, LocalDate debut, LocalDate fin) {
        System.out.println("Nombre des enfants : " + nombreEnfantsEntreDates(femmeId, debut, fin));
    }

    public List<Femme> findMarieesAuMoinsDeuxFois() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Femme.marieesAuMoinsDeuxFois", Femme.class).getResultList();
        }
    }

    public void afficherMarieesAuMoinsDeuxFois() {
        for (Femme femme : findMarieesAuMoinsDeuxFois()) {
            System.out.println(femme);
        }
    }
}
