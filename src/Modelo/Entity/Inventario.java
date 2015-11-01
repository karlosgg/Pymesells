/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author José Carlos Grijalva González
 */
@Entity
@Table(name = "inventario", catalog = "pymesell_v1", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inventario.findAll", query = "SELECT i FROM Inventario i"),
    @NamedQuery(name = "Inventario.findByNumParte", query = "SELECT i FROM Inventario i WHERE i.numParte = :numParte"),
    @NamedQuery(name = "Inventario.findByCostoGravado", query = "SELECT i FROM Inventario i WHERE i.costoGravado = :costoGravado")})
public class Inventario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NumParte", nullable = false, length = 50)
    private String numParte;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CostoGravado", precision = 22)
    private Double costoGravado;
    @JoinColumn(name = "CodBarra", referencedColumnName = "CodBarra")
    @ManyToOne
    private Producto codBarra;
    @JoinColumn(name = "IdCompra", referencedColumnName = "IdCompra")
    @ManyToOne
    private Compra idCompra;

    public Inventario() {
    }

    public Inventario(String numParte) {
        this.numParte = numParte;
    }

    public String getNumParte() {
        return numParte;
    }

    public void setNumParte(String numParte) {
        this.numParte = numParte;
    }

    public Double getCostoGravado() {
        return costoGravado;
    }

    public void setCostoGravado(Double costoGravado) {
        this.costoGravado = costoGravado;
    }

    public Producto getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(Producto codBarra) {
        this.codBarra = codBarra;
    }

    public Compra getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Compra idCompra) {
        this.idCompra = idCompra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numParte != null ? numParte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventario)) {
            return false;
        }
        Inventario other = (Inventario) object;
        if ((this.numParte == null && other.numParte != null) || (this.numParte != null && !this.numParte.equals(other.numParte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Entity.Inventario[ numParte=" + numParte + " ]";
    }
    
}
