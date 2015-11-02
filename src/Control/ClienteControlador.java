/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;
import Modelo.Entity.Cliente;
import Modelo.ClienteJpaController;
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
public class ClienteControlador {

    private String Nombre="";
    private String Nit="";
    private String Nrc="";
    private List<Cliente> Clientes;
    private Cliente Cliente;
    //Variables necesarias para el manejo de entidades y clientes
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SistemaVentas_V1PU");
    private final EntityManager em = emf.createEntityManager();
    private final ClienteJpaController servicio = new ClienteJpaController(emf);

    //Constructor
    public ClienteControlador() {

    }
    
    //Obtiene todos los clientes
    public List<Cliente> Obtener(String parametro){
        //Busca todos los elementos
        if(parametro.isEmpty()){
            em.getTransaction().begin();
            Clientes=servicio.findClienteEntities();
            em.close();
            emf.close();
        }
        else{//Busqueda con parametro
            
        }
        return Clientes;
    }
    
    //Busca cliente por el nombre
    public List<Cliente> BuscarXNombre() throws ParseException{
        em.getTransaction().begin();
        Query q = em.createNamedQuery("Cliente.findByNombre").setParameter("nombre", '%' +Nombre+ '%');
        if(!q.getResultList().isEmpty()){
            Clientes = q.getResultList();
        }else{
            Clientes=null;
        }
        em.close();
        emf.close();

        return Clientes;
    }
    
    //Busca cliente por el nit
    public Cliente BuscarXNit(){
        em.getTransaction().begin();
        Query q = em.createNamedQuery("Cliente.findByNit").setParameter("nit", Nit);
        if(!q.getResultList().isEmpty()){
        Cliente = (Cliente) q.getSingleResult();
        }else{
            Cliente=null;
        }
        em.close();
        emf.close();

        return Cliente;
    }
    
    //Busca cliente por el nrc
    public Cliente BuscarXNrc(){
        em.getTransaction().begin();
        Query q = em.createNamedQuery("Cliente.findByNrc").setParameter("nrc", Nrc);
        if(!q.getResultList().isEmpty()){
        Cliente = (Cliente) q.getSingleResult();
        }else{
            Cliente=null;
        }
        em.close();
        emf.close();

        return Cliente;
    }
   
    
    //INSERTAR CLIENTES
    public void Agregar(Cliente cli){
        try {
            Cliente=new Cliente();
            Cliente=cli;
            em.getTransaction().begin();
            servicio.create(Cliente);
            em.getTransaction().commit();
            em.close();
            emf.close();
        } catch (Exception ex) {
            Logger.getLogger(ClienteControlador.class.getName()).log(Level.SEVERE, null, ex);
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
