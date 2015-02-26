import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.text.rtf.*;
import javax.swing.undo.*;


public class WordProcessor extends JFrame {
	protected JTextPane m_monitor;

	protected StyleContext m_context;

	protected DefaultStyledDocument m_doc;

	protected RTFEditorKit m_kit;

	protected JFileChooser m_chooser;

	protected SimpleFilter m_rtfFilter;

	protected JToolBar m_toolBar;

	protected JComboBox m_cbFonts;

	protected JComboBox m_cbSizes;

	protected SmallToggleButton m_bBold;

	protected SmallToggleButton m_bItalic;

	protected String m_fontName = "";

	protected int m_fontSize = 0;

	protected boolean m_skipUpdate;

	protected int m_xStart = -1;

	protected int m_xFinish = -1;

	protected SimpleFilter m_jpgFilter;

	protected SimpleFilter m_gifFilter;

	protected ColorMenu m_foreground;

	protected ColorMenu m_background;

	protected JComboBox m_cbStyles;
	protected Hashtable m_styles;

	protected UndoManager m_undo = new UndoManager();

	protected Action m_undoAction;

	protected Action m_redoAction;

	protected String[] m_fontNames;

	protected String[] m_fontSizes;

	protected FontDialog m_fontDialog;
	
	protected SmallButton bNew;
	
	/* add for testing */
	public Color getBackColor(){
		return m_background.getColor();
	}
	
	/* add for testing */
	public void setColorPaneNull(){
		m_background.setColorPane_null();
	}
	
	/* add for testing */
	public void getMaxSize(){
		m_fontDialog.getMaxSize();
	}
	
	/* add for testing */
	public void testColorPane(){
		m_background.testColorPane();
	}
	
	/* add for testing */
	public void testundo(){
		m_undoAction.actionPerformed(null);
	}
	
	/* add for testing */
	public void testredo(){
		m_redoAction.actionPerformed(null);
	}
	
	/* add for testing */
	public void testAttr_null(){
		m_fontDialog.m_attributes = null;
		m_fontDialog.getAttributes();
	}
	
	/* add for testing */
	public void testFilter_null(){
		m_rtfFilter.accept(null);
	}
	
	/* add for testing */
	public void testToggleBtn(){
		new SmallToggleButton(true, null, null, "Bold font");
	}
	
	/* add for testing */
	public void testSmallBtn(){
		bNew.mousePressed(null);
		bNew.mouseReleased(null);
		bNew.mouseClicked(null);
	}
	
	/* add for testing */
	public void testColorMenuEvent(){
		  ActionListener [] lsts = m_foreground.getActionListeners();
		  Color c = new Color(255,255,255);
		  m_foreground.setColor(c);
		  lsts[0].actionPerformed(null);
		  MenuListener [] ms = m_foreground.getMenuListeners();
		  ms[0].menuCanceled(null);
		  
		  lsts = m_background.getActionListeners();
		  m_background.setColor(c);
		  lsts[0].actionPerformed(null);
		  ms = m_background.getMenuListeners();
		  ms[0].menuCanceled(null);
	  }
	
	public void testColorCombo(){
		ColorComboRenderer combo = new ColorComboRenderer();
		combo.getListCellRendererComponent(null, null, 0, true, true);
	}

