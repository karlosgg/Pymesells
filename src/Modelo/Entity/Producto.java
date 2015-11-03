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
@Table(name = "producto", catalog = "pymesell_v1", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findByCodBarra", query = "SELECT p FROM Producto p WHERE p.codBarra = :codBarra"),
    @NamedQuery(name = "Producto.findByNombre", query = "SELECT p FROM Producto p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Producto.modificar", query = "UPDATE Producto p SET p.nombre = :nombre WHERE p.codBarra = :codBarra")})
public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CodBarra", nullable = false, length = 50)
    private String codBarra;
    @Basic(optional = false)
    @Column(name = "Nombre", nullable = false, length = 50)
    private String nombre;
    @OneToMany(mappedBy = "codBarra")
    private List<Inventario> inventarioList;
    @OneToMany(mappedBy = "codBarra")
    private List<Detalleventa> detalleventaList;

    public Producto() {
    }

    public Producto(String codBarra) {
        this.codBarra = codBarra;
    }

    public Producto(String codBarra, String nombre) {
        this.codBarra = codBarra;
        this.nombre = nombre;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Inventario> getInventarioList() {
        return inventarioList;
    }

    public void setInventarioList(List<Inventario> inventarioList) {
        this.inventarioList = inventarioList;
    }

    @XmlTransient
    public List<Detalleventa> getDetalleventaList() {
        return detalleventaList;
    }

    public void setDetalleventaList(List<Detalleventa> detalleventaList) {
        this.detalleventaList = detalleventaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codBarra != null ? codBarra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.codBarra == null && other.codBarra != null) || (this.codBarra != null && !this.codBarra.equals(other.codBarra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Entity.Producto[ codBarra=" + codBarra + " ]";
    }
    
}
