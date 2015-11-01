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
import Modelo.Entity.Producto;
import Modelo.Entity.Compra;
import Modelo.Entity.Inventario;
import Modelo.exceptions.NonexistentEntityException;
import Modelo.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author José Carlos Grijalva González
 */
public class InventarioJpaController implements Serializable {

    public InventarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Inventario inventario) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto codBarra = inventario.getCodBarra();
            if (codBarra != null) {
                codBarra = em.getReference(codBarra.getClass(), codBarra.getCodBarra());
                inventario.setCodBarra(codBarra);
            }
            Compra idCompra = inventario.getIdCompra();
            if (idCompra != null) {
                idCompra = em.getReference(idCompra.getClass(), idCompra.getIdCompra());
                inventario.setIdCompra(idCompra);
            }
            em.persist(inventario);
            if (codBarra != null) {
                codBarra.getInventarioList().add(inventario);
                codBarra = em.merge(codBarra);
            }
            if (idCompra != null) {
                idCompra.getInventarioList().add(inventario);
                idCompra = em.merge(idCompra);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findInventario(inventario.getNumParte()) != null) {
                throw new PreexistingEntityException("Inventario " + inventario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Inventario inventario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Inventario persistentInventario = em.find(Inventario.class, inventario.getNumParte());
            Producto codBarraOld = persistentInventario.getCodBarra();
            Producto codBarraNew = inventario.getCodBarra();
            Compra idCompraOld = persistentInventario.getIdCompra();
            Compra idCompraNew = inventario.getIdCompra();
            if (codBarraNew != null) {
                codBarraNew = em.getReference(codBarraNew.getClass(), codBarraNew.getCodBarra());
                inventario.setCodBarra(codBarraNew);
            }
            if (idCompraNew != null) {
                idCompraNew = em.getReference(idCompraNew.getClass(), idCompraNew.getIdCompra());
                inventario.setIdCompra(idCompraNew);
            }
            inventario = em.merge(inventario);
            if (codBarraOld != null && !codBarraOld.equals(codBarraNew)) {
                codBarraOld.getInventarioList().remove(inventario);
                codBarraOld = em.merge(codBarraOld);
            }
            if (codBarraNew != null && !codBarraNew.equals(codBarraOld)) {
                codBarraNew.getInventarioList().add(inventario);
                codBarraNew = em.merge(codBarraNew);
            }
            if (idCompraOld != null && !idCompraOld.equals(idCompraNew)) {
                idCompraOld.getInventarioList().remove(inventario);
                idCompraOld = em.merge(idCompraOld);
            }
            if (idCompraNew != null && !idCompraNew.equals(idCompraOld)) {
                idCompraNew.getInventarioList().add(inventario);
                idCompraNew = em.merge(idCompraNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = inventario.getNumParte();
                if (findInventario(id) == null) {
                    throw new NonexistentEntityException("The inventario with id " + id + " no longer exists.");
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
            Inventario inventario;
            try {
                inventario = em.getReference(Inventario.class, id);
                inventario.getNumParte();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inventario with id " + id + " no longer exists.", enfe);
            }
            Producto codBarra = inventario.getCodBarra();
            if (codBarra != null) {
                codBarra.getInventarioList().remove(inventario);
                codBarra = em.merge(codBarra);
            }
            Compra idCompra = inventario.getIdCompra();
            if (idCompra != null) {
                idCompra.getInventarioList().remove(inventario);
                idCompra = em.merge(idCompra);
            }
            em.remove(inventario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Inventario> findInventarioEntities() {
        return findInventarioEntities(true, -1, -1);
    }

    public List<Inventario> findInventarioEntities(int maxResults, int firstResult) {
        return findInventarioEntities(false, maxResults, firstResult);
    }

    private List<Inventario> findInventarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Inventario.class));
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

    public Inventario findInventario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Inventario.class, id);
        } finally {
            em.close();
        }
    }

    public int getInventarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Inventario> rt = cq.from(Inventario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
