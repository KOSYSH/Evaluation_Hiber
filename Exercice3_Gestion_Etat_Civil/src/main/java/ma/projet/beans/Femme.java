package ma.projet.beans;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("FEMME")
@NamedQueries({
        @NamedQuery(name = "Femme.plusAgee", query = "FROM Femme f ORDER BY f.dateNaissance, f.id"),
        @NamedQuery(name = "Femme.marieesAuMoinsDeuxFois",
                query = "SELECT f FROM Femme f WHERE "
                        + "(SELECT COUNT(m) FROM Mariage m WHERE m.femme = f) >= 2 ORDER BY f.id")
})
@NamedNativeQuery(name = "Femme.nombreEnfantsEntreDates",
        query = "SELECT COALESCE(SUM(nbr_enfant), 0) FROM mariages "
                + "WHERE femme_id = :femmeId AND date_debut <= :fin "
                + "AND (date_fin IS NULL OR date_fin >= :debut)")
public class Femme extends Personne {
    @OneToMany(mappedBy = "femme")
    private List<Mariage> mariages = new ArrayList<>();

    public Femme() {
    }

    public Femme(String nom, String prenom, String telephone, String adresse, LocalDate dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }

    public List<Mariage> getMariages() {
        return mariages;
    }

    public void addMariage(Mariage mariage) {
        Objects.requireNonNull(mariage, "Mariage obligatoire");
        if (mariage.getPk() != null && mariage.getPk().getFemmeId() != id) {
            throw new IllegalArgumentException("Femme differente de la cle");
        }
        if (!mariages.contains(mariage)) mariages.add(mariage);
        mariage.setFemme(this);
    }
}
