package ma.projet.beans;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "mariages")
@NamedQuery(name = "Mariage.parHomme",
        query = "FROM Mariage m WHERE m.homme.id = :hommeId ORDER BY m.pk.dateDebut, m.pk.femmeId")
public class Mariage {
    @NotNull(message = "Cle obligatoire")
    @EmbeddedId
    private MariagePK pk;
    @NotNull(message = "Homme obligatoire")
    @ManyToOne
    @JoinColumn(name = "homme_id", nullable = false, insertable = false, updatable = false)
    private Homme homme;
    @NotNull(message = "Femme obligatoire")
    @ManyToOne
    @JoinColumn(name = "femme_id", nullable = false, insertable = false, updatable = false)
    private Femme femme;
    @Column(name = "date_fin")
    private LocalDate dateFin;
    @Column(name = "nbr_enfant", nullable = false)
    private int nbrEnfant;

    public Mariage() {
    }

    public Mariage(Homme homme, Femme femme, LocalDate dateDebut, LocalDate dateFin, int nbrEnfant) {
        if (homme == null || femme == null || homme.getId() == 0 || femme.getId() == 0) {
            throw new IllegalArgumentException("Enregistrer les personnes avant le mariage");
        }
        this.pk = new MariagePK(homme.getId(), femme.getId(), dateDebut);
        this.homme = homme;
        this.femme = femme;
        this.dateFin = dateFin;
        this.nbrEnfant = nbrEnfant;
    }

    public MariagePK getPk() {
        return pk;
    }

    public void setPk(MariagePK pk) {
        this.pk = pk;
    }

    public Homme getHomme() {
        return homme;
    }

    public void setHomme(Homme homme) {
        this.homme = homme;
    }

    public Femme getFemme() {
        return femme;
    }

    public void setFemme(Femme femme) {
        this.femme = femme;
    }

    public LocalDate getDateDebut() {
        return pk == null ? null : pk.getDateDebut();
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getNbrEnfant() {
        return nbrEnfant;
    }

    public void setNbrEnfant(int nbrEnfant) {
        this.nbrEnfant = nbrEnfant;
    }
}
