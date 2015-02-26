package gttlipse.scriptEditor.dialog;

import gtt.oracle.OracleInfoReader;
import gtt.testscript.AbstractNode;
import gtt.testscript.OracleNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.ViewAssertNode;
import gttlipse.EclipseProject;
import gttlipse.GTTlipse;
import gttlipse.scriptEditor.def.TestScriptTage;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.util.TestScriptNodeFinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


public class OracleReportPanel {

	private Tree m_tree;

	private OracleNode m_node;

	private ImageRegistry image_registry;

	public OracleReportPanel(Composite parent, OracleNode _oracle) {
		m_node = _oracle;
		image_registry = GTTlipse.getDefault().getImageRegistry();
		/* setup layout */
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 400;
		data.heightHint = 200;

		/* init Tree */
		m_tree = new Tree(area, SWT.NULL);
		m_tree.setLayoutData(data);

		/* find oracle file */
		TestScriptNodeFinder finder = new TestScriptNodeFinder();
		TestScriptDocument doc = finder.getTestScriptDocument(m_node);

		if (doc == null)
			return;

		String filename = getOracleFileName(doc, m_node);
		IProject prj = EclipseProject.getEclipseProject();
		if (prj == null)
			return;

		/* add node to tree */
		IFile file = prj.getFile(filename);
		if (file.exists() == false)
			return;
		OracleInfoReader loader = new OracleInfoReader();
		String filepath = file.getLocation().toString();
		List<ViewAssertNode> assertlist = loader.readOracle(filepath);
		Iterator<ViewAssertNode> ite = assertlist.iterator();
		while (ite.hasNext()) {
			ViewAssertNode assertnode = (ViewAssertNode) ite.next();
			TreeItem item_cmptype = null;
			item_cmptype = findTreeItemByName(assertnode.getComponent()
					.getType());
			if (item_cmptype == null) {
				item_cmptype = new TreeItem(m_tree, SWT.NULL);
				item_cmptype.setText(assertnode.getComponent().getType());
				item_cmptype.setImage(image_registry.get(TestScriptTage.OK));
			}

			TreeItem item_name = null;
			item_name = findTreeItemByName(item_cmptype, assertnode
					.getComponent().getName());
			if (item_name == null) {
				item_name = new TreeItem(item_cmptype, SWT.NULL);
				item_name.setText(assertnode.getComponent().getName());
				item_name.setImage(image_registry.get(TestScriptTage.OK));
			}

			TreeItem item_assert = null;
			item_assert = findTreeItemByName(item_name, assertnode
					.getAssertion().toString());
			if (item_assert == null) {
				item_assert = new TreeItem(item_name, SWT.NULL);
				item_assert.setText(assertnode.getAssertion().toString());
				item_assert.setImage(image_registry.get(TestScriptTage.OK));
			}
		}

		/* add fail node to tree */
		file = prj.getFile(filename + ".rep");
		if (file.exists() == false)
			return;
		try {
			String cmptype = "";
			String cmpname = "";
			String assertion = "";

			BufferedReader br = new BufferedReader(new InputStreamReader(file
					.getContents()));
			String line = br.readLine();
			while (line != null) {
				if (line.startsWith("ComponentType::")) {
					cmptype = line.replaceFirst("ComponentType::", "");
					TreeItem item_cmptype = null;
					item_cmptype = findTreeItemByName(cmptype);
					if (item_cmptype == null) {
						item_cmptype = new TreeItem(m_tree, SWT.NULL);
						item_cmptype.setText(cmptype);
					}
					item_cmptype.setImage(image_registry
							.get(TestScriptTage.ERROR));
				} else if (line.startsWith("ComponentName::")) {
					cmpname = line.replaceFirst("ComponentName::", "");
					TreeItem item_cmptype = null;
					item_cmptype = findTreeItemByName(cmptype);
					if (item_cmptype == null) {
						line = br.readLine();
						continue;
					}

					TreeItem item_name = null;
					item_name = findTreeItemByName(item_cmptype, cmpname);
					if (item_name == null) {
						item_name = new TreeItem(item_cmptype, SWT.NULL);
						item_name.setText(cmpname);
					}
					item_name
							.setImage(image_registry.get(TestScriptTage.ERROR));
				} else if (line.startsWith("Assertion::")) {
					assertion = line.replaceFirst("Assertion::", "");
					TreeItem item_cmptype = null;
					item_cmptype = findTreeItemByName(cmptype);
					if (item_cmptype == null) {
						line = br.readLine();
						continue;
					}

					TreeItem item_name = null;
					item_name = findTreeItemByName(item_cmptype, cmpname);
					if (item_name == null) {
						line = br.readLine();
						continue;
					}

					TreeItem item_assert = null;
					item_assert = findTreeItemByName(item_name, assertion);
					if (item_assert == null) {
						item_assert = new TreeItem(item_name, SWT.NULL);
						item_assert.setText(assertion);
					}
					item_assert.setImage(image_registry
							.get(TestScriptTage.ERROR));
				} else if (line.startsWith("ActualVale::")) {
					String actualvalue = line.replaceFirst("ActualVale::", "");
					TreeItem item_cmptype = null;
					item_cmptype = findTreeItemByName(cmptype);
					if (item_cmptype == null) {
						line = br.readLine();
						continue;
					}

					TreeItem item_name = null;
					item_name = findTreeItemByName(item_cmptype, cmpname);
					if (item_name == null) {
						line = br.readLine();
						continue;
					}

					TreeItem item_assert = null;
					item_assert = findTreeItemByName(item_name, assertion);
					if (item_assert == null) {
						item_assert = new TreeItem(item_name, SWT.NULL);
					}
					item_assert.setText(assertion + "(actual:" + actualvalue
							+ ")");
				}
				line = br.readLine();
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param itemname
	 * @param item_cmptype
	 * @return ¤èªk findTreeItemByName
	 */
	private TreeItem findTreeItemByName(String itemname) {
		TreeItem item_result = null;
		for (TreeItem item : m_tree.getItems()) {
			if (item.getText().equals(itemname))
				item_result = item;
		}
		return item_result;
	}

	private TreeItem findTreeItemByName(TreeItem parent, String itemname) {
		TreeItem item_result = null;
		for (TreeItem item : parent.getItems()) {
			if (item.getText().equals(itemname))
				item_result = item;
		}
		return item_result;
	}

	public void update() {
	}

	private String getOracleFileName(TestScriptDocument m_doc, OracleNode node) {
		String result = "";
		String prefix = "";
		String postfix = "";
		if (m_doc == null || node == null)
			return result;
		TestMethodNode methodnode = (TestMethodNode) m_doc.getParent();
		prefix = methodnode.indexOf(m_doc) + "_";
		prefix = methodnode.getName() + "_" + prefix;
		CompositeNode compnode = methodnode.getParent();
		while ((compnode instanceof SourceFolderNode) == false) {
			prefix = compnode.getName() + "_" + prefix;
			compnode = compnode.getParent();
		}
		postfix = node.getParent().indexOf(node) + "";
		AbstractNode parent = node.getParent();
		while (parent != m_doc.getScript()) {
			postfix = parent.indexOf(parent) + "_" + postfix;
			parent = parent.getParent();
		}
		result = prefix + postfix;
		return result;
	}
}