	public WordProcessor() {
		super("RTF Word Processor");
		setSize(600, 400);

		m_monitor = new JTextPane();
		m_kit = new RTFEditorKit();
		m_monitor.setEditorKit(m_kit);
		m_context = new StyleContext(); // 本文的實體
		m_doc = new DefaultStyledDocument(m_context);
		m_monitor.setDocument(m_doc);

		JScrollPane ps = new JScrollPane(m_monitor);
		ps.setName("textPaneScrollPane");
		getContentPane().add(ps, BorderLayout.CENTER);

		JMenuBar menuBar = createMenuBar();
		setJMenuBar(menuBar);

		m_chooser = new JFileChooser();
		m_chooser.setCurrentDirectory(new File("."));
		m_rtfFilter = new SimpleFilter("rtf", "RTF Documents");
		m_chooser.setFileFilter(m_rtfFilter);

		m_gifFilter = new SimpleFilter("gif", "GIF images");
		m_jpgFilter = new SimpleFilter("jpg", "JPG images");

		setAllComponentName();

		CaretListener lst = new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				showAttributes(e.getDot());
			}
		};
		m_monitor.addCaretListener(lst);

		FocusListener flst = new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (m_xStart >= 0 && m_xFinish >= 0)
					/*
					 * if (m_monitor.getCaretPosition()==m_xStart) {
					 * m_monitor.setCaretPosition(m_xFinish);
					 * m_monitor.moveCaretPosition(m_xStart); } else
					 */
					m_monitor.select(m_xStart, m_xFinish);
			}

			public void focusLost(FocusEvent e) {
				m_xStart = m_monitor.getSelectionStart();
				m_xFinish = m_monitor.getSelectionEnd();
			}
		};
		m_monitor.addFocusListener(flst);

		WindowListener wndCloser = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitFrame();
			}
		};
		addWindowListener(wndCloser);

		showAttributes(0);
		showStyles();
		m_doc.addUndoableEditListener(new Undoer());
		setVisible(true);
	}
	
	public void exitFrame(){
		this.dispose();
	}

	private void setAllComponentName() { // XXX Word processor Name
		setName("RTF Word Processor");
		m_monitor.setName("textPane");
		m_chooser.setName("fileChooser");
		m_toolBar.setName("toolBar");
		m_cbFonts.setName("FontName");
		m_cbSizes.setName("FontSize");
		m_fontDialog.setName("FontDialog");
		m_bBold.setName("BoldButton");
		m_bItalic.setName("ItalicButton");
		m_foreground.setName("menuSelectForeground");
		m_background.setName("menuSelectBackground");
		m_cbStyles.setName("Style");
	}

	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu mFile = new JMenu("File");
		mFile.setName("menuFile");
		mFile.setMnemonic('f');

		ImageIcon iconNew = new ImageIcon("images/file_new.gif");
		Action actionNew = new AbstractAction("New", iconNew) {
			public void actionPerformed(ActionEvent e) {
				m_doc = new DefaultStyledDocument(m_context);
				m_monitor.setDocument(m_doc);
				showAttributes(0);
				showStyles();
				m_doc.addUndoableEditListener(new Undoer());
			}
		};
		JMenuItem item = mFile.add(actionNew);
		item.setName("menuNew");
		item.setMnemonic('n');

		ImageIcon iconOpen = new ImageIcon("images/file_open.gif");
		Action actionOpen = new AbstractAction("Open...", iconOpen) {
			public void actionPerformed(ActionEvent e) 
			{
				//WordProcessor.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				//Thread runner = new Thread() {
					//public void run() {
						if (m_chooser.showOpenDialog(WordProcessor.this) != JFileChooser.APPROVE_OPTION)
							return;
						WordProcessor.this.repaint();
						File fChoosen = m_chooser.getSelectedFile();

						// Recall that text component read/write operations are
						// thread safe. Its ok to do this in a separate thread.
						try {
							InputStream in = new FileInputStream(fChoosen);
							m_doc = new DefaultStyledDocument(m_context);
							m_kit.read(in, m_doc, 0);
							m_monitor.setDocument(m_doc);
							in.close();
							showAttributes(0);
							showStyles();
							m_doc.addUndoableEditListener(new Undoer());
						} catch (Exception ex) {
							// ex.printStackTrace();
							System.out
									.println("Problems encountered: Note that RTF"
											+ " support is still under development.");
						}
						WordProcessor.this.setCursor(Cursor
								.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					//}
				//};
				//runner.start();
			}
		};
		item = mFile.add(actionOpen);
		item.setName("menuOpen");
		item.setMnemonic('o');

		ImageIcon iconSave = new ImageIcon("images/file_save.gif");
		Action actionSave = new AbstractAction("Save...", iconSave) {
			public void actionPerformed(ActionEvent e) {
//				WordProcessor.this.setCursor(Cursor
//						.getPredefinedCursor(Cursor.WAIT_CURSOR));
//				Thread runner = new Thread() {
//					public void run() {
						if (m_chooser.showSaveDialog(WordProcessor.this) != JFileChooser.APPROVE_OPTION)
							return;
						WordProcessor.this.repaint();
						File fChoosen = m_chooser.getSelectedFile();

						// Recall that text component read/write operations are
						// thread safe. Its ok to do this in a separate thread.
						try {
							OutputStream out = new FileOutputStream(fChoosen);
							m_kit.write(out, m_doc, 0, m_doc.getLength());
							out.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						// Make sure chooser is updated to reflect new file
						m_chooser.rescanCurrentDirectory();
						WordProcessor.this.setCursor(Cursor
								.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//					}
//				};
//				runner.start();
			}
		};
		item = mFile.add(actionSave);
		item.setName("menuSave");
		item.setMnemonic('s');

		mFile.addSeparator();

		Action actionExit = new AbstractAction("Exit") {
			public void actionPerformed(ActionEvent e) {
				exitFrame();
			}
		};

		item = mFile.add(actionExit);
		item.setName("menuExit");
		item.setMnemonic('x');
		menuBar.add(mFile);

		m_toolBar = new JToolBar();
		bNew = new SmallButton(actionNew, "New document");
		bNew.setName("NewButton");
		m_toolBar.add(bNew);

		JButton bOpen = new SmallButton(actionOpen, "Open RTF document");
		bOpen.setName("OpenButton");
		m_toolBar.add(bOpen);

		JButton bSave = new SmallButton(actionSave, "Save RTF document");
		bSave.setName("SaveButton");
		m_toolBar.add(bSave);

		JMenu mEdit = new JMenu("Edit");
		mEdit.setName("menuEdit");
		mEdit.setMnemonic('e');

		Action action = new AbstractAction("Copy", new ImageIcon(
				"images/edit_copy.gif")) {
			public void actionPerformed(ActionEvent e) {
				m_monitor.copy();
			}
		};
		item = mEdit.add(action);
		item.setName("menuCopy");
		item.setMnemonic('c');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				KeyEvent.CTRL_MASK));

		action = new AbstractAction("Cut", new ImageIcon("images/edit_cut.gif")) {
			public void actionPerformed(ActionEvent e) {
				m_monitor.cut();
			}
		};
		item = mEdit.add(action);
		item.setName("menuCut");
		item.setMnemonic('t');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				KeyEvent.CTRL_MASK));

		action = new AbstractAction("Paste", new ImageIcon(
				"images/edit_paste.gif")) {
			public void actionPerformed(ActionEvent e) {
				m_monitor.paste();
			}
		};
		item = mEdit.add(action);
		item.setName("menuPaste");
		item.setMnemonic('p');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				KeyEvent.CTRL_MASK));

		mEdit.addSeparator();

		m_undoAction = new AbstractAction("Undo", new ImageIcon(
				"images/edit_undo.gif")) {
			public void actionPerformed(ActionEvent e) {
				try {
					m_undo.undo();
				} catch (CannotUndoException ex) {
					System.err.println("Unable to undo: " + ex);
				}
				updateUndo();
			}
		};
		item = mEdit.add(m_undoAction);
		item.setName("menuUndo");
		item.setMnemonic('u');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.CTRL_MASK));

		m_redoAction = new AbstractAction("Redo", new ImageIcon(
				"images/edit_redo.gif")) {
			public void actionPerformed(ActionEvent e) {
				try {
					m_undo.redo();
				} catch (CannotRedoException ex) {
					System.err.println("Unable to redo: " + ex);
				}
				updateUndo();
			}
		};
		item = mEdit.add(m_redoAction);
		item.setName("menuRedo");
		item.setMnemonic('r');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				KeyEvent.CTRL_MASK));

		menuBar.add(mEdit);

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		m_fontNames = ge.getAvailableFontFamilyNames();

		m_toolBar.addSeparator();
		m_cbFonts = new JComboBox(m_fontNames);
		m_cbFonts.setMaximumSize(m_cbFonts.getPreferredSize());
		m_cbFonts.setEditable(true);

		ActionListener lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_fontName = m_cbFonts.getSelectedItem().toString();
				MutableAttributeSet attr = new SimpleAttributeSet();
				StyleConstants.setFontFamily(attr, m_fontName);
				setAttributeSet(attr);
				m_monitor.grabFocus();
			}
		};
		m_cbFonts.addActionListener(lst);
		m_toolBar.add(m_cbFonts);

		m_toolBar.addSeparator();
		m_fontSizes = new String[] { "8", "9", "10", "11", "12", "14", "16",
				"18", "20", "22", "24", "26", "28", "36", "48", "72" };
		m_cbSizes = new JComboBox(m_fontSizes);
		m_cbSizes.setMaximumSize(m_cbSizes.getPreferredSize());
		m_cbSizes.setEditable(true);

		m_fontDialog = new FontDialog(this, m_fontNames, m_fontSizes);
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fontSize = 0;
				try {
					fontSize = Integer.parseInt(m_cbSizes.getSelectedItem()
							.toString());
				} catch (NumberFormatException ex) {
					return;
				}

				m_fontSize = fontSize;
				MutableAttributeSet attr = new SimpleAttributeSet();
				StyleConstants.setFontSize(attr, fontSize);
				setAttributeSet(attr);
				m_monitor.grabFocus();
			}
		};
		m_cbSizes.addActionListener(lst);
		m_toolBar.add(m_cbSizes);

		m_toolBar.addSeparator();
		ImageIcon img1 = new ImageIcon("images/font_bold1.gif");
		ImageIcon img2 = new ImageIcon("images/font_bold2.gif");
		m_bBold = new SmallToggleButton(false, img1, img2, "Bold font");
		m_bBold.setPreferredSize(new Dimension(20, 20));
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MutableAttributeSet attr = new SimpleAttributeSet();
				StyleConstants.setBold(attr, m_bBold.isSelected());
				setAttributeSet(attr);
				m_monitor.grabFocus();
			}
		};
		m_bBold.addActionListener(lst);
		m_toolBar.add(m_bBold);

		img1 = new ImageIcon("images/font_italic1.gif");
		img2 = new ImageIcon("images/font_italic2.gif");
		m_bItalic = new SmallToggleButton(false, img1, img2, "Italic font");
		m_bItalic.setPreferredSize(new Dimension(20, 20));
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MutableAttributeSet attr = new SimpleAttributeSet();
				StyleConstants.setItalic(attr, m_bItalic.isSelected());
				setAttributeSet(attr);
				m_monitor.grabFocus();
			}
		};
		m_bItalic.addActionListener(lst);
		m_toolBar.add(m_bItalic);

		JMenu mFormat = new JMenu("Format");
		mFormat.setName("menuFormat");
		mFormat.setMnemonic('o');

		item = new JMenuItem("Font...");
		item.setName("menuFont");
		item.setMnemonic('o');
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WordProcessor.this.repaint();
				AttributeSet a = m_doc.getCharacterElement(
						m_monitor.getCaretPosition()).getAttributes();
				m_fontDialog.setAttributes(a);

				Dimension d1 = m_fontDialog.getSize();
				Dimension d2 = WordProcessor.this.getSize();
				int x = Math.max((d2.width - d1.width) / 2, 0);
				int y = Math.max((d2.height - d1.height) / 2, 0);
				m_fontDialog.setBounds(x + WordProcessor.this.getX(), y
						+ WordProcessor.this.getY(), d1.width, d1.height);

				m_fontDialog.setVisible(true);
				if (m_fontDialog.getOption() == JOptionPane.OK_OPTION) {
					setAttributeSet(m_fontDialog.getAttributes());
					showAttributes(m_monitor.getCaretPosition());
				}
			}
		};
		item.addActionListener(lst);
		mFormat.add(item);

		mFormat.addSeparator();

		JMenu mStyle = new JMenu("Style");
		mStyle.setName("menuStyle");
		mStyle.setMnemonic('s');
		mFormat.add(mStyle);

		item = new JMenuItem("Update");
		item.setName("menuUpdate");
		item.setMnemonic('u');
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = (String) m_cbStyles.getSelectedItem();
				Style style = m_doc.getStyle(name);
				int p = m_monitor.getCaretPosition();
				AttributeSet a = m_doc.getCharacterElement(p).getAttributes();
				style.addAttributes(a);
				m_monitor.repaint();
			}
		};
		item.addActionListener(lst);
		mStyle.add(item);

		item = new JMenuItem("Reapply");
		item.setName("menuReapply");
		item.setMnemonic('r');
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = (String) m_cbStyles.getSelectedItem();
				Style style = m_doc.getStyle(name);
				setAttributeSet(style);
			}
		};
		item.addActionListener(lst);
		mStyle.add(item);

		mFormat.addSeparator();

		m_foreground = new ColorMenu("Selection Foreground");
		m_foreground.setColor(m_monitor.getForeground());
		m_foreground.setMnemonic('f');
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MutableAttributeSet attr = new SimpleAttributeSet();
				StyleConstants.setForeground(attr, m_foreground.getColor());
				setAttributeSet(attr);
			}
		};
		m_foreground.addActionListener(lst);
		mFormat.add(m_foreground);

		MenuListener ml = new MenuListener() {
			public void menuSelected(MenuEvent e) {
				int p = m_monitor.getCaretPosition();
				AttributeSet a = m_doc.getCharacterElement(p).getAttributes();
				Color c = StyleConstants.getForeground(a);
				m_foreground.setColor(c);
			}

			public void menuDeselected(MenuEvent e) {
			}

			public void menuCanceled(MenuEvent e) {
			}
		};
		m_foreground.addMenuListener(ml);

		// Bug Alert! JEditorPane background color
		// doesn't work as of Java 2 FCS.
		m_background = new ColorMenu("Selection Background");
		m_background.setColor(m_monitor.getBackground());
		m_background.setMnemonic('b');
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MutableAttributeSet attr = new SimpleAttributeSet();
				StyleConstants.setBackground(attr, m_background.getColor());
				setAttributeSet(attr);
			}
		};
		m_background.addActionListener(lst);
		mFormat.add(m_background);

		ml = new MenuListener() {
			public void menuSelected(MenuEvent e) {
				int p = m_monitor.getCaretPosition();
				AttributeSet a = m_doc.getCharacterElement(p).getAttributes();
				Color c = StyleConstants.getBackground(a);
				m_background.setColor(c);
			}

			public void menuDeselected(MenuEvent e) {
			}

			public void menuCanceled(MenuEvent e) {
			}
		};
		m_background.addMenuListener(ml);

		// Bug Alert! Images do not get saved.
		mFormat.addSeparator();
		item = new JMenuItem("Insert Image");
		item.setName("menuInsertImage");
		item.setMnemonic('i');
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_chooser.addChoosableFileFilter(m_gifFilter);
				m_chooser.addChoosableFileFilter(m_jpgFilter);
				m_chooser.setFileFilter(m_gifFilter);
				m_chooser.removeChoosableFileFilter(m_rtfFilter);
				Thread runner = new Thread() {
					public void run() {
						if (m_chooser.showOpenDialog(WordProcessor.this) != JFileChooser.APPROVE_OPTION)
							return;
						WordProcessor.this.repaint();
						File fChoosen = m_chooser.getSelectedFile();
						ImageIcon icon = new ImageIcon(fChoosen.getPath());
						int w = icon.getIconWidth();
						int h = icon.getIconHeight();
						if (w <= 0 || h <= 0) {
							JOptionPane.showMessageDialog(WordProcessor.this,
									"Error reading image file\n"
											+ fChoosen.getPath(), "Warning",
									JOptionPane.WARNING_MESSAGE);
							return;
						}
						MutableAttributeSet attr = new SimpleAttributeSet();
						StyleConstants.setIcon(attr, icon);
						int p = m_monitor.getCaretPosition();
						try {
							m_doc.insertString(p, " ", attr);
						} catch (BadLocationException ex) {
						}

						// Its ok to do this outside of the event-dispatching
						// thread because the chooser is not visible here.
						m_chooser.addChoosableFileFilter(m_rtfFilter);
						m_chooser.setFileFilter(m_rtfFilter);
						m_chooser.removeChoosableFileFilter(m_gifFilter);
						m_chooser.removeChoosableFileFilter(m_jpgFilter);
					}
				};
				runner.start();
			}
		};
		item.addActionListener(lst);
		mFormat.add(item);

		menuBar.add(mFormat);

		m_toolBar.addSeparator();
		m_cbStyles = new JComboBox();
		m_cbStyles.addItem("Default");
		m_cbStyles.setMaximumSize(m_cbStyles.getPreferredSize());
		m_cbStyles.setEditable(true);
		m_toolBar.add(m_cbStyles);

		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (m_skipUpdate || m_cbStyles.getItemCount() == 0)
					return;
				String name = (String) m_cbStyles.getSelectedItem();
				int index = m_cbStyles.getSelectedIndex();
				int p = m_monitor.getCaretPosition();

				// New name entered
				if (index == -1) {
					m_cbStyles.addItem(name);
					Style style = m_doc.addStyle(name, null);
					AttributeSet a = m_doc.getCharacterElement(p)
							.getAttributes();
					style.addAttributes(a);
					return;
				}

				// Apply the selected style
				Style currStyle = m_doc.getLogicalStyle(p);
				if (!currStyle.getName().equals(name)) {
					Style style = m_doc.getStyle(name);
					setAttributeSet(style);
				}
			}
		};
		m_cbStyles.addActionListener(lst);

		getContentPane().add(m_toolBar, BorderLayout.NORTH);

		return menuBar;
	}

	protected void showAttributes(int p) {
		m_skipUpdate = true;
		AttributeSet a = m_doc.getCharacterElement(p).getAttributes();
		String name = StyleConstants.getFontFamily(a);
		if (!m_fontName.equals(name)) {
			m_fontName = name;
			m_cbFonts.setSelectedItem(name);
		}
		int size = StyleConstants.getFontSize(a);
		if (m_fontSize != size) {
			m_fontSize = size;
			m_cbSizes.setSelectedItem(Integer.toString(m_fontSize));
		}
		boolean bold = StyleConstants.isBold(a);
		if (bold != m_bBold.isSelected())
			m_bBold.setSelected(bold);
		boolean italic = StyleConstants.isItalic(a);
		if (italic != m_bItalic.isSelected())
			m_bItalic.setSelected(italic);

		Style style = m_doc.getLogicalStyle(p);
		name = style.getName();
		m_cbStyles.setSelectedItem(name);
		if (m_styles != null && m_styles.get(name) == null) {
			style = m_doc.addStyle(name, null);
			a = m_doc.getCharacterElement(p).getAttributes();
			style.addAttributes(a);
			m_styles.put(name, style);
		}

		m_skipUpdate = false;
	}

	protected void setAttributeSet(AttributeSet attr) {
		if (m_skipUpdate)
			return;
		int xStart = m_monitor.getSelectionStart();
		int xFinish = m_monitor.getSelectionEnd();
		if (!m_monitor.hasFocus()) {
			xStart = m_xStart;
			xFinish = m_xFinish;
		}
		if (xStart != xFinish) {
			m_doc.setCharacterAttributes(xStart, xFinish - xStart, attr, false);
		} else {
			MutableAttributeSet inputAttributes = m_kit.getInputAttributes();
			inputAttributes.addAttributes(attr);
		}
	}

	protected void showStyles() {
		m_skipUpdate = true;
		if (m_cbStyles.getItemCount() > 0)
			m_cbStyles.removeAllItems();
		Enumeration en = m_doc.getStyleNames();
		while (en.hasMoreElements()) {
			String str = en.nextElement().toString();
			m_cbStyles.addItem(str);
		}
		m_styles = new Hashtable();
		m_skipUpdate = false;
	}

	protected void updateUndo() {
		if (m_undo.canUndo()) {
			m_undoAction.setEnabled(true);
			m_undoAction
					.putValue(Action.NAME, m_undo.getUndoPresentationName());
		} else {
			m_undoAction.setEnabled(false);
			m_undoAction.putValue(Action.NAME, "Undo");
		}
		if (m_undo.canRedo()) {
			m_redoAction.setEnabled(true);
			m_redoAction
					.putValue(Action.NAME, m_undo.getRedoPresentationName());
		} else {
			m_redoAction.setEnabled(false);
			m_redoAction.putValue(Action.NAME, "Redo");
		}
	}

	public static void main(String argv[]) {
		if (argv.length == 0)
			new WordProcessor();
		else if (argv[0].indexOf("LayoutChange") != -1)
			new WordProcessorLayoutChange();
	}

	class Undoer implements UndoableEditListener {
		public Undoer() {
			m_undo.die();
			updateUndo();
		}

		public void undoableEditHappened(UndoableEditEvent e) {
			UndoableEdit edit = e.getEdit();
			m_undo.addEdit(e.getEdit());
			updateUndo();
		}

		public void testMethod1() {
		}

		public void testMethod2() {
		}
	}

	public void testMethod1() {
	}

	public void testMethod2() {
	}
}

