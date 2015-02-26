/**
 * 
 */
package gttlipse.scriptEditor.testScript;

import gtt.testscript.TestScriptDocument;
import gttlipse.scriptEditor.testScript.visitor.IGTTlipseScriptVisitor;

import java.util.ArrayList;
import java.util.List;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.TestScript
 * 
 */
public class TestMethodNode extends BaseNode {

	protected List<TestScriptDocument> m_docs;

	String DEFAULT_NAME = "TestMethodNode";

	public TestMethodNode() {
		super();
		init(DEFAULT_NAME);
	}

	public TestMethodNode(String name) {
		init(name);
	}

	void init(String name) {
		m_docs = new ArrayList<TestScriptDocument>();
		m_parent = null;
		setName(name);
	}

	public boolean hasDoc() {
		return size() > 0;
	}

	public int size() {
		return m_docs.size();
	}

	public void clearDoc() {
		if (hasDoc())
			m_docs.clear();
	}

	public void addDocument(TestScriptDocument doc) {
		m_docs.add(doc);
		doc.setParent(this);
	}

	public TestScriptDocument addInteractionSequence() {
		return addNewDoc("Interaction Sequence");
	}

//	public void addNewNode(TestScriptDocument doc) {
//		addDocument(doc);
//	}

	public TestScriptDocument addNewDoc(String name) {
		TestScriptDocument doc = TestScriptDocument.create();
		doc.setName(name);
		addDocument(doc);
		return doc;
	}

	public void removeDoc(TestScriptDocument doc) {
		m_docs.remove(doc);
	}

	public TestScriptDocument[] getDocuments() {
		if (hasDoc())
			return (TestScriptDocument[]) m_docs
					.toArray(new TestScriptDocument[m_docs.size()]);
		return null;
	}

	public TestScriptDocument getDocByName(String name, int no) {
		if (!hasDoc())
			return null;

		int count = no;
		TestScriptDocument docs[] = getDocuments();
		for (int i = 0; i < docs.length; i++)
			if (docs[i].getName().matches(name)) {
				count--;
				if (count == 0)
					return docs[i];
			}

		return null;
	}

	public TestScriptDocument getDocAt(int idx) {
		if (hasDoc() && idx < size())
			return getDocuments()[idx];

		return null;
	}

	/**
	 * clone 給複製貼上使用
	 */
	public TestMethodNode clone() {
		TestMethodNode methodnode = new TestMethodNode(m_nodeName);

		if (m_docs.size() == 0)
			return methodnode;

		TestScriptDocument[] docs = getDocuments();
		for (int i = 0; i < m_docs.size(); i++)
			methodnode.addDocument(docs[i].clone());
		return methodnode;
	}

	public boolean moveChildrenFront(TestScriptDocument child) {
		TestScriptDocument[] children = getDocuments();

		if (children[0].equals(child))
			return false;

		for (int i = 1; i < children.length; i++) {
			if (children[i].equals(child)) {
				TestScriptDocument tmp = children[i - 1];
				children[i - 1] = children[i];
				children[i] = tmp;
				break;
			}
		}
		setDocuments(children);
		return true;
	}

	private void setDocuments(TestScriptDocument[] children) {
		m_docs.clear();
		for (int i = 0; i < children.length; i++)
			m_docs.add(children[i]);
	}

	public boolean moveChildrenRear(TestScriptDocument child) {
		TestScriptDocument[] children = getDocuments();

		if (children[children.length - 1].equals(child))
			return false;

		for (int i = children.length - 1; i >= 0; i--) {
			if (children[i].equals(child)) {
				TestScriptDocument tmp = children[i + 1];
				children[i + 1] = children[i];
				children[i] = tmp;
				break;
			}
		}

		setDocuments(children);
		return true;
	}

	public int indexOf(TestScriptDocument doc) {
		if (m_docs.size() < 0)
			return 0;

		TestScriptDocument docs[] = getDocuments();
		for (int i = 0; i < docs.length; i++)
			if (docs[i] == doc)
				return i + 1;

		return 0;
	}

	public void accept(IGTTlipseScriptVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}
}
