package gttlipse.editors;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class GttFileEditor extends MultiPageEditorPart implements
		IResourceChangeListener {

	private TextEditor m_editor;
	private Composite  m_drawPanel;
	private StyledText m_text;

	/**
	 * Creates a multi-page editor example.
	 */
	public GttFileEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	void createPage0() {
		try {
			m_editor = new TextEditor();
			int index = addPage( m_editor, getEditorInput() );
			setPageText( index, m_editor.getTitle() );
		} catch ( PartInitException e ) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating nested text editor", null, e.getStatus());
		}
	}

	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the
	 * font used in page 2.
	 */
	void createPage1() 
	{
		Composite composite = new Composite( getContainer(), SWT.NONE );
		GridLayout areaLayout = new GridLayout();
		areaLayout.numColumns = 2;
		composite.setLayout( areaLayout );
		
		Composite toolPanel = new Composite( composite, SWT.NONE );
		toolPanel.setLayout( new GridLayout() );
		toolPanel.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		Composite drawPanel = new Composite( composite, SWT.EMBEDDED );
		drawPanel.setLayout( new GridLayout() );
		drawPanel.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		m_drawPanel = drawPanel;
		
		createToolBar( toolPanel );
		createPane( drawPanel );
		
		int index = addPage( composite );
		setPageText( index, "FSM Diagram" );
	}
	
	private void createPane( Composite composite )
	{		
//		VisualFSMGeneration visualPerform = new VisualFSMGeneration();
//		visualPerform.visualize();	
//		JComponent swingDrawPane = createSwingDrawPanel( visualPerform );		
//		java.awt.Frame awtFrame = SWT_AWT.new_Frame( composite );
//		awtFrame.add( swingDrawPane );
	}
	
	private void refreshPane( Composite composite )
	{		
//		VisualFSMGeneration visualPerform = new VisualFSMGeneration();
//		visualPerform.visualize();	
//		JComponent swingDrawPane = createSwingDrawPanel( visualPerform );		
//		java.awt.Frame awtFrame = SWT_AWT.getFrame( composite );
//		awtFrame.removeAll();
//		awtFrame.dispose();
//		awtFrame = SWT_AWT.new_Frame( composite );
//		awtFrame.add( swingDrawPane );
	}
	
//	private JComponent createSwingDrawPanel( VisualFSMGeneration visualPerform )
//	{
//		JScrollPane jsp = new JScrollPane();
//		
//		try 
//		{
//			InputStream input = new FileInputStream( visualPerform.getResultPath() );
//			Parser program = new Parser( input,System.err );
//			program.parse();
//			Graph graph = program.getGraph();
//			System.err.println( "The graph contains " + graph.countOfElements( Grappa.NODE | Grappa.EDGE | Grappa.SUBGRAPH ) + " elements." );
//			graph.setEditable( true );
//			graph.setErrorWriter( new PrintWriter( System.err,true ) );		
//			GrappaPanel gp = new GrappaPanel( graph );
//			gp.addGrappaListener( new GrappaAdapter() );
//			gp.setScaleToFit( false );	
//			jsp.setViewportView( gp );
//			jsp.setDoubleBuffered( true );
//		} 
//		catch( FileNotFoundException e ) 
//		{
//			e.printStackTrace();
//		}
//		catch( Exception ex ) 
//		{
//		    System.err.println( "Exception: " + ex.getMessage() );
//		    ex.printStackTrace( System.err );
//		}
//		return jsp;
//	}
	
	private void createToolBar( Composite composite )
	{
		final ToolBar   toolBar   = new ToolBar( composite, SWT.VERTICAL );
		URL             path      = null;
		ImageDescriptor imagedesc = null;
		Image           image     = null;
		
		// fresh action
        ToolItem freshBtn = new ToolItem( toolBar,SWT.V_SCROLL );		
		path = gttlipse.GTTlipse.getDefault().getBundle().getEntry( "./images/Mode.gif" );
		imagedesc = ImageDescriptor.createFromURL( path );
		image = imagedesc.createImage();
		freshBtn.setText( "Fresh" );
		freshBtn.setImage( image );
		freshBtn.addSelectionListener( new SelectionAdapter() 
	    {
		    public void widgetSelected( SelectionEvent e ) 
		    {     
		    	refreshPane( m_drawPanel );
		    }
	    });
		
//		// Jpeg action
//		ToolItem jpgBtn = new ToolItem( toolBar,SWT.V_SCROLL );
//		path = GTTlipse.GTTlipse.getDefault().getBundle().getEntry( "./images/Instance.gif" );
//		imagedesc = ImageDescriptor.createFromURL( path );
//		image = imagedesc.createImage();
//		jpgBtn.setText( "Jpeg" );
//		jpgBtn.setImage( image );
//		jpgBtn.addSelectionListener( new SelectionAdapter() 
//	    {
//		    public void widgetSelected( SelectionEvent e ) 
//		    {     	
//		    	Shell shell     = toolBar.getShell();
//				String filter[] = { "*.jpg" };	
//				FileDialog fileDialog = new FileDialog( shell, SWT.SAVE );
//				fileDialog.setText( "Save Jpeg" );
//				fileDialog.setFilterExtensions( filter );	
//				
//				if( fileDialog.open().matches( "" ) == false )
//				{	
//					String filePath = fileDialog.getFilterPath() + "\\";
//				    filePath += fileDialog.getFileName();
//				    VisualFSMGeneration visualPerform = new VisualFSMGeneration();
//				    visualPerform.visualizeJpg( filePath );
//				}	
//		    }
//	    });				
	}
	
	/**
	 * Creates page 2 of the multi-page editor, which shows the sorted text.
	 */
	void createPage2() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);

		m_text = new StyledText(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		m_text.setEditable(false);

		int index = addPage(composite);
		setPageText(index, "Preview");
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPage0();
		createPage1();
		createPage2();
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the
	 * text for page 0's tab, and updates this multi-page editor's input to
	 * correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException(
					"Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 2) {
			sortWords();
		}
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow()
							.getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) m_editor.getEditorInput())
								.getFile().getProject().equals(
										event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(m_editor
									.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

	/**
	 * Sets the font related data to be applied to the text in page 2.
	 */
//	void setFont() {
//		FontDialog fontDialog = new FontDialog(getSite().getShell());
//		fontDialog.setFontList(text.getFont().getFontData());
//		FontData fontData = fontDialog.open();
//		if (fontData != null) {
//			if (font != null)
//				font.dispose();
//			font = new Font(text.getDisplay(), fontData);
//			text.setFont(font);
//		}
//	}

	/**
	 * Sorts the words in page 0, and shows them in page 2.
	 */
	void sortWords() {

//		String editorText = editor.getDocumentProvider().getDocument(
//				editor.getEditorInput()).get();

//		StringTokenizer tokenizer = new StringTokenizer(editorText,
//				" \t\n\r\f!@#\u0024%^&*()-_=+`~[]{};:'\",.<>/?|\\");
		List<?> editorWords = new ArrayList<Object>();
//		while (tokenizer.hasMoreTokens()) {
//			editorWords.add(tokenizer.nextToken());
//		}

//		Collections.sort(editorWords, Collator.getInstance());
		StringWriter displayText = new StringWriter();
		for (int i = 0; i < editorWords.size(); i++) {
			displayText.write(((String) editorWords.get(i)));
			displayText.write(System.getProperty("line.separator"));
		}
		m_text.setText(displayText.toString());
	}
}
