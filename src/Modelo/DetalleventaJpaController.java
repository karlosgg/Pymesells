/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Modelo.Entity.Detalleventa;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Entity.Venta;
import Modelo.Entity.Producto;
import Modelo.exceptions.NonexistentEntityException;
import Modelo.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author José Carlos Grijalva González
 */
public class DetalleventaJpaController implements Serializable {

    public DetalleventaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detalleventa detalleventa) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta idVenta = detalleventa.getIdVenta();
            if (idVenta != null) {
                idVenta = em.getReference(idVenta.getClass(), idVenta.getIdVenta());
                detalleventa.setIdVenta(idVenta);
            }
            Producto codBarra = detalleventa.getCodBarra();
            if (codBarra != null) {
                codBarra = em.getReference(codBarra.getClass(), codBarra.getCodBarra());
                detalleventa.setCodBarra(codBarra);
            }
            em.persist(detalleventa);
            if (idVenta != null) {
                idVenta.getDetalleventaList().add(detalleventa);
                idVenta = em.merge(idVenta);
            }
            if (codBarra != null) {
                codBarra.getDetalleventaList().add(detalleventa);
                codBarra = em.merge(codBarra);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetalleventa(detalleventa.getNumParte()) != null) {
                throw new PreexistingEntityException("Detalleventa " + detalleventa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detalleventa detalleventa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detalleventa persistentDetalleventa = em.find(Detalleventa.class, detalleventa.getNumParte());
            Venta idVentaOld = persistentDetalleventa.getIdVenta();
            Venta idVentaNew = detalleventa.getIdVenta();
            Producto codBarraOld = persistentDetalleventa.getCodBarra();
            Producto codBarraNew = detalleventa.getCodBarra();
            if (idVentaNew != null) {
                idVentaNew = em.getReference(idVentaNew.getClass(), idVentaNew.getIdVenta());
                detalleventa.setIdVenta(idVentaNew);
            }
            if (codBarraNew != null) {
                codBarraNew = em.getReference(codBarraNew.getClass(), codBarraNew.getCodBarra());
                detalleventa.setCodBarra(codBarraNew);
            }
            detalleventa = em.merge(detalleventa);
            if (idVentaOld != null && !idVentaOld.equals(idVentaNew)) {
                idVentaOld.getDetalleventaList().remove(detalleventa);
                idVentaOld = em.merge(idVentaOld);
            }
            if (idVentaNew != null && !idVentaNew.equals(idVentaOld)) {
                idVentaNew.getDetalleventaList().add(detalleventa);
                idVentaNew = em.merge(idVentaNew);
            }
            if (codBarraOld != null && !codBarraOld.equals(codBarraNew)) {
                codBarraOld.getDetalleventaList().remove(detalleventa);
                codBarraOld = em.merge(codBarraOld);
            }
            if (codBarraNew != null && !codBarraNew.equals(codBarraOld)) {
                codBarraNew.getDetalleventaList().add(detalleventa);
                codBarraNew = em.merge(codBarraNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = detalleventa.getNumParte();
                if (findDetalleventa(id) == null) {
                    throw new NonexistentEntityException("The detalleventa with id " + id + " no longer exists.");
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
            Detalleventa detalleventa;
            try {
                detalleventa = em.getReference(Detalleventa.class, id);
                detalleventa.getNumParte();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleventa with id " + id + " no longer exists.", enfe);
            }
            Venta idVenta = detalleventa.getIdVenta();
            if (idVenta != null) {
                idVenta.getDetalleventaList().remove(detalleventa);
                idVenta = em.merge(idVenta);
            }
            Producto codBarra = detalleventa.getCodBarra();
            if (codBarra != null) {
                codBarra.getDetalleventaList().remove(detalleventa);
                codBarra = em.merge(codBarra);
            }
            em.remove(detalleventa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detalleventa> findDetalleventaEntities() {
        return findDetalleventaEntities(true, -1, -1);
    }

    public List<Detalleventa> findDetalleventaEntities(int maxResults, int firstResult) {
        return findDetalleventaEntities(false, maxResults, firstResult);
    }

    private List<Detalleventa> findDetalleventaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detalleventa.class));
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

    public Detalleventa findDetalleventa(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleventa.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleventaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detalleventa> rt = cq.from(Detalleventa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
