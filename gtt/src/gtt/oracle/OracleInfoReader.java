package gtt.oracle;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEventModel;
import gtt.testscript.NodeFactory;
import gtt.testscript.ViewAssertNode;
import gtt.util.refelection.ReflectionUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class OracleInfoReader {
	private IEventModel m_SwingModel = EventModelFactory.getDefault();

	private NodeFactory m_NodeFactory = new NodeFactory();

	private List<ViewAssertNode> m_list = new Vector<ViewAssertNode>();

	public List<ViewAssertNode> readOracle(String filename) {
		try {
			// init XML
			File file = new File(filename);
			if (!file.exists())
				return m_list;

			org.w3c.dom.Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(file);

			org.w3c.dom.Node node0 = (Element) doc.getDocumentElement();
			if (node0 == null) {
				System.out.println("File: " + filename + " isn't correct.");
				return new Vector<ViewAssertNode>(); // an empty list
			}
			/* Load TestOracle */
			for (int i = 0; i < node0.getChildNodes().getLength(); i++) {
				getTreeNodeFromDOM(node0.getChildNodes().item(i));
			}

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return m_list;
	}

	private boolean isElementNode(org.w3c.dom.Node node) {
		return (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE);
	}

	private void getTreeNodeFromDOM(org.w3c.dom.Node node) {
		if (isElementNode(node))
			processViewAsseertionNode((org.w3c.dom.Element) node);
	}

	/* for ViewAssertionNode element */
	private void processViewAsseertionNode(Element e) {
		try {
			IComponent com = null;
			Assertion assertion = null;
			for (int i = 0, length = e.getChildNodes().getLength(); i < length; i++) {
				if (!isElementNode(e.getChildNodes().item(i)))
					continue;

				if (e.getChildNodes().item(i).getNodeName().compareTo("Comp") == 0)
					com = readComponent((Element) e.getChildNodes().item(i));
				else if (e.getChildNodes().item(i).getNodeName().compareTo(
						"Assert") == 0)
					assertion = readAssert((Element) e.getChildNodes().item(i),
							com);
			}
			if (com == null || assertion == null)
				return;
			ViewAssertNode n = m_NodeFactory.createViewAssertNode(com,
					assertion);
			m_list.add(n);
		} catch (Exception exc) {
			System.out.println(exc);
		}
	}

	/* 讀取 SwingCompoent 的資訊 */
	private IComponent readComponent(Element e) throws ClassNotFoundException {
		IComponent c = m_SwingModel.getComponent(e
				.getAttribute(IOracleHandler.TAG_ComponentType));
		String winType = e.getAttribute(IOracleHandler.TAG_WindowType);
		String winTitle = e.getAttribute(IOracleHandler.TAG_WindowTitle);
		String comName = e.getAttribute(IOracleHandler.TAG_Name);
		String text = e.getAttribute(IOracleHandler.TAG_Text);
		String idx1 = e.getAttribute(IOracleHandler.TAG_IdxInWindow);
		String idx2 = e.getAttribute(IOracleHandler.TAG_IdxOfSameName);

		c.setWinType(winType);
		c.setTitle(winTitle);
		c.setName(comName);
		c.setText(text);
		c.setIndex(Integer.parseInt(idx1));
		c.setIndexOfSameName(Integer.parseInt(idx2));

		return c;
	}

	/* 讀取 Assertion 的資訊 */
	private Assertion readAssert(Element e, IComponent c) {
		Assertion a = new Assertion();
		a.setValue(e.getAttribute("value"));
		// Oracle 裡的assert 不需要讀msg
		// assertion.setMessage(e.getAttribute("msg"));
		a.setMessage("");
		try {
			a.setMethod(ReflectionUtil.getMethod(Class.forName(c.getType()), e
					.getAttribute("method")));
		} catch (ClassNotFoundException e1) {
			System.out.println(e1);
			return null;
		}

		/*
		 * NOTE: Oracle 中的view assert 似乎不會用到參數
		 * zwshen 2009/12/23
		 */
//		for (int i = 0, counts = e.getChildNodes().getLength(); i < counts; i++) {
//			if (!isElementNode(e.getChildNodes().item(i)))
//				continue;
//			Element arg = (Element) e.getChildNodes().item(i);
//			a.getArgumentList().add(
//					Argument.create(arg.getAttribute("type"), arg
//							.getAttribute("name"), arg.getAttribute("value")));
//		}
		return a;
	}
}
