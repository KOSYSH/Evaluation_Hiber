package ma.projet.beans;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("HOMME")
@NamedQuery(name = "Homme.epousesEntreDates",
        query = "SELECT DISTINCT m.femme FROM Mariage m WHERE m.homme.id = :hommeId "
                + "AND m.pk.dateDebut <= :fin AND (m.dateFin IS NULL OR m.dateFin >= :debut) "
                + "ORDER BY m.femme.id")
public class Homme extends Personne {
    @OneToMany(mappedBy = "homme")
    private List<Mariage> mariages = new ArrayList<>();

    public Homme() {
    }

    public Homme(String nom, String prenom, String telephone, String adresse, LocalDate dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }

    public List<Mariage> getMariages() {
        return mariages;
    }

    public void addMariage(Mariage mariage) {
        Objects.requireNonNull(mariage, "Mariage obligatoire");
        if (mariage.getPk() != null && mariage.getPk().getHommeId() != id) {
            throw new IllegalArgumentException("Homme different de la cle");
        }
        if (!mariages.contains(mariage)) mariages.add(mariage);
        mariage.setHomme(this);
    }
}
