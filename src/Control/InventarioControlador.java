/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;
import Modelo.Entity.Inventario;
import Modelo.InventarioJpaController;
import Modelo.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
/**
 *
 * @author José Carlos Grijalva González
 */
public class InventarioControlador {

    //Variables necesarias para el manejo de entidades y proveedores
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SistemaVentas_V1PU");
    private final EntityManager em = emf.createEntityManager();
    private final InventarioJpaController ci = new InventarioJpaController(emf);

    public InventarioControlador() {
    }

    public void Agregar(Inventario inv) throws Exception{
        ci.create(inv);
    }
    
   public void Modificar(Inventario inv) throws Exception{
        em.getTransaction().begin();
        Query q = em.createNamedQuery("Inventario.modificar").setParameter("numParte", inv.getNumParte())
                .setParameter("codBarra", inv.getCodBarra()).setParameter("idCompra", inv.getIdCompra())
                .setParameter("CostoGravado", inv.getCostoGravado());
        q.executeUpdate();
        em.getTransaction().commit();
        em.close();
        emf.close();
    }
    
    public void Eliminar(String id) throws NonexistentEntityException{
        ci.destroy(id);
    }
    
    public Inventario Obtener(String id){
        Inventario inventario = ci.findInventario(id);
        return inventario;
    }
    
    public String ObtenerNumParte(String id){
        id = ci.findInventario(id).getNumParte();
        return id;
    }
}
