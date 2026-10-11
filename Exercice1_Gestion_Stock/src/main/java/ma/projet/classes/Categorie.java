package ma.projet.classes;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categories")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Code obligatoire")
    @Column(nullable = false)
    private String code;
    @NotBlank(message = "Libelle obligatoire")
    @Column(nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "categorie")
    private List<Produit> produits = new ArrayList<>();

    public Categorie() {
    }

    public Categorie(String code, String libelle) {
        this.code = code;
        this.libelle = libelle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void addProduit(Produit produit) {
        Objects.requireNonNull(produit, "Produit obligatoire");
        Categorie ancienne = produit.getCategorie();
        if (ancienne != null && ancienne != this) {
            ancienne.getProduits().remove(produit);
        }
        if (!produits.contains(produit)) {
            produits.add(produit);
        }
        produit.setCategorie(this);
    }

    @Override
    public String toString() {
        return code + " : " + libelle;
    }
}
