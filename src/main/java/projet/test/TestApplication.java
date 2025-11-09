package projet.test;

import projet.classes.Categorie;
import projet.classes.Commande;
import projet.classes.LigneCommande;
import projet.classes.Produit;
import projet.service.CategorieService;
import projet.service.CommandeService;
import projet.service.LigneCommandeService;
import projet.service.ProduitService;
import projet.util.HibernateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestApplication {

    public static void main(String[] args) {
        // Initialisation des services
        CategorieService categorieService = new CategorieService();
        ProduitService produitService = new ProduitService();
        CommandeService commandeService = new CommandeService();
        LigneCommandeService ligneCommandeService = new LigneCommandeService();

        try {
            // Test 1: Créer des catégories
            System.out.println("=== Test 1: Création des catégories ===");
            Categorie cat1 = new Categorie("ORDI", "Ordinateurs");
            Categorie cat2 = new Categorie("IMP", "Imprimantes");

            categorieService.create(cat1);
            categorieService.create(cat2);
            System.out.println("Catégories créées avec succès!");

            // Test 2: Créer des produits
            System.out.println("\n=== Test 2: Création des produits ===");
            Produit p1 = new Produit("ES12", 120, cat1);
            Produit p2 = new Produit("ZR85", 100, cat1);
            Produit p3 = new Produit("EE85", 200, cat2);
            Produit p4 = new Produit("PP12", 80, cat2);
            Produit p5 = new Produit("ML15", 150, cat1);

            produitService.create(p1);
            produitService.create(p2);
            produitService.create(p3);
            produitService.create(p4);
            produitService.create(p5);
            System.out.println("Produits créés avec succès!");

            // Test 3: Afficher les produits par catégorie
            System.out.println("\n=== Test 3: Produits par catégorie ===");
            List<Produit> produitsOrdi = produitService.findByCategorie(cat1);
            System.out.println("Produits de la catégorie Ordinateurs:");
            for (Produit p : produitsOrdi) {
                System.out.println("  - " + p.getReference() + " : " + p.getPrix() + " DH");
            }

            // Test 4: Créer des commandes
            System.out.println("\n=== Test 4: Création des commandes ===");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Commande cmd1 = new Commande(sdf.parse("14/03/2013"));
            Commande cmd2 = new Commande(sdf.parse("20/03/2013"));
            Commande cmd3 = new Commande(sdf.parse("25/03/2013"));

            commandeService.create(cmd1);
            commandeService.create(cmd2);
            commandeService.create(cmd3);

            // Test 5: Ajouter des lignes de commande
            System.out.println("\n=== Test 5: Ajout des lignes de commande ===");
            LigneCommande lc1 = new LigneCommande(cmd1, p1, 7);
            LigneCommande lc2 = new LigneCommande(cmd1, p2, 14);
            LigneCommande lc3 = new LigneCommande(cmd1, p3, 5);
            LigneCommande lc4 = new LigneCommande(cmd2, p4, 3);
            LigneCommande lc5 = new LigneCommande(cmd2, p5, 10);
            LigneCommande lc6 = new LigneCommande(cmd3, p1, 2);

            ligneCommandeService.create(lc1);
            ligneCommandeService.create(lc2);
            ligneCommandeService.create(lc3);
            ligneCommandeService.create(lc4);
            ligneCommandeService.create(lc5);
            ligneCommandeService.create(lc6);
            System.out.println("Lignes de commande créées avec succès!");

            // Test 6: Afficher les produits d'une commande
            System.out.println("\n=== Test 6: Produits de la commande 1 ===");
            Commande commande = commandeService.findById(cmd1.getId());
            commandeService.afficherProduitsCommande(commande);

            // Test 7: Produits commandés entre deux dates
            System.out.println("\n=== Test 7: Produits commandés entre deux dates ===");
            Date dateDebut = sdf.parse("10/03/2013");
            Date dateFin = sdf.parse("22/03/2013");

            List<Produit> produitsEntreDates = produitService.findProduitsCommandesEntreDeuxDates(dateDebut, dateFin);
            System.out.println("Produits commandés entre le 10/03/2013 et le 22/03/2013:");
            for (Produit p : produitsEntreDates) {
                System.out.println("  - " + p.getReference() + " : " + p.getPrix() + " DH");
            }

            // Test 8: Produits avec prix supérieur à 100 DH (requête nommée)
            System.out.println("\n=== Test 8: Produits avec prix > 100 DH ===");
            List<Produit> produitsCher = produitService.findProduitsPrixSuperieur(100);
            System.out.println("Produits dont le prix est supérieur à 100 DH:");
            for (Produit p : produitsCher) {
                System.out.println("  - " + p.getReference() + " : " + p.getPrix() + " DH (" + p.getCategorie().getLibelle() + ")");
            }

            // Test 9: Afficher toutes les catégories
            System.out.println("\n=== Test 9: Liste de toutes les catégories ===");
            List<Categorie> categories = categorieService.findAll();
            for (Categorie c : categories) {
                System.out.println("  - " + c.getCode() + " : " + c.getLibelle());
            }

            // Test 10: Afficher toutes les commandes
            System.out.println("\n=== Test 10: Liste de toutes les commandes ===");
            List<Commande> commandes = commandeService.findAll();
            for (Commande c : commandes) {
                System.out.println("  - Commande #" + c.getId() + " du " + sdf.format(c.getDate()));
            }

        } catch (ParseException e) {
            System.err.println("Erreur de format de date: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution des tests: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermeture de la SessionFactory
            HibernateUtil.shutdown();
        }
    }
}