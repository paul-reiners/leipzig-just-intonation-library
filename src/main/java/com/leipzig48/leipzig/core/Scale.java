/*
 * Created on Oct 27, 2004
 */
package com.leipzig48.leipzig.core;

import java.util.Arrays;

/**
 * @author Paul Reiners
 */
class Scale {

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof Scale) {
            Scale other = (Scale) obj;

            return Arrays.equals(other.intervals, intervals);
        }

        return false;
    }

    private Interval[] intervals;

    Scale(Interval[] intervals) {
        this.intervals = intervals;
    }

    Scale complement() {
        Interval[] newIntervals = new Interval[intervals.length];
        for (int i = 0; i < newIntervals.length; i++) {
            newIntervals[i] = intervals[i].invert();
        }

        return new Scale(newIntervals);
    }
}