// Class SmallButton unchanged from section 4.8

class SmallButton extends JButton implements MouseListener {
	protected Border m_raised;

	protected Border m_lowered;

	protected Border m_inactive;

	public SmallButton(Action act, String tip) {
		super((Icon) act.getValue(Action.SMALL_ICON));
		m_raised = new BevelBorder(BevelBorder.RAISED);
		m_lowered = new BevelBorder(BevelBorder.LOWERED);
		m_inactive = new EmptyBorder(2, 2, 2, 2);
		setBorder(m_inactive);
		setMargin(new Insets(1, 1, 1, 1));
		setToolTipText(tip);
		addActionListener(act);
		addMouseListener(this);
		setRequestFocusEnabled(false);
	}

	public float getAlignmentY() {
		return 0.5f;
	}

	public void mousePressed(MouseEvent e) {
		setBorder(m_lowered);
	}

	public void mouseReleased(MouseEvent e) {
		setBorder(m_inactive);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		setBorder(m_raised);
	}

	public void mouseExited(MouseEvent e) {
		setBorder(m_inactive);
	}

	public void testMethod1() {
	}

	public void testMethod2() {
	}
}

// Class SimpleFilter unchanged from section 14.1.9

class SimpleFilter extends javax.swing.filechooser.FileFilter {
	private String m_description = null;

