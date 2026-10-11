package ma.projet.classes;

import javax.persistence.Embeddable;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmployeTachePK implements Serializable {
    @Column(name = "employe_id")
    private int employeId;
    @Column(name = "tache_id")
    private int tacheId;

    public EmployeTachePK() {
    }

    public EmployeTachePK(int employeId, int tacheId) {
        this.employeId = employeId;
        this.tacheId = tacheId;
    }

    public int getEmployeId() { return employeId; }
    public void setEmployeId(int employeId) { this.employeId = employeId; }
    public int getTacheId() { return tacheId; }
    public void setTacheId(int tacheId) { this.tacheId = tacheId; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof EmployeTachePK)) return false;
        EmployeTachePK other = (EmployeTachePK) object;
        return Objects.equals(employeId, other.employeId) && Objects.equals(tacheId, other.tacheId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeId, tacheId);
    }
}
