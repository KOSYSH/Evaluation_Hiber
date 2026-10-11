package ma.projet.beans;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class MariagePK implements Serializable {
    @Column(name = "homme_id")
    private int hommeId;
    @Column(name = "femme_id")
    private int femmeId;
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    public MariagePK() {
    }

    public MariagePK(int hommeId, int femmeId, LocalDate dateDebut) {
        this.hommeId = hommeId;
        this.femmeId = femmeId;
        this.dateDebut = dateDebut;
    }

    public int getHommeId() {
        return hommeId;
    }

    public void setHommeId(int hommeId) {
        this.hommeId = hommeId;
    }

    public int getFemmeId() {
        return femmeId;
    }

    public void setFemmeId(int femmeId) {
        this.femmeId = femmeId;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof MariagePK)) return false;
        MariagePK other = (MariagePK) object;
        return hommeId == other.hommeId && femmeId == other.femmeId
                && Objects.equals(dateDebut, other.dateDebut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hommeId, femmeId, dateDebut);
    }
}
