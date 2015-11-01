/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Modelo.Entity.Compra;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Entity.Proveedor;
import Modelo.Entity.Inventario;
import Modelo.exceptions.NonexistentEntityException;
import Modelo.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author José Carlos Grijalva González
 */
public class CompraJpaController implements Serializable {

    public CompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compra compra) throws PreexistingEntityException, Exception {
        if (compra.getInventarioList() == null) {
            compra.setInventarioList(new ArrayList<Inventario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor idProveedor = compra.getIdProveedor();
            if (idProveedor != null) {
                idProveedor = em.getReference(idProveedor.getClass(), idProveedor.getIdProveedor());
                compra.setIdProveedor(idProveedor);
            }
            List<Inventario> attachedInventarioList = new ArrayList<Inventario>();
            for (Inventario inventarioListInventarioToAttach : compra.getInventarioList()) {
                inventarioListInventarioToAttach = em.getReference(inventarioListInventarioToAttach.getClass(), inventarioListInventarioToAttach.getNumParte());
                attachedInventarioList.add(inventarioListInventarioToAttach);
            }
            compra.setInventarioList(attachedInventarioList);
            em.persist(compra);
            if (idProveedor != null) {
                idProveedor.getCompraList().add(compra);
                idProveedor = em.merge(idProveedor);
            }
            for (Inventario inventarioListInventario : compra.getInventarioList()) {
                Compra oldIdCompraOfInventarioListInventario = inventarioListInventario.getIdCompra();
                inventarioListInventario.setIdCompra(compra);
                inventarioListInventario = em.merge(inventarioListInventario);
                if (oldIdCompraOfInventarioListInventario != null) {
                    oldIdCompraOfInventarioListInventario.getInventarioList().remove(inventarioListInventario);
                    oldIdCompraOfInventarioListInventario = em.merge(oldIdCompraOfInventarioListInventario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCompra(compra.getIdCompra()) != null) {
                throw new PreexistingEntityException("Compra " + compra + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Compra compra) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra persistentCompra = em.find(Compra.class, compra.getIdCompra());
            Proveedor idProveedorOld = persistentCompra.getIdProveedor();
            Proveedor idProveedorNew = compra.getIdProveedor();
            List<Inventario> inventarioListOld = persistentCompra.getInventarioList();
            List<Inventario> inventarioListNew = compra.getInventarioList();
            if (idProveedorNew != null) {
                idProveedorNew = em.getReference(idProveedorNew.getClass(), idProveedorNew.getIdProveedor());
                compra.setIdProveedor(idProveedorNew);
            }
            List<Inventario> attachedInventarioListNew = new ArrayList<Inventario>();
            for (Inventario inventarioListNewInventarioToAttach : inventarioListNew) {
                inventarioListNewInventarioToAttach = em.getReference(inventarioListNewInventarioToAttach.getClass(), inventarioListNewInventarioToAttach.getNumParte());
                attachedInventarioListNew.add(inventarioListNewInventarioToAttach);
            }
            inventarioListNew = attachedInventarioListNew;
            compra.setInventarioList(inventarioListNew);
            compra = em.merge(compra);
            if (idProveedorOld != null && !idProveedorOld.equals(idProveedorNew)) {
                idProveedorOld.getCompraList().remove(compra);
                idProveedorOld = em.merge(idProveedorOld);
            }
            if (idProveedorNew != null && !idProveedorNew.equals(idProveedorOld)) {
                idProveedorNew.getCompraList().add(compra);
                idProveedorNew = em.merge(idProveedorNew);
            }
            for (Inventario inventarioListOldInventario : inventarioListOld) {
                if (!inventarioListNew.contains(inventarioListOldInventario)) {
                    inventarioListOldInventario.setIdCompra(null);
                    inventarioListOldInventario = em.merge(inventarioListOldInventario);
                }
            }
            for (Inventario inventarioListNewInventario : inventarioListNew) {
                if (!inventarioListOld.contains(inventarioListNewInventario)) {
                    Compra oldIdCompraOfInventarioListNewInventario = inventarioListNewInventario.getIdCompra();
                    inventarioListNewInventario.setIdCompra(compra);
                    inventarioListNewInventario = em.merge(inventarioListNewInventario);
                    if (oldIdCompraOfInventarioListNewInventario != null && !oldIdCompraOfInventarioListNewInventario.equals(compra)) {
                        oldIdCompraOfInventarioListNewInventario.getInventarioList().remove(inventarioListNewInventario);
                        oldIdCompraOfInventarioListNewInventario = em.merge(oldIdCompraOfInventarioListNewInventario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = compra.getIdCompra();
                if (findCompra(id) == null) {
                    throw new NonexistentEntityException("The compra with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra compra;
            try {
                compra = em.getReference(Compra.class, id);
                compra.getIdCompra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compra with id " + id + " no longer exists.", enfe);
            }
            Proveedor idProveedor = compra.getIdProveedor();
            if (idProveedor != null) {
                idProveedor.getCompraList().remove(compra);
                idProveedor = em.merge(idProveedor);
            }
            List<Inventario> inventarioList = compra.getInventarioList();
            for (Inventario inventarioListInventario : inventarioList) {
                inventarioListInventario.setIdCompra(null);
                inventarioListInventario = em.merge(inventarioListInventario);
            }
            em.remove(compra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Compra> findCompraEntities() {
        return findCompraEntities(true, -1, -1);
    }

    public List<Compra> findCompraEntities(int maxResults, int firstResult) {
        return findCompraEntities(false, maxResults, firstResult);
    }

    private List<Compra> findCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compra.class));
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

    public Compra findCompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compra.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compra> rt = cq.from(Compra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
