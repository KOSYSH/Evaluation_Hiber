package ma.projet.service;

import ma.projet.classes.*;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ProduitService extends AbstractFacade<Produit> {
    public ProduitService() {
        super(Produit.class);
    }

    public List<Produit> findByCategorie(int categorieId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Produit.parCategorie", Produit.class)
                    .setParameter("categorieId", categorieId).getResultList();
        }
    }

    public List<Produit> findCommandesEntreDates(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null || debut.isAfter(fin)) {
            throw new IllegalArgumentException("Dates incorrectes");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Produit.commandesEntreDates", Produit.class)
                    .setParameter("debut", debut).setParameter("fin", fin).getResultList();
        }
    }

    public List<LigneCommandeProduit> findByCommande(int commandeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Produit.parCommande", LigneCommandeProduit.class)
                    .setParameter("commandeId", commandeId).getResultList();
        }
    }

    public List<Produit> findPrixSuperieurA100() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Produit.prixSuperieurA100", Produit.class).getResultList();
        }
    }

    public void afficherParCategorie(int categorieId) {
        for (Produit produit : findByCategorie(categorieId)) {
            System.out.println(produit);
        }
    }

    public void afficherCommandesEntreDates(LocalDate debut, LocalDate fin) {
        for (Produit produit : findCommandesEntreDates(debut, fin)) {
            System.out.println(produit);
        }
    }

    public void afficherPrixSuperieurA100() {
        for (Produit produit : findPrixSuperieurA100()) {
            System.out.println(produit);
        }
    }

    public void afficherParCommande(int commandeId) {
        Commande commande = new CommandeService().findById(commandeId);
        if (commande == null) {
            System.out.println("Commande introuvable : " + commandeId);
            return;
        }
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
        DecimalFormat formatPrix = new DecimalFormat("0.##", DecimalFormatSymbols.getInstance(Locale.FRENCH));
        List<LigneCommandeProduit> lignes = findByCommande(commandeId);
        System.out.println("Commande : " + commande.getId() + "     Date : " + commande.getDate().format(formatDate));
        System.out.println("Liste des produits :");
        System.out.printf("%-12s %-12s %s%n", "Référence", "Prix", "Quantité");
        for (LigneCommandeProduit ligne : lignes) {
            System.out.printf("%-12s %-12s %d%n", ligne.getProduit().getReference(),
                    formatPrix.format(ligne.getProduit().getPrix()) + " DH", ligne.getQuantite());
        }
        if (lignes.isEmpty()) {
            System.out.println("Aucun produit");
        }
    }
}
