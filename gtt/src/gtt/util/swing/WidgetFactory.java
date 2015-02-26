/*
 * Copyright (C) 2006-2009
 * Woei-Kae Chen <wkc@csie.ntut.edu.tw>
 * Hung-Shing Chao <s9598007@ntut.edu.tw>
 * Tung-Hung Tsai <s159020@ntut.edu.tw>
 * Zhe-Ming Zhang <s2598001@ntut.edu.tw>
 * Zheng-Wen Shen <zwshen0603@gmail.com>
 * Jung-Chi Wang <snowwolf725@gmail.com>
 *
 * This file is part of GTT (GUI Testing Tool) Software.
 *
 * GTT is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GTT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * GNU GENERAL PUBLIC LICENSE http://www.gnu.org/licenses/gpl
 */
package gtt.util.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

public class WidgetFactory {
	public static JButton getButton(Action action, String imageUrl,
			String rolloverUrl, String pressedUrl, String title,
			String toolTipText, int width, int height, boolean borderPainted) {
		JButton b = new JButton() {
			private static final long serialVersionUID = -3261516347726238049L;

			public Point getToolTipLocation(MouseEvent e) {
				Dimension size = getSize();
				return new Point(0, size.height);
			}
		};

		if (action != null)
			b.setAction(action);
		if (rolloverUrl != null)
			b.setRolloverIcon(new ImageIcon(rolloverUrl));
		if (imageUrl != null)
			b.setIcon(new ImageIcon(imageUrl));
		if (pressedUrl != null)
			b.setPressedIcon(new ImageIcon(pressedUrl));
		if (title != null)
			b.setText(title);
		if (toolTipText != null)
			b.setToolTipText(toolTipText);
		b.setMargin(new Insets(0, 0, 0, 0));
		setupSize(b, width, height);
		b.setBorderPainted(borderPainted);
		// b.setContentAreaFilled(true);
		b.setOpaque(false);
		// b.setSelectedIcon(new ImageIcon(rolloverUrl));
		return b;
	}

	public static void setupJTextComponent(JTextComponent text, String name,
			int width, int height) {
		text.setText(name);
		text.setFont(new Font("Tahoma", Font.TRUETYPE_FONT, 12));
		setupSize(text, width, height);;
	}

	private static void createJLabel(JLabel label, String name, int width,
			int height, boolean alignCenter) {
		label.setText(name);
		label.setName(name);
		label.setFont(new Font("Tahoma", Font.TRUETYPE_FONT, 12));
		setupSize(label, width, height);
		if (alignCenter)
			label.setHorizontalAlignment(SwingConstants.CENTER);
	}

	public static JLabel createJLabel(String name, int width, int height,
			boolean alignCenter) {
		JLabel label = new JLabel();
		createJLabel(label, name, width, height, alignCenter);
		return label;
	}

	public static void setupSize(JComponent comp, int width, int height) {
		comp.setPreferredSize(new Dimension(width, height));
		comp.setMaximumSize(new Dimension(width, height));
		comp.setMinimumSize(new Dimension(width, height));
	}

	public static void setupBorder(JComponent comp, int width) {
		comp.setBorder(new LineBorder(Color.black, width, false));
	}

	public static void setupJButton(JButton btn, String name, int width,
			int height) {
		btn.setText(name);
		btn.setName(name);
		btn.setFont(new Font("Tahoma", Font.TRUETYPE_FONT, 12));
		btn.setPreferredSize(new Dimension(width, height));
		btn.setMaximumSize(new Dimension(width, height));
		btn.setMinimumSize(new Dimension(width, height));
	}

	public static JButton createJButton(String name, int width, int height) {
		JButton btn = new JButton();
		setupJButton(btn, name, width, height);
		return btn;
	}

	public static void placeCenter(Window window) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation((dim.width - window.getWidth()) / 2,
				(dim.height - window.getHeight()) / 2);
	}
}