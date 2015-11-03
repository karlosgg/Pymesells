/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;
import Modelo.CompraJpaController;
import Modelo.Entity.Compra;
import Modelo.exceptions.NonexistentEntityException;
import java.sql.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
/**
 *
 * @author José Carlos Grijalva González
 */
public class CompraControlador {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SistemaVentas_V1PU");
    private final EntityManager em = emf.createEntityManager();
    private final CompraJpaController cc= new CompraJpaController(emf);
    public CompraControlador() {
    }
    
    public void Agregar(Compra com) throws Exception{
        cc.create(com);
    }
    
    public void Modificar(Compra com) throws Exception{
        cc.edit(com);
    }
    
    public void Eliminar(Compra com) throws NonexistentEntityException{
        cc.destroy(com.getIdCompra());
    }
    
    public Compra ObtenerCompra(int id){
        Compra compra = cc.findCompra(id);
        return compra;
    }
    
    public List<Compra> Obtener(){
        List compras = cc.findCompraEntities();
        return compras;
    }
    
    public List<Compra> Obtener(Date fecha) throws Exception{
        em.getTransaction().begin();
        Query q = em.createNamedQuery("Compra.findByFecha").setParameter("fecha", fecha);
        q.executeUpdate();
        List compras = q.getResultList();
        em.getTransaction().commit();
        em.close();
        emf.close();
        return compras;
    }
    
    public List<Compra> Obtener(Date fecha1, Date fecha2) throws Exception{
        em.getTransaction().begin();
        Query q = em.createNamedQuery("Compra.findByRangoFecha").setParameter("fecha1", fecha1)
                .setParameter("fecha2", fecha2);
        q.executeUpdate();
        List compras = q.getResultList();
        em.getTransaction().commit();
        em.close();
        emf.close();
        return compras;
    }
    
}
