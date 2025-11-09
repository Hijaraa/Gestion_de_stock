package projet.service;

import projet.classes.Commande;
import projet.classes.LigneCommande;
import projet.dao.IDao;
import projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommandeService implements IDao<Commande> {

    @Override
    public boolean create(Commande o) {
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
    public boolean update(Commande o) {
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
    public boolean delete(Commande o) {
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
    public Commande findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Commande.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Commande> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Commande", Commande.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Afficher les produits commandés dans une commande donnée
     */
    public void afficherProduitsCommande(Commande commande) {
        if (commande == null) {
            System.out.println("Commande introuvable.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

        System.out.println("\nCommande : " + commande.getId() + "     Date : " + sdf.format(commande.getDate()));
        System.out.println("Liste des produits :");
        System.out.println("Référence\tPrix\t\tQuantité");

        if (commande.getLignesCommande() != null && !commande.getLignesCommande().isEmpty()) {
            for (LigneCommande lc : commande.getLignesCommande()) {
                System.out.printf("%s\t\t%.0f DH\t\t%d\n",
                        lc.getProduit().getReference(),
                        lc.getProduit().getPrix(),
                        lc.getQuantite());
            }
        } else {
            System.out.println("Aucun produit dans cette commande.");
        }
    }
}