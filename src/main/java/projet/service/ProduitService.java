package projet.service;

import projet.classes.Categorie;
import projet.classes.Produit;
import projet.dao.IDao;
import projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class ProduitService implements IDao<Produit> {

    @Override
    public boolean create(Produit o) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(o);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Produit o) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(o);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Produit o) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(o);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Produit findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Produit.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Produit> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Produit", Produit.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Afficher la liste des produits par catégorie
     */
    public List<Produit> findByCategorie(Categorie categorie) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Produit> query = session.createQuery(
                    "FROM Produit p WHERE p.categorie = :categorie", Produit.class);
            query.setParameter("categorie", categorie);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Afficher les produits commandés entre deux dates
     */
    public List<Produit> findProduitsCommandesEntreDeuxDates(Date dateDebut, Date dateFin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Produit> query = session.createQuery(
                    "SELECT DISTINCT p FROM Produit p " +
                            "JOIN p.lignesCommande lc " +
                            "JOIN lc.commande c " +
                            "WHERE c.date BETWEEN :dateDebut AND :dateFin",
                    Produit.class);
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Afficher les produits dont le prix est supérieur à 100 DH (requête nommée)
     */
    public List<Produit> findProduitsPrixSuperieur(double prix) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Produit> query = session.createNamedQuery("findProduitsByPrixSuperieur", Produit.class);
            query.setParameter("prix", prix);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}