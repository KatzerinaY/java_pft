package ru.stqa.pft.sandbox;

import org.testng.annotations.Test;


public class PointTest {
    @Test
    public void testDistance1(){
        Point p1 = new Point(3,5);
        Point p2 = new Point(6,5);
        //assert p1.distance(p2) == 3;
        assert p1.distance(p2) == 4;
        System.out.println("test1 katja");
    }
    @Test
    public void testDistance2(){
        Point p1 = new Point(3,7);
        Point p2 = new Point(3,5);
        assert p2.distance(p1) == 2;
        System.out.println("test2 katja");
    }

}
