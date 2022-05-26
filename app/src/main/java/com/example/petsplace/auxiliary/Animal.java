package com.example.petsplace.auxiliary;

public class Animal {
    String name;
    String age;
    String type;

    public Animal(String type, String name, String age) {
        this.name = name;
        this.age = age;
        this.type = type;
    }

    public Animal() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getType() {
        return type;
    }

}
