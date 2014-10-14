package com.codepath.columbus.columbus.models;

import java.util.Comparator;

public class ExhibitDistanceComparator implements Comparator<Exhibit> {
    public int compare(Exhibit item1, Exhibit item2) {
        return item2.getDistance() - item1.getDistance();
    }
}
