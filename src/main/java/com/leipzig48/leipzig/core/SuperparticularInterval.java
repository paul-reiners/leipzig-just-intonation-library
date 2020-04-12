/*
 * Created on Dec 4, 2004
 *
 * Leipzig: A Just Intonation Library
 * Copyright (C) 2004 Paul Reiners
 *
 * This library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Contact Info:
 *
 * 	Paul Reiners
 * 	2506 18 1/2 Ave NW
 * 	Apt 206
 * 	Rochester, MN  55901
 *
 * 	paulreiners@earthlink.net
 */
package com.leipzig48.leipzig.core;

import java.math.BigInteger;

/**
 * @author Paul Reiners
 */
class SuperparticularInterval extends Interval {

    final static SuperparticularInterval OCTAVE = new SuperparticularInterval(
            2, true);

    final static SuperparticularInterval PERFECT_FOURTH = new SuperparticularInterval(
            4, true);

    final static SuperparticularInterval PERFECT_FIFTH = new SuperparticularInterval(
            3, true);

    SuperparticularInterval(long numerator, boolean denominatorIsSmaller) {
        this.numerator = new BigInteger(Long.toString(numerator));
        if (denominatorIsSmaller) {
            this.denominator = new BigInteger(Long.toString(numerator - 1));
        } else {
            this.denominator = new BigInteger(Long.toString(numerator + 1));
        }
    }

    /**
     * @param numParts
     * @return
     */
    Interval[] divideIntoSpecifiedNumberOfParts(int numParts) {
        long upper = numerator.longValue() * numParts;
        long lower = denominator.longValue() * numParts;
        return divideIntoParts(upper, lower);
    }
}