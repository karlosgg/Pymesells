/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author José Carlos Grijalva González
 */
@Entity
@Table(name = "totalmensual", catalog = "pymesell_v1", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Totalmensual.findAll", query = "SELECT t FROM Totalmensual t"),
    @NamedQuery(name = "Totalmensual.findByAnio", query = "SELECT t FROM Totalmensual t WHERE t.totalmensualPK.anio = :anio"),
    @NamedQuery(name = "Totalmensual.findByMes", query = "SELECT t FROM Totalmensual t WHERE t.totalmensualPK.mes = :mes"),
    @NamedQuery(name = "Totalmensual.findByCompraGravada", query = "SELECT t FROM Totalmensual t WHERE t.compraGravada = :compraGravada"),
    @NamedQuery(name = "Totalmensual.findByPagoACuenta", query = "SELECT t FROM Totalmensual t WHERE t.pagoACuenta = :pagoACuenta"),
    @NamedQuery(name = "Totalmensual.findByIva", query = "SELECT t FROM Totalmensual t WHERE t.iva = :iva"),
    @NamedQuery(name = "Totalmensual.findByRenta", query = "SELECT t FROM Totalmensual t WHERE t.renta = :renta"),
    @NamedQuery(name = "Totalmensual.findByUtilidad", query = "SELECT t FROM Totalmensual t WHERE t.utilidad = :utilidad"),
    @NamedQuery(name = "Totalmensual.findByVentaGravada", query = "SELECT t FROM Totalmensual t WHERE t.ventaGravada = :ventaGravada"),
    @NamedQuery(name = "Totalmensual.findByIVACompra", query = "SELECT t FROM Totalmensual t WHERE t.iVACompra = :iVACompra"),
    @NamedQuery(name = "Totalmensual.findByPercepcion", query = "SELECT t FROM Totalmensual t WHERE t.percepcion = :percepcion")})
public class Totalmensual implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TotalmensualPK totalmensualPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CompraGravada", precision = 22)
    private Double compraGravada;
    @Column(name = "PagoACuenta", precision = 22)
    private Double pagoACuenta;
    @Column(name = "IVA", precision = 22)
    private Double iva;
    @Column(name = "Renta", precision = 22)
    private Double renta;
    @Column(name = "Utilidad", precision = 22)
    private Double utilidad;
    @Column(name = "VentaGravada", precision = 22)
    private Double ventaGravada;
    @Column(name = "IVACompra", precision = 22)
    private Double iVACompra;
    @Column(name = "Percepcion", precision = 22)
    private Double percepcion;

    public Totalmensual() {
    }

    public Totalmensual(TotalmensualPK totalmensualPK) {
        this.totalmensualPK = totalmensualPK;
    }

    public Totalmensual(int anio, String mes) {
        this.totalmensualPK = new TotalmensualPK(anio, mes);
    }

    public TotalmensualPK getTotalmensualPK() {
        return totalmensualPK;
    }

    public void setTotalmensualPK(TotalmensualPK totalmensualPK) {
        this.totalmensualPK = totalmensualPK;
    }

    public Double getCompraGravada() {
        return compraGravada;
    }

    public void setCompraGravada(Double compraGravada) {
        this.compraGravada = compraGravada;
    }

    public Double getPagoACuenta() {
        return pagoACuenta;
    }

    public void setPagoACuenta(Double pagoACuenta) {
        this.pagoACuenta = pagoACuenta;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
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

    public Double getVentaGravada() {
        return ventaGravada;
    }

    public void setVentaGravada(Double ventaGravada) {
        this.ventaGravada = ventaGravada;
    }

    public Double getIVACompra() {
        return iVACompra;
    }

    public void setIVACompra(Double iVACompra) {
        this.iVACompra = iVACompra;
    }

    public Double getPercepcion() {
        return percepcion;
    }

    public void setPercepcion(Double percepcion) {
        this.percepcion = percepcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (totalmensualPK != null ? totalmensualPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Totalmensual)) {
            return false;
        }
        Totalmensual other = (Totalmensual) object;
        if ((this.totalmensualPK == null && other.totalmensualPK != null) || (this.totalmensualPK != null && !this.totalmensualPK.equals(other.totalmensualPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Entity.Totalmensual[ totalmensualPK=" + totalmensualPK + " ]";
    }
    
}
