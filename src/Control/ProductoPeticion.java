/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Modelo.Entity.Producto;
import Modelo.ProductoJpaController;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Jose Carlos Grijalva
 */
public class ProductoPeticion {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SistemaVentas_V1PU");
    private final EntityManager em = emf.createEntityManager();
    private final ProductoJpaController servicio = new ProductoJpaController(emf);
    private List<Producto> productos;
    private Producto producto;
    
    public ProductoPeticion() {
        
    }
    
    public void crearProducto(String cod, String nom){
        
        producto=new Producto();
        em.getTransaction().begin();
        producto.setCodBarra(cod);
        producto.setNombre(nom);
        try {
            servicio.create(producto);
            em.getTransaction().commit();
            
        } catch (Exception ex) {
            Logger.getLogger(ProductoPeticion.class.getName()).log(Level.SEVERE, null, ex);
            em.getTransaction().rollback();
            System.out.println("Erro, no se pudo crear ->" + producto);
        }
        
        em.close();
        emf.close();
       
    }
    public List<Producto> obtenerProductos(){
        em.getTransaction().begin();
        productos=servicio.findProductoEntities();
        em.close();
        emf.close();
        return productos;
    }
    
    
}
