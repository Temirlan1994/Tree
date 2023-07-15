package org.example;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class DeleteCategory {
    public static void main(String[] args) {
        // паттерны проектирования книга ооп

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        Scanner sc = new Scanner(System.in);
        System.out.print("введите id категории: ");
        int category = Integer.parseInt(sc.nextLine());

        try {
            manager.getTransaction().begin();

            TypedQuery<Tree> treeTypedQuery = manager.createQuery(
                    "select t from Tree t where t.id=?1", Tree.class
            );
            treeTypedQuery.setParameter(1,category);
            Tree treeResult = treeTypedQuery.getSingleResult();

            int leftKey = treeResult.getLeftKey();
            int rightKey = treeResult.getRightKey();
            int res = rightKey-leftKey+1;


            Query query1 = manager.createQuery(
                    "delete from Tree t where t.right_key<=?1 and t.left_key>=?2"
            );
            query1.setParameter(1,rightKey);
            query1.setParameter(2,leftKey);
            query1.executeUpdate();

            Query query = manager.createQuery(
                    "update Tree t set t.left_key = t.left_key - ?1 where t.left_key>?2"
            );
            query.setParameter(1,res);
            query.setParameter(2,rightKey);
            query.executeUpdate();

            query = manager.createQuery(
                    "update Tree t set t.right_key = t.right_key - ?1 where t.right_key>=?2"
            );
            query.setParameter(1,res);
            query.setParameter(2,rightKey);
            query.executeUpdate();


            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }

    }
}
