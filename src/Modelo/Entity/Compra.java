/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author José Carlos Grijalva González
 */
@Entity
@Table(name = "compra", catalog = "pymesell_v1", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Compra.findAll", query = "SELECT c FROM Compra c"),
    @NamedQuery(name = "Compra.findByIdCompra", query = "SELECT c FROM Compra c WHERE c.idCompra = :idCompra"),
    @NamedQuery(name = "Compra.findByNumDocumento", query = "SELECT c FROM Compra c WHERE c.numDocumento = :numDocumento"),
    @NamedQuery(name = "Compra.findByFecha", query = "SELECT c FROM Compra c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "Compra.findByGravadoCompra", query = "SELECT c FROM Compra c WHERE c.gravadoCompra = :gravadoCompra"),
    @NamedQuery(name = "Compra.findByIva", query = "SELECT c FROM Compra c WHERE c.iva = :iva"),
    @NamedQuery(name = "Compra.findByPercepcion", query = "SELECT c FROM Compra c WHERE c.percepcion = :percepcion"),
    @NamedQuery(name = "Compra.findByRangoFecha", query = "SELECT c FROM Compra c WHERE c.fecha < :fecha2 AND c.fecha > :fecha1")})
public class Compra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IdCompra", nullable = false)
    private Integer idCompra;
    @Column(name = "NumDocumento")
    private Integer numDocumento;
    @Column(name = "Fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "GravadoCompra", precision = 22)
    private Double gravadoCompra;
    @Column(name = "IVA", precision = 22)
    private Double iva;
    @Column(name = "Percepcion", precision = 22)
    private Double percepcion;
    @JoinColumn(name = "IdProveedor", referencedColumnName = "IdProveedor")
    @ManyToOne
    private Proveedor idProveedor;
    @OneToMany(mappedBy = "idCompra")
    private List<Inventario> inventarioList;

    public Compra() {
    }

    public Compra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public Integer getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(Integer numDocumento) {
        this.numDocumento = numDocumento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getGravadoCompra() {
        return gravadoCompra;
    }

    public void setGravadoCompra(Double gravadoCompra) {
        this.gravadoCompra = gravadoCompra;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getPercepcion() {
        return percepcion;
    }

    public void setPercepcion(Double percepcion) {
        this.percepcion = percepcion;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedor idProveedor) {
        this.idProveedor = idProveedor;
    }

    @XmlTransient
    public List<Inventario> getInventarioList() {
        return inventarioList;
    }

    public void setInventarioList(List<Inventario> inventarioList) {
        this.inventarioList = inventarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompra != null ? idCompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compra)) {
            return false;
        }
        Compra other = (Compra) object;
        if ((this.idCompra == null && other.idCompra != null) || (this.idCompra != null && !this.idCompra.equals(other.idCompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Entity.Compra[ idCompra=" + idCompra + " ]";
    }
    
}
