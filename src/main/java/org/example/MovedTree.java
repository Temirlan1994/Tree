package org.example;

import jakarta.persistence.*;

import java.util.Scanner;

public class MovedTree {

    public void launch(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        Scanner sc = new Scanner(System.in);
        System.out.print("введите id категории: ");
        int categoryId = Integer.parseInt(sc.nextLine());
        System.out.print("введите id новой родительской категории: ");
        int newCategoryId = Integer.parseInt(sc.nextLine());

        try{
            manager.getTransaction().begin();

            // получаем объект категории которую будем перемещать, для получения нужных полей
            Tree moveCategory = manager.find(Tree.class, categoryId);
            int leftKey = moveCategory.getLeftKey();
            int rightKey = moveCategory.getRightKey();

            // 1) Присваиваем минусовые значения
            Query query = manager.createQuery(
                    "update Tree t set t.left_key = t.left_key * ?1, t.right_key = t.right_key * ?1" +
                            "where t.left_key>=?2 and t.left_key<=?3 or t.right_key>=?2 and t.right_key<=?3"
            );
            query.setParameter(1, -1);
            query.setParameter(2,leftKey);
            query.setParameter(3,rightKey);
            query.executeUpdate();

            // Для перезаписи берем разницу ключей
            int keysInterval = rightKey-leftKey+1;

            // 2) Перезаписываем все левые и правые ключи всех категории
            query = manager.createQuery(
                    "update Tree t set t.left_key = t.left_key - ?1 where t.left_key>=?2"
            );
            query.setParameter(1,keysInterval);
            query.setParameter(2,rightKey);
            query.executeUpdate();

            query = manager.createQuery(
                    "update Tree t set t.right_key = t.right_key - ?1 where t.right_key>=?2"
            );
            query.setParameter(1,keysInterval);
            query.setParameter(2,rightKey);
            query.executeUpdate();

            // Для высвобождения места получаем объект родительской категории, куда будет
            // перемещена выбранная категория
            Tree parentCategory = manager.find(Tree.class, newCategoryId);

            // Высвобождаем место для вставки перемещаемой категории
            query = manager.createQuery(
                    "update Tree t set t.left_key = t.left_key + ?1 where t.left_key>=?2"
            );
            query.setParameter(1,keysInterval);
            query.setParameter(2,parentCategory.getRightKey());
            query.executeUpdate();

            query = manager.createQuery(
                    "update Tree t set t.right_key = t.right_key + ?1 where t.right_key>=?2"
            );
            query.setParameter(1,keysInterval);
            query.setParameter(2,parentCategory.getRightKey());
            query.executeUpdate();

            // Обновляем данные родительского объекта
            manager.refresh(parentCategory);

            // Меняем ключи по формуле
            query = manager.createQuery(
                    "update  Tree t set t.left_key = (0 - t.left_key) + (?1 - ?2 - 1)," +
                            "t.right_key = (0 - t.right_key) + (?1 - ?2 -1) where t.left_key<0 and t.right_key<0"
            );
            query.setParameter(1,parentCategory.getRightKey());
            query.setParameter(2,moveCategory.getRightKey());
            query.executeUpdate();

            // обновляем перемещаемую категорию и приводим уровни в соответствие
            manager.refresh(moveCategory);

            int level2 = parentCategory.getLevel(); // уровень новой родительской категории
            int level1 = moveCategory.getLevel(); // уровень перемещаемой категории
            int levelInterval = level2-level1; // интервал уровней

            query = manager.createQuery(
                    "update Tree t set t.level = t.level + ?1 + 1 where t.left_key>=?2 and t.right_key<=?3"
            );
            query.setParameter(1,levelInterval);
            query.setParameter(2,moveCategory.getLeftKey());
            query.setParameter(3,moveCategory.getRightKey());
            query.executeUpdate();

            manager.getTransaction().commit();
        }catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}



