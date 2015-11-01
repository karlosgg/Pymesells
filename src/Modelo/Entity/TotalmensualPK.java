/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author José Carlos Grijalva González
 */
@Embeddable
public class TotalmensualPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "Anio", nullable = false)
    private int anio;
    @Basic(optional = false)
    @Column(name = "Mes", nullable = false, length = 50)
    private String mes;

    public TotalmensualPK() {
    }

    public TotalmensualPK(int anio, String mes) {
        this.anio = anio;
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) anio;
        hash += (mes != null ? mes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TotalmensualPK)) {
            return false;
        }
        TotalmensualPK other = (TotalmensualPK) object;
        if (this.anio != other.anio) {
            return false;
        }
        if ((this.mes == null && other.mes != null) || (this.mes != null && !this.mes.equals(other.mes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Entity.TotalmensualPK[ anio=" + anio + ", mes=" + mes + " ]";
    }
    
}