	private String m_extension = null;

	public SimpleFilter(String extension, String description) {
		m_description = description;
		m_extension = "." + extension.toLowerCase();
	}

	public String getDescription() {
		return m_description;
	}

	public boolean accept(File f) {
		if (f == null)
			return false;
		if (f.isDirectory())
			return true;
		return f.getName().toLowerCase().endsWith(m_extension);
	}

	public void testMethod1() {
	}

	public void testMethod2() {
	}
}

// Class SmallToggleButton unchanged from section 4.8

class SmallToggleButton extends JToggleButton implements ItemListener {
	protected Border m_raised;

	protected Border m_lowered;

	public SmallToggleButton(boolean selected, ImageIcon imgUnselected,
			ImageIcon imgSelected, String tip) {
		super(imgUnselected, selected);
		setHorizontalAlignment(CENTER);
		setBorderPainted(true);
		m_raised = new BevelBorder(BevelBorder.RAISED);
		m_lowered = new BevelBorder(BevelBorder.LOWERED);
		setBorder(selected ? m_lowered : m_raised);
		setMargin(new Insets(1, 1, 1, 1));
		setToolTipText(tip);
		setRequestFocusEnabled(false);
		setSelectedIcon(imgSelected);
		addItemListener(this);
	}

	public float getAlignmentY() {
		return 0.5f;
	}

