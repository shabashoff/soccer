package ru.shabashoff.entity;

import org.junit.Assert;
import org.junit.Test;

public class VectorLengthAngleTest {

    private Point pp(double x, double y) {
        return new Point(x, y);
    }

    @Test
    public void testAngle45() {
        Vector vector = new Vector(pp(1, 1), pp(2, 2));

        Assert.assertEquals(vector.getLength(), 1.414d, 0.001);
        Assert.assertEquals(vector.getAngle(), 45.0d, 0.001);

        vector = new Vector(pp(-2, -2), pp(-1, -1));

        Assert.assertEquals(vector.getLength(), 1.414d, 0.001);
        Assert.assertEquals(vector.getAngle(), 45.0d, 0.001);
    }

    @Test
    public void testAngle90() {
        Vector vector = new Vector(pp(1, 1), pp(1, 2));

        Assert.assertEquals(vector.getLength(), 1.d, 0.001);
        Assert.assertEquals(vector.getAngle(), 90.0d, 0.001);

        vector = new Vector(pp(-2, -2), pp(-2, -1));

        Assert.assertEquals(vector.getLength(), 1.d, 0.001);
        Assert.assertEquals(vector.getAngle(), 90.0d, 0.001);
    }

    @Test
    public void testAngle135() {
        Vector vector = new Vector(pp(1, 1), pp(0, 2));

        Assert.assertEquals(vector.getLength(), 1.414d, 0.001);
        Assert.assertEquals(vector.getAngle(), 135.0d, 0.001);

        vector = new Vector(pp(-2, -2), pp(-3, -1));

        Assert.assertEquals(vector.getLength(), 1.414d, 0.001);
        Assert.assertEquals(vector.getAngle(), 135.0d, 0.001);
    }

    @Test
    public void testAngle150() {
        Vector vector = new Vector(pp(1, 1), pp(0.13397459621, 1.5));

        Assert.assertEquals(vector.getLength(), 1.d, 0.001);
        Assert.assertEquals(vector.getAngle(), 150.0d, 0.001);

        vector = new Vector(pp(-2, -2), pp(-2.866, -1.5));

        Assert.assertEquals(vector.getLength(), 1.d, 0.001);
        Assert.assertEquals(vector.getAngle(), 150.0d, 0.001);
    }

    @Test
    public void testAngle225() {
        Vector vector = new Vector(pp(1, 1), pp(0, 0));

        Assert.assertEquals(vector.getLength(), 1.414d, 0.001);
        Assert.assertEquals(vector.getAngle(), -135.0d, 0.001);

        vector = new Vector(pp(-2, -2), pp(-3, -3));

        Assert.assertEquals(vector.getLength(), 1.414d, 0.001);
        Assert.assertEquals(vector.getAngle(), -135.0d, 0.001);
    }

    @Test
    public void testAngle270() {
        Vector vector = new Vector(pp(1, 1), pp(1, 0));

        Assert.assertEquals(vector.getLength(), 1.d, 0.001);
        Assert.assertEquals(vector.getAngle(), -90.0d, 0.001);

        vector = new Vector(pp(-2, -2), pp(-2, -3));

        Assert.assertEquals(vector.getLength(), 1.d, 0.001);
        Assert.assertEquals(vector.getAngle(), -90.0d, 0.001);
    }

    @Test
    public void testAngle315() {
        Vector vector = new Vector(pp(1, 1), pp(2, 0));

        Assert.assertEquals(vector.getLength(), 1.414d, 0.001);
        Assert.assertEquals(vector.getAngle(), -45.0d, 0.001);

        vector = new Vector(pp(-2, -2), pp(-1, -3));

        Assert.assertEquals(vector.getLength(), 1.414d, 0.001);
        Assert.assertEquals(vector.getAngle(), -45.0d, 0.001);
    }


}
