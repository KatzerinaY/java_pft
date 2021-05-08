package ru.stqa.pft.sandbox;

public class MyFirstProgramm {

    public static void main(String[] args) {

        Point p1 = new Point(2,3);
        Point p2 = new Point(2,5);

        System.out.println("p1=("+p1.x+","+p1.y+")");
        System.out.println("p2=("+p2.x+","+p2.y+")");

        System.out.println("function distance(p1,p2) = "+distance(p1, p2));
        System.out.println("method p1.distance(p2) = "+p1.distance(p2));
    }


    public static double distance(Point p1, Point p2){
        return  Math.sqrt( Math.pow(p1.x-p2.x, 2)+ Math.pow(p1.y-p2.y, 2));
    }

}