	public void itemStateChanged(ItemEvent e) {
		setBorder(isSelected() ? m_lowered : m_raised);
	}

	public void testMethod1() {
	}

	public void testMethod2() {
	}
}

// Class ColorMenu unchanged from section 12.5

class ColorMenu extends JMenu {
	protected Border m_unselectedBorder;

	protected Border m_selectedBorder;

	protected Border m_activeBorder;

	protected Hashtable m_panes;

	protected ColorPane m_selected;

	public ColorMenu(String name) {
		super(name);
		m_unselectedBorder = new CompoundBorder(new MatteBorder(1, 1, 1, 1,
				getBackground()), new BevelBorder(BevelBorder.LOWERED,
				Color.white, Color.gray));
		m_selectedBorder = new CompoundBorder(new MatteBorder(2, 2, 2, 2,
				Color.red), new MatteBorder(1, 1, 1, 1, getBackground()));
		m_activeBorder = new CompoundBorder(new MatteBorder(2, 2, 2, 2,
				Color.blue), new MatteBorder(1, 1, 1, 1, getBackground()));

		JPanel p = new JPanel();
		p.setBorder(new EmptyBorder(5, 5, 5, 5));
		p.setLayout(new GridLayout(8, 8));
		m_panes = new Hashtable();

		int[] values = new int[] { 0, 128, 192, 255 };
		for (int r = 0; r < values.length; r++) {
			for (int g = 0; g < values.length; g++) {
				for (int b = 0; b < values.length; b++) {
					Color c = new Color(values[r], values[g], values[b]);
					ColorPane pn = new ColorPane(c);
					p.add(pn);
					m_panes.put(c, pn);
				}
			}
		}
		add(p);
	}

