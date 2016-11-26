package com.example.adam.objects_in_air;

import java.util.Arrays;

/**
 * Created by szkol_000 on 26.11.2016.
 */

public class ObjectInAir {
    String name;
    float[] position;

    public ObjectInAir(String name, float[] position) {
        this.name = name;
        this.position = position;
    }

    @Override
    public String toString() {
        return "ObjectInAir{" +
                "name='" + name + '\'' +
                ", position=" + Arrays.toString(position) +
                '}';
    }
}
