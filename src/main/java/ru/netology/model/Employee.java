package ru.netology.model;

public class Employee {
    public long id;
    public String firstName;
    public String lastName;
    public String country;
    public int age;

    public Employee() {}

    public Employee(long id, String firstName, String lastName, String country, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format(
                "Employee{id=%d, firstName='%s', lastName='%s', country='%s', age=%d}",
                id, firstName, lastName, country, age
        );
    }
}