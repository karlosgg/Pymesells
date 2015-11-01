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
@Table(name = "venta", catalog = "pymesell_v1", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v"),
    @NamedQuery(name = "Venta.findByIdVenta", query = "SELECT v FROM Venta v WHERE v.idVenta = :idVenta"),
    @NamedQuery(name = "Venta.findByNumComprobante", query = "SELECT v FROM Venta v WHERE v.numComprobante = :numComprobante"),
    @NamedQuery(name = "Venta.findByFecha", query = "SELECT v FROM Venta v WHERE v.fecha = :fecha"),
    @NamedQuery(name = "Venta.findByGravadoVenta", query = "SELECT v FROM Venta v WHERE v.gravadoVenta = :gravadoVenta"),
    @NamedQuery(name = "Venta.findByIva", query = "SELECT v FROM Venta v WHERE v.iva = :iva"),
    @NamedQuery(name = "Venta.findByPagoACuenta", query = "SELECT v FROM Venta v WHERE v.pagoACuenta = :pagoACuenta"),
    @NamedQuery(name = "Venta.findByRenta", query = "SELECT v FROM Venta v WHERE v.renta = :renta"),
    @NamedQuery(name = "Venta.findByUtilidad", query = "SELECT v FROM Venta v WHERE v.utilidad = :utilidad"),
    @NamedQuery(name = "Venta.findByNombreCliente", query = "SELECT v FROM Venta v WHERE v.nombreCliente = :nombreCliente")})
public class Venta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IdVenta", nullable = false)
    private Integer idVenta;
    @Column(name = "NumComprobante")
    private Integer numComprobante;
    @Column(name = "Fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "GravadoVenta", precision = 22)
    private Double gravadoVenta;
    @Column(name = "IVA", precision = 22)
    private Double iva;
    @Column(name = "PagoACuenta", precision = 22)
    private Double pagoACuenta;
    @Column(name = "Renta", precision = 22)
    private Double renta;
    @Column(name = "Utilidad", precision = 22)
    private Double utilidad;
    @Column(name = "NombreCliente", length = 50)
    private String nombreCliente;
    @OneToMany(mappedBy = "idVenta")
    private List<Detalleventa> detalleventaList;
    @JoinColumn(name = "IdTipoVenta", referencedColumnName = "IdTipoVenta")
    @ManyToOne
    private Tipoventa idTipoVenta;
    @JoinColumn(name = "IdCliente", referencedColumnName = "IdCliente")
    @ManyToOne
    private Cliente idCliente;

    public Venta() {
    }

    public Venta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Integer getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(Integer numComprobante) {
        this.numComprobante = numComprobante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getGravadoVenta() {
        return gravadoVenta;
    }

    public void setGravadoVenta(Double gravadoVenta) {
        this.gravadoVenta = gravadoVenta;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getPagoACuenta() {
        return pagoACuenta;
    }

    public void setPagoACuenta(Double pagoACuenta) {
        this.pagoACuenta = pagoACuenta;
    }

    public Double getRenta() {
        return renta;
    }

    public void setRenta(Double renta) {
        this.renta = renta;
    }

    public Double getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(Double utilidad) {
        this.utilidad = utilidad;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    @XmlTransient
    public List<Detalleventa> getDetalleventaList() {
        return detalleventaList;
    }

    public void setDetalleventaList(List<Detalleventa> detalleventaList) {
        this.detalleventaList = detalleventaList;
    }

    public Tipoventa getIdTipoVenta() {
        return idTipoVenta;
    }

    public void setIdTipoVenta(Tipoventa idTipoVenta) {
        this.idTipoVenta = idTipoVenta;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVenta != null ? idVenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.idVenta == null && other.idVenta != null) || (this.idVenta != null && !this.idVenta.equals(other.idVenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Entity.Venta[ idVenta=" + idVenta + " ]";
    }
    
}
