package com.szachnowicz.test;

import java.util.List;

public class TestClass {

    public String a;
    public int liczba;
    public long Longs;

    public List<List<String>> list;

    public TestClass(String a, int liczba, long longs) {
        this.a = a;
        this.liczba = liczba;
        Longs = longs;
    }

    public TestClass(String a, int liczba) {

        this.a = a;
        this.liczba = liczba;
    }

    public TestClass(String a) {

        this.a = a;
    }

    public String returnString() {
        return "just a regular String ";
    }


    public int returnInt() {
        return 10;
    }

    public int add(int a, int b) {
        return a + b;
    }

    public TestClass instaceOf(TestClass testClass) {
        return this;

    }


    public static void printCL() {
        System.out.println("Foo ClassLoader: " + TestClass.class.getClassLoader());
    }

}