	public void setColor(Color c) {
		Object obj = m_panes.get(c);
		if (obj == null)
			return;
		if (m_selected != null)
			m_selected.setSelected(false);
		m_selected = (ColorPane) obj;
		m_selected.setSelected(true);
	}

	public Color getColor() {
		if (m_selected == null)
			return null;
		return m_selected.getColor();
	}

	public void doSelection() {
		fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
				getActionCommand()));
	}
	
	/* add for testing */
	public void testColorPane(){
		m_selected.mousePressed(null);
		m_selected.mouseReleased(null);
		m_selected.mouseEntered(null);
		m_selected.mouseExited(null);
		m_selected.mouseClicked(null);
		m_selected.getMaximumSize();
		m_selected.m_selected = false;
		m_selected.isSelected();
		m_selected.mouseExited(null);
	}
	
	/* add for testing */
	public void setColorPane_null(){
		m_selected = null;
	}

	class ColorPane extends JPanel implements MouseListener {
		protected Color m_c;

		protected boolean m_selected;

		public ColorPane(Color c) {
			m_c = c;
			setBackground(c);
			setBorder(m_unselectedBorder);
			String msg = "R " + c.getRed() + ", G " + c.getGreen() + ", B "
					+ c.getBlue();
			setToolTipText(msg);
			this.setName(c.getRed()+"_"+c.getGreen()+"_"+c.getBlue());
			addMouseListener(this);
		}

		public Color getColor() {
			return m_c;
		}

		public Dimension getPreferredSize() {
			return new Dimension(15, 15);
		}

		public Dimension getMaximumSize() {
			return getPreferredSize();
		}

		public Dimension getMinimumSize() {
			return getPreferredSize();
		}

		public void setSelected(boolean selected) {
			m_selected = selected;
			if (m_selected)
				setBorder(m_selectedBorder);
			else
				setBorder(m_unselectedBorder);
		}

		public boolean isSelected() {
			return m_selected;
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
			setColor(m_c);
			MenuSelectionManager.defaultManager().clearSelectedPath();
			doSelection();
		}

		public void mouseEntered(MouseEvent e) {
			setBorder(m_activeBorder);
		}

		public void mouseExited(MouseEvent e) {
			setBorder(m_selected ? m_selectedBorder : m_unselectedBorder);
		}

		public void testMethod1() {
		}

		public void testMethod2() {
		}
	}

	public void testMethod1() {
	}

	public void testMethod2() {
	}
}

class FontDialog extends JDialog {
	protected int m_option = JOptionPane.CLOSED_OPTION;

	protected OpenList m_lstFontName;

	protected OpenList m_lstFontSize;

	protected MutableAttributeSet m_attributes;

	protected JCheckBox m_chkBold;

	protected JCheckBox m_chkItalic;

	protected JCheckBox m_chkUnderline;

	protected JCheckBox m_chkStrikethrough;

	protected JCheckBox m_chkSubscript;

	protected JCheckBox m_chkSuperscript;

	protected JComboBox m_cbColor;

	protected JLabel m_preview;
	
	/* add for testing */
	public void getMaxSize(){
		m_lstFontSize.getMaximumSize();
	}

