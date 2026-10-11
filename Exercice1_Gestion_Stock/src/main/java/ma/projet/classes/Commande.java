package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "commandes")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "Date obligatoire")
    @Column(name = "date_commande", nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "commande")
    private List<LigneCommandeProduit> lignes = new ArrayList<>();

    public Commande() {
    }

    public Commande(LocalDate date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<LigneCommandeProduit> getLignes() {
        return lignes;
    }

    public void addLigne(LigneCommandeProduit ligne) {
        Objects.requireNonNull(ligne, "Ligne obligatoire");
        if (id == 0 || ligne.getPk() == null || id != ligne.getPk().getCommandeId()) {
            throw new IllegalArgumentException("Commande incorrecte");
        }
        if (!lignes.contains(ligne)) {
            lignes.add(ligne);
        }
        ligne.setCommande(this);
    }

    @Override
    public String toString() {
        return id + " : " + date;
    }
}
