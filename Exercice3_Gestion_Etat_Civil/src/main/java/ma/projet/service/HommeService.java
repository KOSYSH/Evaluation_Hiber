package ma.projet.service;

import ma.projet.beans.*;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

public class HommeService extends AbstractFacade<Homme> {
    public HommeService() {
        super(Homme.class);
    }

    public List<Femme> findEpousesEntreDates(int hommeId, LocalDate debut, LocalDate fin) {
        verifierPeriode(debut, fin);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Homme.epousesEntreDates", Femme.class)
                    .setParameter("hommeId", hommeId).setParameter("debut", debut)
                    .setParameter("fin", fin).getResultList();
        }
    }

    public void afficherEpousesEntreDates(int hommeId, LocalDate debut, LocalDate fin) {
        for (Femme femme : findEpousesEntreDates(hommeId, debut, fin)) {
            System.out.println(femme);
        }
    }

    private Subquery<Long> compterFemmes(CriteriaQuery<Homme> query, Root<Homme> homme,
                                         Expression<LocalDate> date) {
        CriteriaBuilder cb = HibernateUtil.getSessionFactory().getCriteriaBuilder();
        Subquery<Long> nombre = query.subquery(Long.class);
        Root<Mariage> mariage = nombre.from(Mariage.class);
        nombre.select(cb.countDistinct(mariage.get("femme").get("id")));
        nombre.where(cb.equal(mariage.get("homme"), homme),
                cb.lessThanOrEqualTo(mariage.get("pk").get("dateDebut"), date),
                cb.or(cb.isNull(mariage.get("dateFin")),
                        cb.greaterThanOrEqualTo(mariage.get("dateFin"), date)));
        return nombre;
    }

    public List<Homme> findHommesMariesAQuatreFemmes(LocalDate debut, LocalDate fin) {
        verifierPeriode(debut, fin);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Homme> query = cb.createQuery(Homme.class);
            Root<Homme> homme = query.from(Homme.class);
            Root<Mariage> mariage = query.from(Mariage.class);
            Path<LocalDate> dateDebut = mariage.get("pk").get("dateDebut");
            Path<LocalDate> dateFin = mariage.get("dateFin");
            Expression<LocalDate> date = cb.<LocalDate>selectCase()
                    .when(cb.lessThan(dateDebut, debut), cb.literal(debut)).otherwise(dateDebut);
            Expression<LocalDate> lendemain = cb.function("adddate", LocalDate.class, dateFin, cb.literal(1));
            Predicate quatreAuDebut = cb.and(cb.lessThanOrEqualTo(dateDebut, fin),
                    cb.or(cb.isNull(dateFin), cb.greaterThanOrEqualTo(dateFin, debut)),
                    cb.equal(compterFemmes(query, homme, date), 4L));
            Predicate quatreApresFin = cb.and(cb.isNotNull(dateFin),
                    cb.greaterThanOrEqualTo(dateFin, debut), cb.lessThan(dateFin, fin),
                    cb.equal(compterFemmes(query, homme, lendemain), 4L));
            query.select(homme).distinct(true).where(cb.equal(mariage.get("homme"), homme),
                    cb.or(quatreAuDebut, quatreApresFin)).orderBy(cb.asc(homme.get("id")));
            return session.createQuery(query).getResultList();
        }
    }

    public long countHommesMariesAQuatreFemmes(LocalDate debut, LocalDate fin) {
        return findHommesMariesAQuatreFemmes(debut, fin).size();
    }

    public void afficherHommesMariesAQuatreFemmes(LocalDate debut, LocalDate fin) {
        List<Homme> hommes = findHommesMariesAQuatreFemmes(debut, fin);
        System.out.println("Nombre des hommes maries a quatre femmes : " + hommes.size());
        for (Homme homme : hommes) {
            System.out.println(homme);
        }
    }
}