	public FontDialog(JFrame parent, String[] names, String[] sizes) {
		super(parent, "Font", true);
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		JPanel p = new JPanel(new GridLayout(1, 2, 10, 2));
		p.setBorder(new TitledBorder(new EtchedBorder(), "Font"));
		m_lstFontName = new OpenList(names, "Name");
		p.add(m_lstFontName);

		m_lstFontSize = new OpenList(sizes, "Size");
		p.add(m_lstFontSize);
		getContentPane().add(p);

		p = new JPanel(new GridLayout(2, 3, 10, 5));
		p.setBorder(new TitledBorder(new EtchedBorder(), "Effects"));
		m_chkBold = new JCheckBox("Bold");
		p.add(m_chkBold);
		m_chkItalic = new JCheckBox("Italic");
		p.add(m_chkItalic);
		m_chkUnderline = new JCheckBox("Underline");
		p.add(m_chkUnderline);
		m_chkStrikethrough = new JCheckBox("Strikeout");
		p.add(m_chkStrikethrough);
		m_chkSubscript = new JCheckBox("Subscript");
		p.add(m_chkSubscript);
		m_chkSuperscript = new JCheckBox("Superscript");
		p.add(m_chkSuperscript);
		getContentPane().add(p);

		getContentPane().add(Box.createVerticalStrut(5));
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(Box.createHorizontalStrut(10));
		p.add(new JLabel("Color:"));
		p.add(Box.createHorizontalStrut(20));
		m_cbColor = new JComboBox();

		int[] values = new int[] { 0, 128, 192, 255 };
		for (int r = 0; r < values.length; r++) {
			for (int g = 0; g < values.length; g++) {
				for (int b = 0; b < values.length; b++) {
					Color c = new Color(values[r], values[g], values[b]);
					m_cbColor.addItem(c);
				}
			}
		}

		m_cbColor.setRenderer(new ColorComboRenderer());
		p.add(m_cbColor);
		p.add(Box.createHorizontalStrut(10));
		getContentPane().add(p);

		p = new JPanel(new BorderLayout());
		p.setBorder(new TitledBorder(new EtchedBorder(), "Preview"));
		m_preview = new JLabel("Preview Font", JLabel.CENTER);
		m_preview.setBackground(Color.white);
		m_preview.setForeground(Color.black);
		m_preview.setOpaque(true);
		m_preview.setBorder(new LineBorder(Color.black));
		m_preview.setPreferredSize(new Dimension(120, 40));
		p.add(m_preview, BorderLayout.CENTER);
		getContentPane().add(p);

		p = new JPanel(new FlowLayout());
		JPanel p1 = new JPanel(new GridLayout(1, 2, 10, 2));
		JButton btOK = new JButton("OK");
		btOK.setName("OK");
		ActionListener lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_option = JOptionPane.OK_OPTION;
				setVisible(false);
			}
		};
		btOK.addActionListener(lst);
		p1.add(btOK);

		JButton btCancel = new JButton("Cancel");
		btCancel.setName("Cancel");
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_option = JOptionPane.CANCEL_OPTION;
				setVisible(false);
			}
		};
		btCancel.addActionListener(lst);
		p1.add(btCancel);
		p.add(p1);
		getContentPane().add(p);

		setAllComponentName();

		pack();
		setResizable(false);

		ListSelectionListener lsel = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updatePreview();
			}
		};
		m_lstFontName.addListSelectionListener(lsel);
		m_lstFontSize.addListSelectionListener(lsel);

		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updatePreview();
			}
		};
		m_chkBold.addActionListener(lst);
		m_chkItalic.addActionListener(lst);
		m_cbColor.addActionListener(lst);
	}

	private void setAllComponentName() { // XXX font dialog Name
		m_chkBold.setName("Bold");
		m_chkItalic.setName("Italic");
		m_chkUnderline.setName("Underline");
		m_chkStrikethrough.setName("Strikeout");
		m_chkSubscript.setName("Subscript");
		m_chkSuperscript.setName("Superscript");
		m_cbColor.setName("Color");
		m_preview.setName("Preview");
	}

	public void setAttributes(AttributeSet a) {
		m_attributes = new SimpleAttributeSet(a);
		String name = StyleConstants.getFontFamily(a);
		m_lstFontName.setSelected(name);
		int size = StyleConstants.getFontSize(a);
		m_lstFontSize.setSelectedInt(size);
		m_chkBold.setSelected(StyleConstants.isBold(a));
		m_chkItalic.setSelected(StyleConstants.isItalic(a));
		m_chkUnderline.setSelected(StyleConstants.isUnderline(a));
		m_chkStrikethrough.setSelected(StyleConstants.isStrikeThrough(a));
		m_chkSubscript.setSelected(StyleConstants.isSubscript(a));
		m_chkSuperscript.setSelected(StyleConstants.isSuperscript(a));
		m_cbColor.setSelectedItem(StyleConstants.getForeground(a));
		updatePreview();
	}

	public AttributeSet getAttributes() {
		if (m_attributes == null)
			return null;
		StyleConstants.setFontFamily(m_attributes, m_lstFontName.getSelected());
		StyleConstants
				.setFontSize(m_attributes, m_lstFontSize.getSelectedInt());
		StyleConstants.setBold(m_attributes, m_chkBold.isSelected());
		StyleConstants.setItalic(m_attributes, m_chkItalic.isSelected());
		StyleConstants.setUnderline(m_attributes, m_chkUnderline.isSelected());
		StyleConstants.setStrikeThrough(m_attributes, m_chkStrikethrough
				.isSelected());
		StyleConstants.setSubscript(m_attributes, m_chkSubscript.isSelected());
		StyleConstants.setSuperscript(m_attributes, m_chkSuperscript
				.isSelected());
		StyleConstants.setForeground(m_attributes, (Color) m_cbColor
				.getSelectedItem());
		return m_attributes;
	}

	public int getOption() {
		return m_option;
	}

	protected void updatePreview() {
		String name = m_lstFontName.getSelected();
		int size = m_lstFontSize.getSelectedInt();
		if (size <= 0)
			return;
		int style = Font.PLAIN;
		if (m_chkBold.isSelected())
			style |= Font.BOLD;
		if (m_chkItalic.isSelected())
			style |= Font.ITALIC;

		// Bug Alert! This doesn't work if only style is changed.
		Font fn = new Font(name, style, size);
		m_preview.setFont(fn);

		Color c = (Color) m_cbColor.getSelectedItem();
		m_preview.setForeground(c);
		m_preview.repaint();
	}

	public void testMethod1() {
	}

	public void testMethod2() {
	}
}

