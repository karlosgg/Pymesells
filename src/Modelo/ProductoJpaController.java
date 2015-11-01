/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Entity.Inventario;
import java.util.ArrayList;
import java.util.List;
import Modelo.Entity.Detalleventa;
import Modelo.Entity.Producto;
import Modelo.exceptions.NonexistentEntityException;
import Modelo.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author José Carlos Grijalva González
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, Exception {
        if (producto.getInventarioList() == null) {
            producto.setInventarioList(new ArrayList<Inventario>());
        }
        if (producto.getDetalleventaList() == null) {
            producto.setDetalleventaList(new ArrayList<Detalleventa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Inventario> attachedInventarioList = new ArrayList<Inventario>();
            for (Inventario inventarioListInventarioToAttach : producto.getInventarioList()) {
                inventarioListInventarioToAttach = em.getReference(inventarioListInventarioToAttach.getClass(), inventarioListInventarioToAttach.getNumParte());
                attachedInventarioList.add(inventarioListInventarioToAttach);
            }
            producto.setInventarioList(attachedInventarioList);
            List<Detalleventa> attachedDetalleventaList = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaListDetalleventaToAttach : producto.getDetalleventaList()) {
                detalleventaListDetalleventaToAttach = em.getReference(detalleventaListDetalleventaToAttach.getClass(), detalleventaListDetalleventaToAttach.getNumParte());
                attachedDetalleventaList.add(detalleventaListDetalleventaToAttach);
            }
            producto.setDetalleventaList(attachedDetalleventaList);
            em.persist(producto);
            for (Inventario inventarioListInventario : producto.getInventarioList()) {
                Producto oldCodBarraOfInventarioListInventario = inventarioListInventario.getCodBarra();
                inventarioListInventario.setCodBarra(producto);
                inventarioListInventario = em.merge(inventarioListInventario);
                if (oldCodBarraOfInventarioListInventario != null) {
                    oldCodBarraOfInventarioListInventario.getInventarioList().remove(inventarioListInventario);
                    oldCodBarraOfInventarioListInventario = em.merge(oldCodBarraOfInventarioListInventario);
                }
            }
            for (Detalleventa detalleventaListDetalleventa : producto.getDetalleventaList()) {
                Producto oldCodBarraOfDetalleventaListDetalleventa = detalleventaListDetalleventa.getCodBarra();
                detalleventaListDetalleventa.setCodBarra(producto);
                detalleventaListDetalleventa = em.merge(detalleventaListDetalleventa);
                if (oldCodBarraOfDetalleventaListDetalleventa != null) {
                    oldCodBarraOfDetalleventaListDetalleventa.getDetalleventaList().remove(detalleventaListDetalleventa);
                    oldCodBarraOfDetalleventaListDetalleventa = em.merge(oldCodBarraOfDetalleventaListDetalleventa);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProducto(producto.getCodBarra()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getCodBarra());
            List<Inventario> inventarioListOld = persistentProducto.getInventarioList();
            List<Inventario> inventarioListNew = producto.getInventarioList();
            List<Detalleventa> detalleventaListOld = persistentProducto.getDetalleventaList();
            List<Detalleventa> detalleventaListNew = producto.getDetalleventaList();
            List<Inventario> attachedInventarioListNew = new ArrayList<Inventario>();
            for (Inventario inventarioListNewInventarioToAttach : inventarioListNew) {
                inventarioListNewInventarioToAttach = em.getReference(inventarioListNewInventarioToAttach.getClass(), inventarioListNewInventarioToAttach.getNumParte());
                attachedInventarioListNew.add(inventarioListNewInventarioToAttach);
            }
            inventarioListNew = attachedInventarioListNew;
            producto.setInventarioList(inventarioListNew);
            List<Detalleventa> attachedDetalleventaListNew = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaListNewDetalleventaToAttach : detalleventaListNew) {
                detalleventaListNewDetalleventaToAttach = em.getReference(detalleventaListNewDetalleventaToAttach.getClass(), detalleventaListNewDetalleventaToAttach.getNumParte());
                attachedDetalleventaListNew.add(detalleventaListNewDetalleventaToAttach);
            }
            detalleventaListNew = attachedDetalleventaListNew;
            producto.setDetalleventaList(detalleventaListNew);
            producto = em.merge(producto);
            for (Inventario inventarioListOldInventario : inventarioListOld) {
                if (!inventarioListNew.contains(inventarioListOldInventario)) {
                    inventarioListOldInventario.setCodBarra(null);
                    inventarioListOldInventario = em.merge(inventarioListOldInventario);
                }
            }
            for (Inventario inventarioListNewInventario : inventarioListNew) {
                if (!inventarioListOld.contains(inventarioListNewInventario)) {
                    Producto oldCodBarraOfInventarioListNewInventario = inventarioListNewInventario.getCodBarra();
                    inventarioListNewInventario.setCodBarra(producto);
                    inventarioListNewInventario = em.merge(inventarioListNewInventario);
                    if (oldCodBarraOfInventarioListNewInventario != null && !oldCodBarraOfInventarioListNewInventario.equals(producto)) {
                        oldCodBarraOfInventarioListNewInventario.getInventarioList().remove(inventarioListNewInventario);
                        oldCodBarraOfInventarioListNewInventario = em.merge(oldCodBarraOfInventarioListNewInventario);
                    }
                }
            }
            for (Detalleventa detalleventaListOldDetalleventa : detalleventaListOld) {
                if (!detalleventaListNew.contains(detalleventaListOldDetalleventa)) {
                    detalleventaListOldDetalleventa.setCodBarra(null);
                    detalleventaListOldDetalleventa = em.merge(detalleventaListOldDetalleventa);
                }
            }
            for (Detalleventa detalleventaListNewDetalleventa : detalleventaListNew) {
                if (!detalleventaListOld.contains(detalleventaListNewDetalleventa)) {
                    Producto oldCodBarraOfDetalleventaListNewDetalleventa = detalleventaListNewDetalleventa.getCodBarra();
                    detalleventaListNewDetalleventa.setCodBarra(producto);
                    detalleventaListNewDetalleventa = em.merge(detalleventaListNewDetalleventa);
                    if (oldCodBarraOfDetalleventaListNewDetalleventa != null && !oldCodBarraOfDetalleventaListNewDetalleventa.equals(producto)) {
                        oldCodBarraOfDetalleventaListNewDetalleventa.getDetalleventaList().remove(detalleventaListNewDetalleventa);
                        oldCodBarraOfDetalleventaListNewDetalleventa = em.merge(oldCodBarraOfDetalleventaListNewDetalleventa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = producto.getCodBarra();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getCodBarra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<Inventario> inventarioList = producto.getInventarioList();
            for (Inventario inventarioListInventario : inventarioList) {
                inventarioListInventario.setCodBarra(null);
                inventarioListInventario = em.merge(inventarioListInventario);
            }
            List<Detalleventa> detalleventaList = producto.getDetalleventaList();
            for (Detalleventa detalleventaListDetalleventa : detalleventaList) {
                detalleventaListDetalleventa.setCodBarra(null);
                detalleventaListDetalleventa = em.merge(detalleventaListDetalleventa);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Producto findProducto(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
