package ma.projet;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestApp {
    public static void main(String[] args) {
        CategorieService cs = new CategorieService();
        ProduitService ps = new ProduitService();
        CommandeService cmds = new CommandeService();
        LigneCommandeService ls = new LigneCommandeService();
        DateTimeFormatter dates = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            System.out.println("Gestion de stok  BOUDAD Yasser  Bouarfa");

            Categorie accessoires = new Categorie("ACC", "Accessoires");
            Categorie stockage = new Categorie("STO", "Stockage");
            cs.create(accessoires);
            cs.create(stockage);

            Produit clavier = new Produit("CLV01", 120, accessoires);
            Produit usb = new Produit("USB01", 100, accessoires);
            Produit ssd = new Produit("SSD01", 200, stockage);
            Produit souris = new Produit("SRS01", 80, accessoires);
            ps.create(clavier);
            ps.create(usb);
            ps.create(ssd);
            ps.create(souris);

            Commande commande = new Commande(LocalDate.of(2026, 10, 5));
            Commande ancienne = new Commande(LocalDate.of(2026, 9, 20));
            cmds.create(commande);
            cmds.create(ancienne);
            LigneCommandeProduit ligne = new LigneCommandeProduit(2, clavier, commande);
            ls.create(ligne);
            ls.create(new LigneCommandeProduit(3, usb, commande));
            ls.create(new LigneCommandeProduit(1, ssd, commande));
            ls.create(new LigneCommandeProduit(4, clavier, ancienne));

            System.out.println("\nListe des categories :");
            for (Categorie categorie : cs.findAll()) {
                System.out.println(categorie);
            }
            System.out.println("\nListe des produits :");
            for (Produit produit : ps.findAll()) {
                System.out.println(produit);
            }
            System.out.println("Nombre de commandes : " + cmds.findAll().size());
            System.out.println("Nombre de lignes : " + ls.findAll().size());

            System.out.println("\nProduits par catgorie :");
            ps.afficherParCategorie(accessoires.getId());

            System.out.println("\nProduits comandes entre le 01/10/2026 et le 10/10/2026 :");
            ps.afficherCommandesEntreDates(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 10));

            System.out.println();
            ps.afficherParCommande(commande.getId());

            System.out.println("\nProduits de prix superieur a 100 DH :");
            ps.afficherPrixSuperieurA100();

            System.out.println("\nModification et recherche :");
            accessoires.setLibelle("Accessoires informatiques");
            cs.update(accessoires);
            System.out.println("Categorie trouvee : " + cs.findById(accessoires.getId()));
            souris.setPrix(85);
            ps.update(souris);
            System.out.println("Produit trouve : " + ps.findById(souris.getId()));
            commande.setDate(LocalDate.of(2026, 10, 6));
            cmds.update(commande);
            System.out.println("Date trouvee : " + cmds.findById(commande.getId()).getDate().format(dates));
            CommandeProduitPK pk = new CommandeProduitPK(commande.getId(), clavier.getId());
            System.out.println("Quantite trouvee : " + ls.findById(pk).getQuantite());
            ligne.setQuantite(5);
            ls.update(ligne);
            System.out.println("Nouvelle quantite : " + ls.findById(pk).getQuantite());

            System.out.println("\nSuppression :");
            ls.delete(ligne);
            System.out.println("Lignes restantes : " + ls.findAll().size());
            cmds.delete(commande);
            cmds.delete(ancienne);
            System.out.println("Commandes restantes : " + cmds.findAll().size());
            System.out.println("Lignes restantes : " + ls.findAll().size());
            System.out.println("Produits conserves : " + ps.findAll().size());
            ps.delete(souris);
            ps.delete(ssd);
            cs.delete(stockage);
            System.out.println("Produits restants : " + ps.findAll().size());
            System.out.println("Categories restantes : " + cs.findAll().size());
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
