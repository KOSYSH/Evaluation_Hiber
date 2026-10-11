package ma.projet.classes;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "produits")
@NamedQueries({
    @NamedQuery(name = "Produit.parCategorie",
            query = "SELECT p FROM Produit p WHERE p.categorie.id = :categorieId ORDER BY p.id"),
    @NamedQuery(name = "Produit.commandesEntreDates",
            query = "SELECT DISTINCT p FROM LigneCommandeProduit l JOIN l.produit p "
                    + "WHERE l.commande.date BETWEEN :debut AND :fin ORDER BY p.id"),
    @NamedQuery(name = "Produit.parCommande",
            query = "SELECT l FROM LigneCommandeProduit l JOIN FETCH l.produit p "
                    + "WHERE l.commande.id = :commandeId ORDER BY p.id"),
    @NamedQuery(name = "Produit.prixSuperieurA100",
            query = "SELECT p FROM Produit p WHERE p.prix > 100 ORDER BY p.id")
})
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Reference obligatoire")
    @Column(nullable = false)
    private String reference;

    @Column(nullable = false)
    private float prix;

    @NotNull(message = "Categorie obligatoire")
    @ManyToOne
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @OneToMany(mappedBy = "produit")
    private List<LigneCommandeProduit> lignes = new ArrayList<>();

    public Produit() {
    }

    public Produit(String reference, float prix, Categorie categorie) {
        this.reference = reference;
        this.prix = prix;
        this.categorie = categorie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public List<LigneCommandeProduit> getLignes() {
        return lignes;
    }

    public void addLigne(LigneCommandeProduit ligne) {
        Objects.requireNonNull(ligne, "Ligne obligatoire");
        if (id == 0 || ligne.getPk() == null || id != ligne.getPk().getProduitId()) {
            throw new IllegalArgumentException("Produit incorrect");
        }
        if (!lignes.contains(ligne)) {
            lignes.add(ligne);
        }
        ligne.setProduit(this);
    }

    @Override
    public String toString() {
        return reference + " : " + prix + " DH";
    }
}
