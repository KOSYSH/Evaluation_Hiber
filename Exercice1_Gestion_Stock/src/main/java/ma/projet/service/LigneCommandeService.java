package ma.projet.service;

import ma.projet.classes.CommandeProduitPK;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

public class LigneCommandeService extends AbstractFacade<LigneCommandeProduit> {
    public LigneCommandeService() {
        super(LigneCommandeProduit.class);
    }

    public LigneCommandeProduit findById(CommandeProduitPK pk) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(LigneCommandeProduit.class, pk);
        }
    }

    @Override
    public LigneCommandeProduit findById(int id) {
        throw new IllegalArgumentException("Utiliser la cle commande et produit");
    }

    @Override
    public boolean create(LigneCommandeProduit ligne) {
        verifierLigne(ligne);
        return super.create(ligne);
    }

    @Override
    public boolean update(LigneCommandeProduit ligne) {
        verifierLigne(ligne);
        return super.update(ligne);
    }

    private void verifierLigne(LigneCommandeProduit ligne) {
        if (ligne == null || ligne.getPk() == null || ligne.getProduit() == null
                || ligne.getCommande() == null || ligne.getQuantite() < 1) {
            throw new IllegalArgumentException("Ligne incorrecte");
        }
        if (ligne.getPk().getProduitId() != ligne.getProduit().getId()
                || ligne.getPk().getCommandeId() != ligne.getCommande().getId()) {
            throw new IllegalArgumentException("Cle incorrecte");
        }
    }
}
