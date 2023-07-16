package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public class Main {
    public static void main(String[] args) {


        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        try {
            manager.getTransaction().begin();

            TypedQuery<Tree> treeQuery = manager.createQuery(
                    "select t from Tree t", Tree.class
            );

            List<Tree> treeList = treeQuery.getResultList();

            for (Tree tree : treeList) {
                for (int i = 0; i < tree.getLevel(); i++) {
                    System.out.print("- ");
                }
                System.out.println(tree.getName());
            }

            manager.getTransaction().commit();
        }catch (Exception e){
            manager.getTransaction().rollback();
            e.printStackTrace();
        }





    }



}

