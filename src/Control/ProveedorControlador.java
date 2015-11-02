/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;
import Modelo.Entity.Proveedor;
import Modelo.ProveedorJpaController;
import Modelo.exceptions.NonexistentEntityException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
/**
 *
 * @author José Carlos Grijalva González
 */
public class ProveedorControlador {
    private String Nombre="";
    private String Nit="";
    private String Nrc="";
    private List<Proveedor> Proveedores;
    private Proveedor Proveedor;
    //Variables necesarias para el manejo de entidades y proveedores
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SistemaVentas_V1PU");
    private final EntityManager em = emf.createEntityManager();
    private final ProveedorJpaController servicio = new ProveedorJpaController(emf);

    //Constructor
    public ProveedorControlador() {

    }
    
    //Obtiene todos los proveedores
    public List<Proveedor> Obtener(String parametro){
        //Busca todos los elementos
        if(parametro.isEmpty()){
            em.getTransaction().begin();
            Proveedores=servicio.findProveedorEntities();
            em.close();
            emf.close();
        }
        else{//Busqueda con parametro
            
        }
        return Proveedores;
    }
    
    //Busca Proveedor por el nombre
    public List<Proveedor> BuscarXNombre() throws ParseException{
        em.getTransaction().begin();
        Query q = em.createNamedQuery("Proveedor.findByNombre").setParameter("nombre", '%' +Nombre+ '%');
        if(!q.getResultList().isEmpty()){
            Proveedores = q.getResultList();
        }else{
            Proveedores=null;
        }
        em.close();
        emf.close();

        return Proveedores;
    }
    
    //Busca Proveedor por el nit
    public Proveedor BuscarXNit(){
        em.getTransaction().begin();
        Query q = em.createNamedQuery("Proveedor.findByNit").setParameter("nit", Nit);
        if(!q.getResultList().isEmpty()){
        Proveedor = (Proveedor) q.getSingleResult();
        }else{
            Proveedor=null;
        }
        em.close();
        emf.close();

        return Proveedor;
    }
    
    //Busca Proveedor por el nrc
    public Proveedor BuscarXNrc(){
        em.getTransaction().begin();
        Query q = em.createNamedQuery("Proveedor.findByNrc").setParameter("nrc", Nrc);
        if(!q.getResultList().isEmpty()){
        Proveedor = (Proveedor) q.getSingleResult();
        }else{
            Proveedor=null;
        }
        em.close();
        emf.close();

        return Proveedor;
    }
   
    
    //INSERTAR PROVEEDORES
    public void Agregar(Proveedor pro){
        try {
            Proveedor=new Proveedor();
            Proveedor=pro;
            em.getTransaction().begin();
            servicio.create(Proveedor);
            em.getTransaction().commit();
            em.close();
            emf.close();
        } catch (Exception ex) {
            Logger.getLogger(ProveedorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Eliminar Proveedor
    public void Eliminar(String id){
        try {
            em.getTransaction().begin();
            servicio.destroy(id);
            em.getTransaction().commit();
            em.close();
            emf.close();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ProveedorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Modificar Proveedor
    public void Modificar(Proveedor pr){
        try {
            em.getTransaction().begin();
            Query q = em.createNamedQuery("Proveedor.modificar").setParameter("nombre", pr.getNombre())
                    .setParameter("nrc", pr.getNrc()).setParameter("giro", pr.getGiro())
                    .setParameter("direccion", pr.getDireccion()).setParameter("nit", pr.getNit())
                    .setParameter("telefono", pr.getTelefono()).setParameter("idProveedor", pr.getIdProveedor());
            q.executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        } catch (Exception ex) {
            Logger.getLogger(ProveedorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
   
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getNit() {
        return Nit;
    }

    public void setNit(String Nit) {
        this.Nit = Nit;
    }

    public String getNrc() {
        return Nrc;
    }

    public void setNrc(String Nrc) {
        this.Nrc = Nrc;
    }
}
