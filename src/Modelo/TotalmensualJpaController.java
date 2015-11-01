/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Modelo.Entity.Totalmensual;
import Modelo.Entity.TotalmensualPK;
import Modelo.exceptions.NonexistentEntityException;
import Modelo.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author José Carlos Grijalva González
 */
public class TotalmensualJpaController implements Serializable {

    public TotalmensualJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Totalmensual totalmensual) throws PreexistingEntityException, Exception {
        if (totalmensual.getTotalmensualPK() == null) {
            totalmensual.setTotalmensualPK(new TotalmensualPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(totalmensual);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTotalmensual(totalmensual.getTotalmensualPK()) != null) {
                throw new PreexistingEntityException("Totalmensual " + totalmensual + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Totalmensual totalmensual) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            totalmensual = em.merge(totalmensual);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TotalmensualPK id = totalmensual.getTotalmensualPK();
                if (findTotalmensual(id) == null) {
                    throw new NonexistentEntityException("The totalmensual with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TotalmensualPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Totalmensual totalmensual;
            try {
                totalmensual = em.getReference(Totalmensual.class, id);
                totalmensual.getTotalmensualPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The totalmensual with id " + id + " no longer exists.", enfe);
            }
            em.remove(totalmensual);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Totalmensual> findTotalmensualEntities() {
        return findTotalmensualEntities(true, -1, -1);
    }

    public List<Totalmensual> findTotalmensualEntities(int maxResults, int firstResult) {
        return findTotalmensualEntities(false, maxResults, firstResult);
    }

    private List<Totalmensual> findTotalmensualEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Totalmensual.class));
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

    public Totalmensual findTotalmensual(TotalmensualPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Totalmensual.class, id);
        } finally {
            em.close();
        }
    }

    public int getTotalmensualCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Totalmensual> rt = cq.from(Totalmensual.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
