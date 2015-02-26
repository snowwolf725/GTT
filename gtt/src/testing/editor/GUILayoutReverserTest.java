package testing.editor;

import gtt.eventmodel.EventModelFactory;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.util.reverser.GUILayoutReverser;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import junit.framework.TestCase;

public class GUILayoutReverserTest extends TestCase {

	GUILayoutReverser m_layout;

	protected void setUp() throws Exception {
		super.setUp();
		m_layout = new GUILayoutReverser();
		// 重新初始化
		EventModelFactory.getDefault().initialize("descriptor/swing.xml");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		m_layout = null;
	}

	public void testGetRoot() {
		assertNotNull(m_layout.getRoot());
		assertEquals(m_layout.getRoot().size(), 0);
	}

	public void testReverse_four_JMenu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setName("menuBar");
		JMenu menu;
		// Build the first menu.
		menu = new JMenu("A Menu");
		menuBar.add(menu);
		menu = new JMenu("B Menu");
		menuBar.add(menu);
		menu = new JMenu("C Menu");
		menuBar.add(menu);
		menu = new JMenu("D Menu");
		menuBar.add(menu);

		m_layout.reverse(menuBar);
		AbstractMacroNode node = m_layout.getRoot();

		assertEquals(node.getName(), "GUI Hierarchy");
		assertEquals(node.size(), 1);

		node = node.get(0);
		assertEquals(node.getName(), "menuBar");
		assertEquals(node.size(), 5);

		assertEquals("menuBar", node.get(0).getName());

		for (int i = 1; i < node.size(); i++) {
			AbstractMacroNode menuA = node.get(i);
			assertEquals("javax.swing.JMenu", menuA.getName());
			assertEquals(menuA.size(), 1);
		}
	}

	public void testReverseJMenu_deep_JMenu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setName("menuBar");

		JMenu menu;
		// Build the first menu.
		menu = new JMenu("File");
		menu.setName("FileMenu");
		menuBar.add(menu);

		JMenuItem item;
		item = new JMenuItem("NewFile");
		item.setName("NewFile");
		menu.add(item);

		item = new JMenuItem("OpenFile");
		item.setName("OpenFile");
		menu.add(item);

		item = new JMenuItem("SaveFile");
		item.setName("SaveFile");
		menu.add(item);

		m_layout.reverse(menuBar);
		AbstractMacroNode node = m_layout.getRoot();

		assertEquals(node.getName(), "GUI Hierarchy");
		assertEquals(node.size(), 1);

		node = node.get(0);
		assertEquals(node.getName(), "menuBar");
		assertEquals(node.size(), 2);

		assertEquals(menu.getMenuComponentCount(), 3);

		node = node.get(1);
		assertEquals(node.getName(), "FileMenu");
		assertEquals(node.size(), 4);
	}

	public void testReverseToolbar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setName("ToolBar");

		JButton btn;
		btn = new JButton();
		btn.setName("btn1");
		toolBar.add(btn);

		btn = new JButton();
		btn.setName("btn2");
		toolBar.add(btn);

		btn = new JButton();
		btn.setName("btn3");
		toolBar.add(btn);

		m_layout.reverse(toolBar);
		AbstractMacroNode node = m_layout.getRoot();

		assertEquals(node.getName(), "GUI Hierarchy");
		assertEquals(node.size(), 1);

		node = node.get(0);
		assertEquals(node.getName(), "ToolBar");
		assertEquals(node.size(), 4);

		assertEquals(node.get(0).getName(), "ToolBar");
		assertEquals(node.get(0).size(), 0);
		assertEquals(node.get(1).getName(), "btn1");
		assertEquals(node.get(1).size(), 0);
		assertEquals(node.get(2).getName(), "btn2");
		assertEquals(node.get(2).size(), 0);
		assertEquals(node.get(3).getName(), "btn3");
		assertEquals(node.get(3).size(), 0);

	}

}
