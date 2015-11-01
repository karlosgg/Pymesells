/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Modelo.Entity.Tipoventa;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Entity.Venta;
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
public class TipoventaJpaController implements Serializable {

    public TipoventaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipoventa tipoventa) throws PreexistingEntityException, Exception {
        if (tipoventa.getVentaList() == null) {
            tipoventa.setVentaList(new ArrayList<Venta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Venta> attachedVentaList = new ArrayList<Venta>();
            for (Venta ventaListVentaToAttach : tipoventa.getVentaList()) {
                ventaListVentaToAttach = em.getReference(ventaListVentaToAttach.getClass(), ventaListVentaToAttach.getIdVenta());
                attachedVentaList.add(ventaListVentaToAttach);
            }
            tipoventa.setVentaList(attachedVentaList);
            em.persist(tipoventa);
            for (Venta ventaListVenta : tipoventa.getVentaList()) {
                Tipoventa oldIdTipoVentaOfVentaListVenta = ventaListVenta.getIdTipoVenta();
                ventaListVenta.setIdTipoVenta(tipoventa);
                ventaListVenta = em.merge(ventaListVenta);
                if (oldIdTipoVentaOfVentaListVenta != null) {
                    oldIdTipoVentaOfVentaListVenta.getVentaList().remove(ventaListVenta);
                    oldIdTipoVentaOfVentaListVenta = em.merge(oldIdTipoVentaOfVentaListVenta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoventa(tipoventa.getIdTipoVenta()) != null) {
                throw new PreexistingEntityException("Tipoventa " + tipoventa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipoventa tipoventa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoventa persistentTipoventa = em.find(Tipoventa.class, tipoventa.getIdTipoVenta());
            List<Venta> ventaListOld = persistentTipoventa.getVentaList();
            List<Venta> ventaListNew = tipoventa.getVentaList();
            List<Venta> attachedVentaListNew = new ArrayList<Venta>();
            for (Venta ventaListNewVentaToAttach : ventaListNew) {
                ventaListNewVentaToAttach = em.getReference(ventaListNewVentaToAttach.getClass(), ventaListNewVentaToAttach.getIdVenta());
                attachedVentaListNew.add(ventaListNewVentaToAttach);
            }
            ventaListNew = attachedVentaListNew;
            tipoventa.setVentaList(ventaListNew);
            tipoventa = em.merge(tipoventa);
            for (Venta ventaListOldVenta : ventaListOld) {
                if (!ventaListNew.contains(ventaListOldVenta)) {
                    ventaListOldVenta.setIdTipoVenta(null);
                    ventaListOldVenta = em.merge(ventaListOldVenta);
                }
            }
            for (Venta ventaListNewVenta : ventaListNew) {
                if (!ventaListOld.contains(ventaListNewVenta)) {
                    Tipoventa oldIdTipoVentaOfVentaListNewVenta = ventaListNewVenta.getIdTipoVenta();
                    ventaListNewVenta.setIdTipoVenta(tipoventa);
                    ventaListNewVenta = em.merge(ventaListNewVenta);
                    if (oldIdTipoVentaOfVentaListNewVenta != null && !oldIdTipoVentaOfVentaListNewVenta.equals(tipoventa)) {
                        oldIdTipoVentaOfVentaListNewVenta.getVentaList().remove(ventaListNewVenta);
                        oldIdTipoVentaOfVentaListNewVenta = em.merge(oldIdTipoVentaOfVentaListNewVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tipoventa.getIdTipoVenta();
                if (findTipoventa(id) == null) {
                    throw new NonexistentEntityException("The tipoventa with id " + id + " no longer exists.");
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
            Tipoventa tipoventa;
            try {
                tipoventa = em.getReference(Tipoventa.class, id);
                tipoventa.getIdTipoVenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoventa with id " + id + " no longer exists.", enfe);
            }
            List<Venta> ventaList = tipoventa.getVentaList();
            for (Venta ventaListVenta : ventaList) {
                ventaListVenta.setIdTipoVenta(null);
                ventaListVenta = em.merge(ventaListVenta);
            }
            em.remove(tipoventa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipoventa> findTipoventaEntities() {
        return findTipoventaEntities(true, -1, -1);
    }

    public List<Tipoventa> findTipoventaEntities(int maxResults, int firstResult) {
        return findTipoventaEntities(false, maxResults, firstResult);
    }

    private List<Tipoventa> findTipoventaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipoventa.class));
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

    public Tipoventa findTipoventa(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipoventa.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoventaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipoventa> rt = cq.from(Tipoventa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
