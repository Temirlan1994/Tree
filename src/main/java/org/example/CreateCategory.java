package org.example;

import jakarta.persistence.*;

import java.util.List;
import java.util.Scanner;

public class CreateCategory {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        Scanner sc = new Scanner(System.in);
        System.out.print("введите id категории: ");
        int category = Integer.parseInt(sc.nextLine());
        System.out.println("введите название продукта: ");
        String name = sc.nextLine();

        try {
            manager.getTransaction().begin();

            if (category != 0) {
                TypedQuery<Tree> treeTypedQuery = manager.createQuery(
                        "select t from Tree t where t.id=?1", Tree.class
                );
                treeTypedQuery.setParameter(1, category);
                Tree treeResult = treeTypedQuery.getSingleResult();

                int rightKey = treeResult.getRightKey();

                Query query = manager.createQuery(
                        "update Tree t set t.left_key = t.left_key + 2 where t.left_key>?1"
                );
                query.setParameter(1, rightKey);
                query.executeUpdate();

                query = manager.createQuery(
                        "update Tree t set t.right_key = t.right_key + 2 where t.right_key>=?1"
                );
                query.setParameter(1, rightKey);
                query.executeUpdate();

                Tree tree = new Tree();
                tree.setLevel(treeResult.getLevel() + 1);
                tree.setName(name);
                tree.setLeftKey(rightKey);
                tree.setRightKey(rightKey + 1);
                manager.persist(tree);
            } else {
                TypedQuery<Integer> treeQuery = manager.createQuery(
                        "select max(t.right_key) from Tree t where t.level=?1", Integer.class
                );
                treeQuery.setParameter(1,category);
                Integer res = treeQuery.getSingleResult();
                Tree tree = new Tree();
                tree.setName(name);
                tree.setLevel(category);
                tree.setLeftKey(res+1);
                tree.setRightKey(res+2);
                manager.persist(tree);
            }




            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }


    }
}
