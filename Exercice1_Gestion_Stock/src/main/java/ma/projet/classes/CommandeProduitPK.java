package ma.projet.classes;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CommandeProduitPK implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "commande_id")
    private int commandeId;
    @Column(name = "produit_id")
    private int produitId;

    public CommandeProduitPK() {
    }

    public CommandeProduitPK(int commandeId, int produitId) {
        this.commandeId = commandeId;
        this.produitId = produitId;
    }

    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommandeProduitPK that = (CommandeProduitPK) o;
        return commandeId == that.commandeId && produitId == that.produitId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandeId, produitId);
    }
}
