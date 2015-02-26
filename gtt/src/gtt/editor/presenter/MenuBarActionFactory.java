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
package gtt.editor.presenter;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

/*******************************************************************************
 * support MainEditor MenuBar zws 2007/03/14
 */
public class MenuBarActionFactory {

	private final String IMAGE_DIRECTORY = System.getProperty("user.dir")
			+ "/images/";

	// logic 全交由 presenter 去做
	IMenuBarPresenter presenter;

	public MenuBarActionFactory(IMenuBarPresenter pr) {
		presenter = pr;
	}

	// counting
	public Action getNodeCounting() {
		return new AbstractAction("Nodes Counting", new ImageIcon(
				IMAGE_DIRECTORY + "macroMode.gif")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.doNodesCounting();
			}
		};
	}

	// Macro statistics
	public Action getMacroStatistics() {
		return new AbstractAction("Macro Statistics", new ImageIcon(
				IMAGE_DIRECTORY + "macroMode.gif")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.doMacroStatistics();
			}
		};
	}

	// component abstraction
	public Action getComponentAbstraction() {
		return new AbstractAction("reverse GUI hierarchy", new ImageIcon(
				IMAGE_DIRECTORY + "macroMode.gif")) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.doGUIHierarchyReverse();
			}
		};
	}

	public Action getAbout() {
		return new AbstractAction("About", new ImageIcon(IMAGE_DIRECTORY
				+ "about.gif")) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				// nothing
			}
		};
	}

	public Action getLaunchAUT() {
		return new AbstractAction("Launch AUT", new ImageIcon(IMAGE_DIRECTORY
				+ "openAUT.gif")) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.launchAUT();
			}
		};
	}

	public Action getTerminateAUT() {
		return new AbstractAction("Terminate AUT", new ImageIcon(IMAGE_DIRECTORY
				+ "reopenAUT.gif")) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.terimentAUT();
			}
		};

	}

	public Action getConfiguration() {
		return new AbstractAction("Configuration", new ImageIcon(
				IMAGE_DIRECTORY + "configuration.gif")) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.configuration();
			}
		};
	}

	public Action getGTTAbout() {
		return new AbstractAction("About GTT", new ImageIcon(IMAGE_DIRECTORY
				+ "about.gif")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				// doAbout();
			}
		};
	}

	/***************************************************************************
	 * Capture/Replay
	 */
	public Action getReplay() {
		return new AbstractAction("Replay", new ImageIcon(IMAGE_DIRECTORY
				+ "replay2.gif")) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.replay();
			}
		};
	}

	public Action getStopCapture() {
		return new AbstractAction("Stop", new ImageIcon(IMAGE_DIRECTORY
				+ "stop2.gif")) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.stopCapture();
			}
		};
	}

	public Action getStartCapture() {
		return new AbstractAction("Capture", new ImageIcon(IMAGE_DIRECTORY
				+ "record2.gif")) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.startCapture();
			}

		};
	}

	/***************************************************************************
	 * File Toolbar
	 */
	public Action getFileNew() {
		return new AbstractAction("New", new ImageIcon(IMAGE_DIRECTORY
				+ "newFile.gif")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.newFile();
			}
		};
	}

	public Action getFileSaveAs() {
		// = = = = = = = = = = = = = = = = = =
		return new AbstractAction("Save As", new ImageIcon(IMAGE_DIRECTORY
				+ "saveAsFile.gif")) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.saveAsFile();
			}
		};
	}

	public Action getFileSave() {
		return new AbstractAction("Save", new ImageIcon(IMAGE_DIRECTORY
				+ "saveFile.gif")) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.saveFile();
			}
		};
	}

	public Action getSaveGTMLScript() {
		return new AbstractAction("Save GTML", new ImageIcon(IMAGE_DIRECTORY
				+ "macroMode.gif")) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.saveGTMLScript();
			}
		};
	}

	// public Action getCreateMETS() {
	// return new AbstractAction("Test Case Generation", new
	// ImageIcon(IMAGE_DIRECTORY
	// + "manualInsert.gif")) {
	//
	// private static final long serialVersionUID = 1L;
	//
	// public void actionPerformed(ActionEvent e) {
	// presenter.createMETS();
	// }
	// };
	// }

	// public Action getCalculateContractLevel() {
	// return new AbstractAction("Calculate Contract Lv", new
	// ImageIcon(IMAGE_DIRECTORY
	// + "CalContractLevel.gif")) {
	//
	// private static final long serialVersionUID = 1L;
	//
	// public void actionPerformed(ActionEvent e) {
	// presenter.calculateContractLevel();
	// }
	// };
	// }

	public Action getFileOpen() {
		return new AbstractAction("Open", new ImageIcon(IMAGE_DIRECTORY
				+ "openFile.gif")) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.openFile();
			}
		};
	}

	public Action getFileExit() {
		return new AbstractAction("Exit") {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
	}

	/**
	 * Script actions
	 */
	public Action getScriptReset() {
		return new AbstractAction("Reset script", new ImageIcon(IMAGE_DIRECTORY
				+ "resetScript.gif")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.resetTestScript();
			}
		};
	}

	/***************************************************************************
	 * zws 2007/05/23
	 */
	public Action getMacroToScript() {
		return new AbstractAction("macro to test script", new ImageIcon(
				IMAGE_DIRECTORY + "insertToScript.gif")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.macroToScript();
			}
		};
	}

	/***************************************************************************
	 * zws 2007/05/23
	 */
	public Action getScriptToMacro() {
		return new AbstractAction("test script to macro", new ImageIcon(
				IMAGE_DIRECTORY + "cloneFromScript.gif")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.ScriptToMacro();
			}
		};
	}

	// Test-Case Generation
	public Action getStateCoverageTestGen() {
		return new AbstractAction("SC Test Generation", new ImageIcon(
				IMAGE_DIRECTORY + "test3.gif")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.generateSCTestCsae();
			}
		};
	}

	public Action getEdgeCoverageTestGen() {
		return new AbstractAction("EC Test Generation", new ImageIcon(
				IMAGE_DIRECTORY + "test3.gif")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.generateECTestCsae();
			}
		};
	}

	public Action getCEdgeCoverageTestGen() {
		return new AbstractAction("CC Test Generation", new ImageIcon(
				IMAGE_DIRECTORY + "test3.gif")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				presenter.generateCCTestCsae();
			}
		};
	}

}
