package ma.projet.service;

import ma.projet.classes.Commande;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CommandeService extends AbstractFacade<Commande> {
    public CommandeService() {
        super(Commande.class);
    }

    @Override
    public boolean delete(Commande commande) {
        if (commande == null || commande.getId() == 0) {
            return false;
        }
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Commande existante = session.get(Commande.class, commande.getId());
            if (existante == null) {
                tx.commit();
                return false;
            }

            session.createNamedQuery("LigneCommandeProduit.supprimerParCommande")
                    .setParameter("commandeId", commande.getId()).executeUpdate();
            session.delete(existante);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