class OpenList extends JPanel implements ListSelectionListener, ActionListener {
	protected JLabel m_title;

	protected JTextField m_text;

	protected JList m_list;

	protected JScrollPane m_scroll;

	public OpenList(String[] data, String title) {
		setLayout(null);
		m_title = new JLabel(title + ":", JLabel.LEFT);
		add(m_title);
		m_text = new JTextField();
		m_text.addActionListener(this);
		add(m_text);
		m_list = new JList(data);
		m_list.setVisibleRowCount(4);
		m_list.addListSelectionListener(this);
		m_scroll = new JScrollPane(m_list);
		setComponentName(title);
		add(m_scroll);
	}

	private void setComponentName(String title) { // XXX OpenList Name
		m_text.setName(title + "Text");
		m_list.setName(title + "List");
		m_scroll.setName(title + "Scroll");
	}

	public void setSelected(String sel) {
		m_list.setSelectedValue(sel, true);
		m_text.setText(sel);
	}

	public String getSelected() {
		return m_text.getText();
	}

	public void setSelectedInt(int value) {
		setSelected(Integer.toString(value));
	}

	public int getSelectedInt() {
		try {
			return Integer.parseInt(getSelected());
		} catch (NumberFormatException ex) {
			return -1;
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		Object obj = m_list.getSelectedValue();
		if (obj != null)
			m_text.setText(obj.toString());
	}

	public void actionPerformed(ActionEvent e) {
		ListModel model = m_list.getModel();
		String key = m_text.getText().toLowerCase();
		for (int k = 0; k < model.getSize(); k++) {
			String data = (String) model.getElementAt(k);
			if (data.toLowerCase().startsWith(key)) {
				m_list.setSelectedValue(data, true);
				break;
			}
		}
	}

	public void addListSelectionListener(ListSelectionListener lst) {
		m_list.addListSelectionListener(lst);
	}

	public Dimension getPreferredSize() {
		Insets ins = getInsets();
		Dimension d1 = m_title.getPreferredSize();
		Dimension d2 = m_text.getPreferredSize();
		Dimension d3 = m_scroll.getPreferredSize();
		int w = Math.max(Math.max(d1.width, d2.width), d3.width);
		int h = d1.height + d2.height + d3.height;
		return new Dimension(w + ins.left + ins.right, h + ins.top + ins.bottom);
	}

	public Dimension getMaximumSize() {
		Insets ins = getInsets();
		Dimension d1 = m_title.getMaximumSize();
		Dimension d2 = m_text.getMaximumSize();
		Dimension d3 = m_scroll.getMaximumSize();
		int w = Math.max(Math.max(d1.width, d2.width), d3.width);
		int h = d1.height + d2.height + d3.height;
		return new Dimension(w + ins.left + ins.right, h + ins.top + ins.bottom);
	}

	public Dimension getMinimumSize() {
		Insets ins = getInsets();
		Dimension d1 = m_title.getMinimumSize();
		Dimension d2 = m_text.getMinimumSize();
		Dimension d3 = m_scroll.getMinimumSize();
		int w = Math.max(Math.max(d1.width, d2.width), d3.width);
		int h = d1.height + d2.height + d3.height;
		return new Dimension(w + ins.left + ins.right, h + ins.top + ins.bottom);
	}

	public void doLayout() {
		Insets ins = getInsets();
		Dimension d = getSize();
		int x = ins.left;
		int y = ins.top;
		int w = d.width - ins.left - ins.right;
		int h = d.height - ins.top - ins.bottom;

		Dimension d1 = m_title.getPreferredSize();
		m_title.setBounds(x, y, w, d1.height);
		y += d1.height;
		Dimension d2 = m_text.getPreferredSize();
		m_text.setBounds(x, y, w, d2.height);
		y += d2.height;
		m_scroll.setBounds(x, y, w, h - y);
	}

	public void testMethod1() {
	}

	public void testMethod2() {
	}
}

class ColorComboRenderer extends JPanel implements ListCellRenderer {
	protected Color m_color = Color.black;

	protected Color m_focusColor = (Color) UIManager
			.get("List.selectionBackground");

	protected Color m_nonFocusColor = Color.white;

	public Component getListCellRendererComponent(JList list, Object obj,
			int row, boolean sel, boolean hasFocus) {
		if (hasFocus || sel)
			setBorder(new CompoundBorder(new MatteBorder(2, 10, 2, 10,
					m_focusColor), new LineBorder(Color.black)));
		else
			setBorder(new CompoundBorder(new MatteBorder(2, 10, 2, 10,
					m_nonFocusColor), new LineBorder(Color.black)));

		if (obj instanceof Color)
			m_color = (Color) obj;
		return this;
	}

	public void paintComponent(Graphics g) {
		setBackground(m_color);
		super.paintComponent(g);
	}

	public void testMethod1() {
	}

	public void testMethod2() {
	}
}
