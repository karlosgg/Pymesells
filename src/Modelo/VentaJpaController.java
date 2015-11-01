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
import Modelo.Entity.Tipoventa;
import Modelo.Entity.Cliente;
import Modelo.Entity.Detalleventa;
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
public class VentaJpaController implements Serializable {

    public VentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Venta venta) throws PreexistingEntityException, Exception {
        if (venta.getDetalleventaList() == null) {
            venta.setDetalleventaList(new ArrayList<Detalleventa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoventa idTipoVenta = venta.getIdTipoVenta();
            if (idTipoVenta != null) {
                idTipoVenta = em.getReference(idTipoVenta.getClass(), idTipoVenta.getIdTipoVenta());
                venta.setIdTipoVenta(idTipoVenta);
            }
            Cliente idCliente = venta.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                venta.setIdCliente(idCliente);
            }
            List<Detalleventa> attachedDetalleventaList = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaListDetalleventaToAttach : venta.getDetalleventaList()) {
                detalleventaListDetalleventaToAttach = em.getReference(detalleventaListDetalleventaToAttach.getClass(), detalleventaListDetalleventaToAttach.getNumParte());
                attachedDetalleventaList.add(detalleventaListDetalleventaToAttach);
            }
            venta.setDetalleventaList(attachedDetalleventaList);
            em.persist(venta);
            if (idTipoVenta != null) {
                idTipoVenta.getVentaList().add(venta);
                idTipoVenta = em.merge(idTipoVenta);
            }
            if (idCliente != null) {
                idCliente.getVentaList().add(venta);
                idCliente = em.merge(idCliente);
            }
            for (Detalleventa detalleventaListDetalleventa : venta.getDetalleventaList()) {
                Venta oldIdVentaOfDetalleventaListDetalleventa = detalleventaListDetalleventa.getIdVenta();
                detalleventaListDetalleventa.setIdVenta(venta);
                detalleventaListDetalleventa = em.merge(detalleventaListDetalleventa);
                if (oldIdVentaOfDetalleventaListDetalleventa != null) {
                    oldIdVentaOfDetalleventaListDetalleventa.getDetalleventaList().remove(detalleventaListDetalleventa);
                    oldIdVentaOfDetalleventaListDetalleventa = em.merge(oldIdVentaOfDetalleventaListDetalleventa);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVenta(venta.getIdVenta()) != null) {
                throw new PreexistingEntityException("Venta " + venta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Venta venta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta persistentVenta = em.find(Venta.class, venta.getIdVenta());
            Tipoventa idTipoVentaOld = persistentVenta.getIdTipoVenta();
            Tipoventa idTipoVentaNew = venta.getIdTipoVenta();
            Cliente idClienteOld = persistentVenta.getIdCliente();
            Cliente idClienteNew = venta.getIdCliente();
            List<Detalleventa> detalleventaListOld = persistentVenta.getDetalleventaList();
            List<Detalleventa> detalleventaListNew = venta.getDetalleventaList();
            if (idTipoVentaNew != null) {
                idTipoVentaNew = em.getReference(idTipoVentaNew.getClass(), idTipoVentaNew.getIdTipoVenta());
                venta.setIdTipoVenta(idTipoVentaNew);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                venta.setIdCliente(idClienteNew);
            }
            List<Detalleventa> attachedDetalleventaListNew = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaListNewDetalleventaToAttach : detalleventaListNew) {
                detalleventaListNewDetalleventaToAttach = em.getReference(detalleventaListNewDetalleventaToAttach.getClass(), detalleventaListNewDetalleventaToAttach.getNumParte());
                attachedDetalleventaListNew.add(detalleventaListNewDetalleventaToAttach);
            }
            detalleventaListNew = attachedDetalleventaListNew;
            venta.setDetalleventaList(detalleventaListNew);
            venta = em.merge(venta);
            if (idTipoVentaOld != null && !idTipoVentaOld.equals(idTipoVentaNew)) {
                idTipoVentaOld.getVentaList().remove(venta);
                idTipoVentaOld = em.merge(idTipoVentaOld);
            }
            if (idTipoVentaNew != null && !idTipoVentaNew.equals(idTipoVentaOld)) {
                idTipoVentaNew.getVentaList().add(venta);
                idTipoVentaNew = em.merge(idTipoVentaNew);
            }
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getVentaList().remove(venta);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getVentaList().add(venta);
                idClienteNew = em.merge(idClienteNew);
            }
            for (Detalleventa detalleventaListOldDetalleventa : detalleventaListOld) {
                if (!detalleventaListNew.contains(detalleventaListOldDetalleventa)) {
                    detalleventaListOldDetalleventa.setIdVenta(null);
                    detalleventaListOldDetalleventa = em.merge(detalleventaListOldDetalleventa);
                }
            }
            for (Detalleventa detalleventaListNewDetalleventa : detalleventaListNew) {
                if (!detalleventaListOld.contains(detalleventaListNewDetalleventa)) {
                    Venta oldIdVentaOfDetalleventaListNewDetalleventa = detalleventaListNewDetalleventa.getIdVenta();
                    detalleventaListNewDetalleventa.setIdVenta(venta);
                    detalleventaListNewDetalleventa = em.merge(detalleventaListNewDetalleventa);
                    if (oldIdVentaOfDetalleventaListNewDetalleventa != null && !oldIdVentaOfDetalleventaListNewDetalleventa.equals(venta)) {
                        oldIdVentaOfDetalleventaListNewDetalleventa.getDetalleventaList().remove(detalleventaListNewDetalleventa);
                        oldIdVentaOfDetalleventaListNewDetalleventa = em.merge(oldIdVentaOfDetalleventaListNewDetalleventa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = venta.getIdVenta();
                if (findVenta(id) == null) {
                    throw new NonexistentEntityException("The venta with id " + id + " no longer exists.");
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
            Venta venta;
            try {
                venta = em.getReference(Venta.class, id);
                venta.getIdVenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The venta with id " + id + " no longer exists.", enfe);
            }
            Tipoventa idTipoVenta = venta.getIdTipoVenta();
            if (idTipoVenta != null) {
                idTipoVenta.getVentaList().remove(venta);
                idTipoVenta = em.merge(idTipoVenta);
            }
            Cliente idCliente = venta.getIdCliente();
            if (idCliente != null) {
                idCliente.getVentaList().remove(venta);
                idCliente = em.merge(idCliente);
            }
            List<Detalleventa> detalleventaList = venta.getDetalleventaList();
            for (Detalleventa detalleventaListDetalleventa : detalleventaList) {
                detalleventaListDetalleventa.setIdVenta(null);
                detalleventaListDetalleventa = em.merge(detalleventaListDetalleventa);
            }
            em.remove(venta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Venta> findVentaEntities() {
        return findVentaEntities(true, -1, -1);
    }

    public List<Venta> findVentaEntities(int maxResults, int firstResult) {
        return findVentaEntities(false, maxResults, firstResult);
    }

    private List<Venta> findVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Venta.class));
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

    public Venta findVenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Venta> rt = cq.from(Venta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
