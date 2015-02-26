/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.testscript.AbstractNode;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.OracleNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.scriptEditor.def.TestScriptTage;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class AddOracleNodeToScriptDialog extends TitleAreaDialog {
	
	private Combo m_com_target;
	
	private Combo m_com_strategy;
	
	private Shell m_shell;
	
	private Tree m_tree;
	
	private BaseNode m_node;
	
	private FolderNode m_folder;
	
	private Button m_radio_add;
	
	private Button m_radio_del;
	
	public AddOracleNodeToScriptDialog(Shell parentShell,FolderNode folder) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		m_shell = parentShell;
		m_folder = folder;
	}
	
	public AddOracleNodeToScriptDialog(Shell parentShell,BaseNode node) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		m_shell = parentShell;
		m_node = node;
	}
	
	protected Control createDialogArea(Composite parent) {
		/* setup layout */
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;
		
		/* Radio Select */
		m_radio_add = new Button(area,SWT.RADIO);
		m_radio_add.setText("Add Test Oracle Node");
		m_radio_add.setSelection(true);
		
		m_radio_del = new Button(area,SWT.RADIO);
		m_radio_del.setText("Clear Test Oracle Node");

		/* target */
		final Label m_lbl_target = new Label(area, SWT.NULL);
		m_lbl_target.setText("Target:");
		m_com_target = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_com_target.setLayoutData(data);
		m_com_target.setVisibleItemCount(10);
		m_com_target.add("Event Node");
		m_com_target.add("Folder Node");
		m_com_target.add("Interation Sequence");
		m_com_target.add("Macro Node");
		m_com_target.setText("Event Node");
		
		/* Strategy */
		final Label m_lbl_strategy = new Label(area, SWT.NULL);
		m_lbl_strategy.setText("Strategy:");
		m_com_strategy = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_com_strategy.setLayoutData(data);
		m_com_strategy.setVisibleItemCount(10);
		m_com_strategy.add("After");
		m_com_strategy.add("before");
		m_com_strategy.setText("After");
		
		/* AssertNode Tree */
		final Composite area2 = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout2 = new GridLayout();
		gridlayout2.numColumns = 1;
		area2.setLayout(gridlayout2);
		m_tree = new Tree(area2,SWT.NULL);
		GridData data2 = new GridData();
		data2.widthHint = 400;
		m_tree.setLayoutData(data2);
		m_tree.addMouseListener(new MouseListener(){

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				TreeItem [] selitems = m_tree.getSelection();
				if(selitems == null)return;
				TreeItem item = selitems[0];
				OracleNode oraclenode = (OracleNode)item.getData();
				EditOracleNodeDialog editdialog = new EditOracleNodeDialog(m_shell, oraclenode);
				editdialog.open();
				item.setText(oraclenode.toSimpleString());
			}

			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
//		 add button/ del button
		final Composite area3 = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout3 = new GridLayout();
		gridlayout3.numColumns = 2;
		area3.setLayout(gridlayout3);
		final Button btn_add = new Button(area3, SWT.Modify);
		btn_add.setText("Add");
		btn_add.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				NodeFactory factory = new NodeFactory();
				OracleNode node=factory.createOracleNode();
				
				final TreeItem treeitem = new TreeItem(m_tree,SWT.NULL);
				treeitem.setText(node.toSimpleString());
				treeitem.setData(node);
				ImageRegistry image_registry = GTTlipse.getDefault().getImageRegistry();
				treeitem.setImage(image_registry.get(TestScriptTage.ORACLE_NODE));
			}
			
		});
		
		final Button btn_del = new Button(area3, SWT.Modify);
		btn_del.setText("Del");
		btn_del.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				TreeItem [] selitems = m_tree.getSelection();
				if(selitems == null)return;
				TreeItem item = selitems[0];
				item.dispose();
			}
			
		});
		
		return parent;
	}
		
	protected void createButtonsForButtonBar(Composite parent) {
		Button btn_modify = createButton(parent, SWT.Modify, "Modify", true);
		btn_modify.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				if(m_radio_add.getSelection()){
					if(m_node != null)
						AddOracleToAllScript(m_node);
					else if(m_folder != null)
						addOracleNode(m_folder);
				} else if(m_radio_del.getSelection()){
					if(m_node != null)
						ClearOracleToAllScript(m_node);
					else if(m_folder != null)
						ClearOracleToAllScript(m_folder);
				}
				setReturnCode(SWT.Modify);
				close();
			}
		});

		Button btn_cancel = createButton(parent, CANCEL, "Cancel", true);
		btn_cancel.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				setReturnCode(CANCEL);
				close();
			}
		});
	}
	
	private void ClearOracleToAllScript(Object node){
		if(node instanceof ProjectNode||
		   node instanceof SourceFolderNode||
		   node instanceof PackageNode||
		   node instanceof TestCaseNode){
			CompositeNode cmpnode = (CompositeNode)node;
			BaseNode [] childs = cmpnode.getChildren();
			if(childs == null) return;
			for(BaseNode child:childs){
				ClearOracleToAllScript(child);
			}
		} else if(node instanceof TestMethodNode){
			TestMethodNode method = (TestMethodNode)node;
			TestScriptDocument [] docs = method.getDocuments();
			if(docs == null) return;
			for(TestScriptDocument doc:docs){
				ClearOracleToAllScript((FolderNode)doc.getScript());
			}
		} else if(node instanceof FolderNode){
			FolderNode folder = (FolderNode)node;
			for(int i=0;i<folder.getChildren().length;i++){
				AbstractNode anode = folder.get(i);
				if(anode instanceof FolderNode)
					ClearOracleToAllScript(anode);
				else if(anode instanceof OracleNode){
					folder.remove(i);
					i--;
				}
			}
		}
			
	}
	
	private void AddOracleToAllScript(BaseNode node){
		if(node instanceof ProjectNode||
		   node instanceof SourceFolderNode||
		   node instanceof PackageNode||
		   node instanceof TestCaseNode){
			CompositeNode cmpnode = (CompositeNode)node;
			BaseNode [] childs = cmpnode.getChildren();
			if(childs == null) return;
			for(BaseNode child:childs){
				AddOracleToAllScript(child);
			}
		} else if(node instanceof TestMethodNode){
			TestMethodNode method = (TestMethodNode)node;
			TestScriptDocument [] docs = method.getDocuments();
			if(docs == null) return;
			for(TestScriptDocument doc:docs){
				addOracleNode((FolderNode)doc.getScript());
			}
		}
	}
	
	public void addOracleNode(FolderNode folder){
		if(folder == null) return;
		if(m_com_target.getSelectionIndex() == 0){
			/* event */
			folder.accept(new AddOracleNodeVisitor_Event(m_com_strategy, m_tree));
		}else if(m_com_target.getSelectionIndex() == 1) {
			/* folder */
			folder.accept(new AddOracleNodeVisitor_Folder(m_com_strategy, m_tree));
		}else if(m_com_target.getSelectionIndex() == 2) {
			/* Interation Sequence */
			if(folder.getParent() != null)return;
			if( m_com_strategy.getSelectionIndex() == 0){
				/* aftare */
				for(int i = 0 ; i< m_tree.getItemCount() ; i++){
					TreeItem treeitem = m_tree.getItem(i);
					OracleNode oraclenode = (OracleNode)treeitem.getData();
					folder.add(oraclenode.clone() , folder.size());
				}
			} else {
				/* before */
				for(int i = 0 ; i< m_tree.getItemCount() ; i++){
					/* fix for autinfonode */
					int prefix = 0;
					while(folder.get(prefix) instanceof LaunchNode)
						prefix++;
					
					TreeItem treeitem = m_tree.getItem(i);
					OracleNode oraclenode = (OracleNode)treeitem.getData();
					folder.add(oraclenode.clone() , prefix);
				}
			}
		}else if(m_com_target.getSelectionIndex() == 3) {
			/* folder */
			folder.accept(new AddOracleNodeVisitor_Macro(m_com_strategy, m_tree));
		}
	}
}
