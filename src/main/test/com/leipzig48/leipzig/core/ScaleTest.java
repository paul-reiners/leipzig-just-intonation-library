/*
 * Created on Oct 27, 2004
 */
package com.leipzig48.leipzig.core;

import junit.framework.TestCase;

/**
 * @author Paul Reiners
 */
public class ScaleTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(ScaleTest.class);
	}

	public void testComplement() {
		Scale cMajor = new Scale(new Interval[] { new Interval(8, 9),
				new Interval(9, 10), new Interval(15, 16), new Interval(8, 9),
				new Interval(9, 10), new Interval(8, 9), new Interval(15, 16) });
		Scale descendingCPhrygianFMinor = new Scale(new Interval[] {
				new Interval(9, 8), new Interval(10, 9), new Interval(16, 15),
				new Interval(9, 8), new Interval(10, 9), new Interval(9, 8),
				new Interval(16, 15) });
		assertEquals(descendingCPhrygianFMinor, cMajor.complement());
	}
}