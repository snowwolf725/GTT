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

public class WordProcessorLayoutChange extends JFrame 
{
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
  
  /* add for testing */
  public void testundo(){
 	m_undoAction.actionPerformed(null);
  }
	
  /* add for testing */
  public void testredo(){
	m_redoAction.actionPerformed(null);
  }
	
  /* add for testing */
  public void testColorMenuEvent(){
	  System.out.println("==================");
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

  public void exitFrame(){
		this.dispose();
	}
  
  public WordProcessorLayoutChange() {
    super("RTF Word Processor");
    setSize(600, 400);

    m_monitor = new JTextPane();
    m_kit = new RTFEditorKit();
    m_monitor.setEditorKit(m_kit);
    m_context = new StyleContext(); //本文的實體
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
        if (m_xStart>=0 && m_xFinish>=0)
          /*if (m_monitor.getCaretPosition()==m_xStart) {
            m_monitor.setCaretPosition(m_xFinish);
            m_monitor.moveCaretPosition(m_xStart);
          }
          else*/
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
  private void setAllComponentName(){	//XXX Word processor Name
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
    JMenuItem item =  mFile.add(actionNew);
    item.setName("menuNew");
    item.setMnemonic('n');

    ImageIcon iconOpen = new ImageIcon("images/file_open.gif");
    Action actionOpen = new AbstractAction("Open...", iconOpen) { 
      public void actionPerformed(ActionEvent e) {
        WordProcessorLayoutChange.this.setCursor(
          Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Thread runner = new Thread() {
          public void run() {
            if (m_chooser.showOpenDialog(WordProcessorLayoutChange.this) != 
             JFileChooser.APPROVE_OPTION)
              return;
            WordProcessorLayoutChange.this.repaint();
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
            } 
            catch (Exception ex) {
              //ex.printStackTrace();
              System.out.println("Problems encountered: Note that RTF"
                + " support is still under development."); 
            }
            WordProcessorLayoutChange.this.setCursor(Cursor.getPredefinedCursor(
              Cursor.DEFAULT_CURSOR));
          }
        };
        runner.start();
      }
    };
    item =  mFile.add(actionOpen);  
    item.setName("menuOpen");
    item.setMnemonic('o');

    ImageIcon iconSave = new ImageIcon("images/file_save.gif");
    Action actionSave = new AbstractAction("Save...", iconSave) {
      public void actionPerformed(ActionEvent e) {
        WordProcessorLayoutChange.this.setCursor(
          Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Thread runner = new Thread() {
          public void run() {
            if (m_chooser.showSaveDialog(WordProcessorLayoutChange.this) !=
             JFileChooser.APPROVE_OPTION)
              return;
            WordProcessorLayoutChange.this.repaint();
            File fChoosen = m_chooser.getSelectedFile();

            // Recall that text component read/write operations are
            // thread safe. Its ok to do this in a separate thread.
            try {
              OutputStream out = new FileOutputStream(fChoosen);
              m_kit.write(out, m_doc, 0, m_doc.getLength());
              out.close();
            } 
            catch (Exception ex) {
              ex.printStackTrace();
            }
           
            // Make sure chooser is updated to reflect new file
            m_chooser.rescanCurrentDirectory();
            WordProcessorLayoutChange.this.setCursor(Cursor.getPredefinedCursor(
              Cursor.DEFAULT_CURSOR));
          }
        };
        runner.start();
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

    item =  mFile.add(actionExit);  
    item.setName("menuExit");
    item.setMnemonic('x');
    menuBar.add(mFile);

    m_toolBar = new JToolBar();
    JButton bNew = new SmallButton(actionNew, "New document");
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

    item = new JMenuItem("Font...");
    item.setName("menuFont");
    item.setMnemonic('o');
    ActionListener lst = new ActionListener() { 
      public void actionPerformed(ActionEvent e) {
        WordProcessorLayoutChange.this.repaint();
        AttributeSet a = m_doc.getCharacterElement(
          m_monitor.getCaretPosition()).getAttributes();
        m_fontDialog.setAttributes(a);

        Dimension d1 = m_fontDialog.getSize();
        Dimension d2 = WordProcessorLayoutChange.this.getSize();
        int x = Math.max((d2.width-d1.width)/2, 0);
        int y = Math.max((d2.height-d1.height)/2, 0);
        m_fontDialog.setBounds(x + WordProcessorLayoutChange.this.getX(),
          y + WordProcessorLayoutChange.this.getY(), d1.width, d1.height);

        m_fontDialog.setVisible(true);
        if (m_fontDialog.getOption()==JOptionPane.OK_OPTION) {
          setAttributeSet(m_fontDialog.getAttributes());
          showAttributes(m_monitor.getCaretPosition());
        }
      }
    };
    item.addActionListener(lst);
    mEdit.add(item);

    mEdit.addSeparator();
    
    Action action = new AbstractAction("Copy", 
     new ImageIcon("images/edit_copy.gif")) 
    { 
      public void actionPerformed(ActionEvent e) {
        m_monitor.copy();
      }
    };
    item = mEdit.add(action);  
    item.setName("menuCopy");
    item.setMnemonic('c');
    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, 
      KeyEvent.CTRL_MASK));

    action = new AbstractAction("Cut", 
     new ImageIcon("images/edit_cut.gif")) 
    { 
      public void actionPerformed(ActionEvent e) {
        m_monitor.cut();
      }
    };
    item = mEdit.add(action);
    item.setName("menuCut");
    item.setMnemonic('t');
    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 
      KeyEvent.CTRL_MASK));

    action = new AbstractAction("Paste", 
     new ImageIcon("images/edit_paste.gif")) 
    { 
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

    m_undoAction = new AbstractAction("Undo", 
     new ImageIcon("images/edit_undo.gif")) 
    { 
      public void actionPerformed(ActionEvent e) {
        try {
          m_undo.undo();
        } 
        catch (CannotUndoException ex) {
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

    m_redoAction = new AbstractAction("Redo", 
     new ImageIcon("images/edit_redo.gif")) 
    { 
      public void actionPerformed(ActionEvent e) {
        try {
          m_undo.redo();
        } 
        catch (CannotRedoException ex) {
          System.err.println("Unable to redo: " + ex);
        }
        updateUndo();
      }
    };
    item =  mEdit.add(m_redoAction);  
    item.setName("menuRedo");
    item.setMnemonic('r');
    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 
      KeyEvent.CTRL_MASK));

    menuBar.add(mEdit);
    
    GraphicsEnvironment ge = GraphicsEnvironment.
      getLocalGraphicsEnvironment();
    m_fontNames = ge.getAvailableFontFamilyNames();

    m_toolBar.addSeparator();
    m_cbFonts = new JComboBox(m_fontNames);
    m_cbFonts.setMaximumSize(m_cbFonts.getPreferredSize());
    m_cbFonts.setEditable(true);

    lst = new ActionListener() { 
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
    m_fontSizes = new String[] {"8", "9", "10", "11", "12", "14",
      "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"};
    m_cbSizes = new JComboBox(m_fontSizes);
    m_cbSizes.setMaximumSize(m_cbSizes.getPreferredSize());
    m_cbSizes.setEditable(true);

    m_fontDialog = new FontDialog(this, m_fontNames, m_fontSizes);
    lst = new ActionListener() { 
      public void actionPerformed(ActionEvent e) {
        int fontSize = 0;
        try {
          fontSize = Integer.parseInt(m_cbSizes.
            getSelectedItem().toString());
        }
        catch (NumberFormatException ex) { return; }

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
    m_bBold = new SmallToggleButton(false, img1, img2, 
      "Bold font");
    m_bBold.setPreferredSize(new Dimension(20,20));
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
    m_bItalic = new SmallToggleButton(false, img1, img2, 
      "Italic font");
    m_bItalic.setPreferredSize(new Dimension(20,20));
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



    JMenu mStyle = new JMenu("Style");
    mStyle.setName("menuStyle");
    mStyle.setMnemonic('s');
    mFormat.add(mStyle);

    item = new JMenuItem("Update");
    item.setName("menuUpdate");
    item.setMnemonic('u');
    lst = new ActionListener() { 
      public void actionPerformed(ActionEvent e) {
        String name = (String)m_cbStyles.getSelectedItem();
        Style style = m_doc.getStyle(name);
        int p = m_monitor.getCaretPosition();
        AttributeSet a = m_doc.getCharacterElement(p).
          getAttributes();
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
        String name = (String)m_cbStyles.getSelectedItem();
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
        AttributeSet a = m_doc.getCharacterElement(p).
          getAttributes();
        Color c = StyleConstants.getForeground(a);
        m_foreground.setColor(c);
      }

      public void menuDeselected(MenuEvent e) {}

      public void menuCanceled(MenuEvent e) {}
    };
    m_foreground.addMenuListener(ml);

    // Bug Alert! JEditorPane background color 
    // doesn't work as of Java 2 FCS.
    m_background = new ColorMenu("Selection Background");
    m_background.setColor(m_monitor.getBackground());
    m_background.setMnemonic('b');
    lst = new ActionListener()  { 
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
        AttributeSet a = m_doc.getCharacterElement(p).
          getAttributes();
        Color c = StyleConstants.getBackground(a);
        m_background.setColor(c);
      }

      public void menuDeselected(MenuEvent e) {}

      public void menuCanceled(MenuEvent e) {}
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
            if (m_chooser.showOpenDialog(WordProcessorLayoutChange.this) != 
             JFileChooser.APPROVE_OPTION)
              return;
            WordProcessorLayoutChange.this.repaint();
            File fChoosen = m_chooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(fChoosen.getPath());
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            if (w<=0 || h<=0) {
              JOptionPane.showMessageDialog(WordProcessorLayoutChange.this, 
                "Error reading image file\n"+
                fChoosen.getPath(), "Warning", 
                JOptionPane.WARNING_MESSAGE);
                return;
            }
            MutableAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setIcon(attr, icon);
            int p = m_monitor.getCaretPosition();
            try {
              m_doc.insertString(p, " ", attr);
            }
            catch (BadLocationException ex) {}

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
    m_cbStyles.setMaximumSize(m_cbStyles.getPreferredSize());
    m_cbStyles.setEditable(true);
    m_toolBar.add(m_cbStyles);
        
    lst = new ActionListener() { 
      public void actionPerformed(ActionEvent e) {
        if (m_skipUpdate || m_cbStyles.getItemCount()==0)
          return;
        String name = (String)m_cbStyles.getSelectedItem();
        int index = m_cbStyles.getSelectedIndex();
        int p = m_monitor.getCaretPosition();
                
        // New name entered
        if (index == -1) {
          m_cbStyles.addItem(name);
          Style style = m_doc.addStyle(name, null);
          AttributeSet a = m_doc.getCharacterElement(p).
            getAttributes();
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
    AttributeSet a = m_doc.getCharacterElement(p).
      getAttributes();
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
    if (m_styles!=null && m_styles.get(name)==null) {
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
      m_doc.setCharacterAttributes(xStart, xFinish - xStart, 
        attr, false);
    } 
    else {
      MutableAttributeSet inputAttributes = 
        m_kit.getInputAttributes();
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
    if(m_undo.canUndo()) {
      m_undoAction.setEnabled(true);
      m_undoAction.putValue(Action.NAME, 
      m_undo.getUndoPresentationName());
    }
    else {
      m_undoAction.setEnabled(false);
      m_undoAction.putValue(Action.NAME, "Undo");
    }
    if(m_undo.canRedo()) {
      m_redoAction.setEnabled(true);
      m_redoAction.putValue(Action.NAME, 
      m_undo.getRedoPresentationName());
    }
    else {
      m_redoAction.setEnabled(false);
      m_redoAction.putValue(Action.NAME, "Redo");
    }
  }

  public static void main(String argv[]) {
	if (argv.length == 0)
		new WordProcessorLayoutChange();
	else if( argv[0].indexOf("LayoutChange") != -1)
		new WordProcessorLayoutChange();
  }

  class Undoer implements UndoableEditListener
  {
    public Undoer() {
      m_undo.die();
      updateUndo();
    }

    public void undoableEditHappened(UndoableEditEvent e) {
      UndoableEdit edit = e.getEdit();
      m_undo.addEdit(e.getEdit());
      updateUndo();
    }
  }
}

// Class SmallButton unchanged from section 4.8


