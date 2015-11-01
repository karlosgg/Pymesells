/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author José Carlos Grijalva González
 */
@Entity
@Table(name = "tipoventa", catalog = "pymesell_v1", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipoventa.findAll", query = "SELECT t FROM Tipoventa t"),
    @NamedQuery(name = "Tipoventa.findByIdTipoVenta", query = "SELECT t FROM Tipoventa t WHERE t.idTipoVenta = :idTipoVenta"),
    @NamedQuery(name = "Tipoventa.findByTipoVenta", query = "SELECT t FROM Tipoventa t WHERE t.tipoVenta = :tipoVenta")})
public class Tipoventa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IdTipoVenta", nullable = false, length = 50)
    private String idTipoVenta;
    @Column(name = "TipoVenta", length = 50)
    private String tipoVenta;
    @OneToMany(mappedBy = "idTipoVenta")
    private List<Venta> ventaList;

    public Tipoventa() {
    }

    public Tipoventa(String idTipoVenta) {
        this.idTipoVenta = idTipoVenta;
    }

    public String getIdTipoVenta() {
        return idTipoVenta;
    }

    public void setIdTipoVenta(String idTipoVenta) {
        this.idTipoVenta = idTipoVenta;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    @XmlTransient
    public List<Venta> getVentaList() {
        return ventaList;
    }

    public void setVentaList(List<Venta> ventaList) {
        this.ventaList = ventaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoVenta != null ? idTipoVenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoventa)) {
            return false;
        }
        Tipoventa other = (Tipoventa) object;
        if ((this.idTipoVenta == null && other.idTipoVenta != null) || (this.idTipoVenta != null && !this.idTipoVenta.equals(other.idTipoVenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Entity.Tipoventa[ idTipoVenta=" + idTipoVenta + " ]";
    }
    
}
