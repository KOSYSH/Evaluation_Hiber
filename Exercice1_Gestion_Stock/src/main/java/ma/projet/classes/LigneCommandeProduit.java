package ma.projet.classes;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "lignes_commande")
@NamedQuery(name = "LigneCommandeProduit.supprimerParCommande",
        query = "DELETE FROM LigneCommandeProduit l WHERE l.pk.commandeId = :commandeId")
public class LigneCommandeProduit {
    @NotNull(message = "Cle obligatoire")
    @EmbeddedId
    private CommandeProduitPK pk;
    @Column(nullable = false)
    private int quantite;

    @NotNull(message = "Produit obligatoire")
    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false, insertable = false, updatable = false)
    private Produit produit;
    @NotNull(message = "Commande obligatoire")
    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false, insertable = false, updatable = false)
    private Commande commande;

    public LigneCommandeProduit() {
    }

    public LigneCommandeProduit(int quantite, Produit produit, Commande commande) {
        if (produit == null || commande == null || produit.getId() == 0 || commande.getId() == 0) {
            throw new IllegalArgumentException("Enregistrer le produit et la commande avant la ligne");
        }
        this.pk = new CommandeProduitPK(commande.getId(), produit.getId());
        this.quantite = quantite;
        this.produit = produit;
        this.commande = commande;
    }

    public CommandeProduitPK getPk() {
        return pk;
    }

    public void setPk(CommandeProduitPK pk) {
        this.pk = pk;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }
}
