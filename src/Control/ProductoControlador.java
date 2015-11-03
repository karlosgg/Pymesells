/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;
import Modelo.ProductoJpaController;
import Modelo.Entity.Producto;
import Modelo.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
/**
 *
 * @author José Carlos Grijalva González
 */
public class ProductoControlador {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SistemaVentas_V1PU");
    private final ProductoJpaController pc = new ProductoJpaController(emf);
    private EntityManager em = emf.createEntityManager();
    
    public ProductoControlador() {
    }
    
    public void Agregar(Producto prod) throws Exception{
        pc.create(prod);
    }
    
    public List<Producto> ObtenerProductos(){
        List productos;
        productos = pc.findProductoEntities();
        return productos;
    }
    
    public Producto Obtener(String id){
        Producto producto = pc.findProducto(id);
        return producto;
    }
    
    public void Modificar(Producto prod) throws Exception{
        em.getTransaction().begin();
        Query q = em.createNamedQuery("Producto.modificar").setParameter("nombre", prod.getNombre())
                .setParameter("codBarra", prod.getCodBarra());
        q.executeUpdate();
        em.getTransaction().commit();
        em.close();
        emf.close();
    }
    
    public void Eliminar(String id) throws NonexistentEntityException{
        pc.destroy(id);
    }
    
}
