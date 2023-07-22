package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "tree")
public class Tree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int left_key;
    private int right_key;

    private int level;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeftKey() {
        return left_key;
    }

    public void setLeftKey(int leftKey) {
        this.left_key = leftKey;
    }

    public int getRightKey() {
        return right_key;
    }

    public void setRightKey(int rightKey) {
        this.right_key = rightKey;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
