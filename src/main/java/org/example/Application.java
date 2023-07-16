package org.example;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {

        System.out.println("Создать категорию [1]");
        System.out.println("Переместить категорию [2]");
        System.out.println("Удалить категорию [3]");
        Scanner sc = new Scanner(System.in);
        System.out.print("Выберите действие: ");
        String action = sc.nextLine();

        if(action.equals("1")){
            CreateCategory createCategory = new CreateCategory();
            createCategory.launch();
        }else if(action.equals("2")){
            MovedTree movedTree = new MovedTree();
            movedTree.launch();
        }else if(action.equals("3")){
            DeleteCategory deleteCategory = new DeleteCategory();
            deleteCategory.launch();
        }else {
            System.out.println("wrong action");
        }




    }
}

