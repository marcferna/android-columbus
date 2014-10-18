package com.codepath.columbus.columbus.utils;

import com.codepath.columbus.columbus.models.Exhibit;

import java.util.Comparator;

public class ExhibitDistanceComparator implements Comparator<Exhibit> {
    public int compare(Exhibit item1, Exhibit item2) {
        return Double.compare(item2.getDistance(), item1.getDistance());
    }
}
