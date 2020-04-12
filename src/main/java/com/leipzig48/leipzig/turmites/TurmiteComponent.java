/*
 * Created on Dec 19, 2004
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
package com.leipzig48.leipzig.turmites;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

/**
 * @author Paul Reiners
 */
class TurmiteComponent extends JComponent {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3833465098051597878L;

	private String title;

	public TurmiteComponent(String title) {
		this.title = title;
		setTitle();
	}

	final static Color[] COLORS = { Color.blue, Color.black, Color.red,
			Color.yellow, Color.green, Color.cyan, Color.orange, Color.pink,
			Color.magenta, Color.lightGray };

	void setTitle() {
		setBorder(new TitledBorder(title));
	}

	void clearWorld() {
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Dimension d = getSize();
		int gridWidth = d.width;
		int gridHeight = d.height;

		g2.clearRect(0, 0, gridWidth, gridHeight);
		setTitle();
	}

	void updateWorld(Turmite[] turmites, int[] oldTurmiteXs, int[] oldTurmiteYs) {
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		if (g2 == null) {
			System.err.println("Error: Graphics is null!");

			return;
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Dimension d = getSize();
		int gridWidth = d.width;
		int gridHeight = d.height;

		int yRadius = turmites[0].getYRadius();
		int xRadius = turmites[0].getXRadius();
		int worldWidth = 2 * xRadius + 1;
		int worldHeight = 2 * yRadius + 1;
		int rectWidth = gridWidth / worldWidth;
		int rectHeight = gridHeight / worldHeight;

		for (int i = 0; i < turmites.length; i++) {
			int iTimes = yRadius - oldTurmiteYs[i];
			int x = worldWidth / 2 * rectWidth - gridWidth / 2 + rectWidth / 2
					+ iTimes * rectWidth;
			int jTimes = oldTurmiteXs[i] + xRadius;
			int y = jTimes * rectHeight;
			Rectangle2D rectangle = new Rectangle2D.Float(x, y, rectWidth,
					rectHeight);
			g2.setPaint(COLORS[turmites[i].getColor(oldTurmiteXs[i],
					oldTurmiteYs[i])]);
			g2.fill(rectangle);
		}
	}
}