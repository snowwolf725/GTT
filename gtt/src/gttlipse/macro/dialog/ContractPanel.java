package gttlipse.macro.dialog;

import gtt.macro.macroStructure.MacroContract;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ContractPanel {
	private StyledText m_preCondition = null;
	private StyledText m_action = null;
	private StyledText m_postCondition = null;

	// private MacroContract m_Contract;

	public void setContract(MacroContract c) {
		// m_Contract = c;
		m_preCondition.setText(c.getPostCondition());
		m_action.setText(c.getAction());
		m_postCondition.setText(c.getPostCondition());
	}

	public MacroContract getContract() {
		MacroContract c = new MacroContract();
		c.setPreCondition(m_preCondition.getText());
		c.setAction(m_action.getText());
		c.setPostCondition(m_postCondition.getText());

		return c;
	}

	//
	// public void setPreCond(String condition) {
	// m_preCondition.setText(condition);
	// }
	//
	// public void setAction(String condition) {
	// m_action.setText(condition);
	// }
	//
	// public void setPostCond(String condition) {
	// m_postCondition.setText(condition);
	// }
	//
	// public String getPreCond() {
	// return m_preCondition.getText();
	// }
	//
	// public String getAction() {
	// return m_action.getText();
	// }
	//
	// public String getPostCond() {
	// return m_postCondition.getText();
	// }

	public ContractPanel(Composite parent) {
		initPanel(parent);
	}

	private boolean doCheckContext() {
		// doParse("pre");
		// doParse("post");
		textFormat(m_preCondition);
		textFormat(m_action);
		textFormat(m_postCondition);
		return true;
	}

	// private StyleRange getNormalStyle(int start, int length) {
	// StyleRange sr = new StyleRange(start, length, null, null);
	// return sr;
	// }

	private StyleRange getKeywordStyle(int start, int length) {
		// Color blue = new Color(null, 0, 0, 255);
		StyleRange sr = new StyleRange(start, length,
				UsedColors.PURE_BLUE_COLOR, null);
		sr.fontStyle = SWT.BOLD;
		return sr;
	}

	private StyleRange getOperatorStyle(int start, int length) {
		// Color red = new Color(null, 125, 0, 0);
		StyleRange sr = new StyleRange(start, length,
				UsedColors.DARK_RED_COLOR, null);
		sr.fontStyle = SWT.BOLD;
		return sr;
	}

	// private StyleRange getErrorStyle(int start, int length) {
	// Color red = new Color(null, 255, 200, 200);
	// StyleRange sr = new StyleRange(start, length, null, red);
	// return sr;
	// }

	private void textFormat(StyledText condition) {
		String s = condition.getText();

		// keyword "&&"
		int index = 0;
		while ((index = s.indexOf("&&", index)) != -1) {
			condition.setStyleRange(getOperatorStyle(index, 2));
			index += 2;
		}

		// keyword "||"
		index = 0;
		while ((index = s.indexOf("||", index)) != -1) {
			condition.setStyleRange(getOperatorStyle(index, 2));
			index += 2;
		}

		// keyword "!"
		index = 0;
		while ((index = s.indexOf("!", index)) != -1) {
			condition.setStyleRange(getOperatorStyle(index, 1));
			index += 1;
		}

		// keyword "add"
		index = 0;
		while ((index = s.indexOf("add", index)) != -1) {
			condition.setStyleRange(getKeywordStyle(index, 3));
			index += 3;
		}

		// keyword "remove"
		index = 0;
		while ((index = s.indexOf("remove", index)) != -1) {
			condition.setStyleRange(getKeywordStyle(index, 6));
			index += 6;
		}

		// keyword "exist"
		index = 0;
		while ((index = s.indexOf("exist", index)) != -1) {
			condition.setStyleRange(getKeywordStyle(index, 5));
			index += 5;
		}
	}

	private class ConditionModifyListener implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			doCheckContext();
		}
	}

	private void initPanel(Composite parent) {
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		final GridData gd = new GridData();
		final ConditionModifyListener _modifyListener = new ConditionModifyListener();
		final FontData fontdata = new FontData();
		fontdata.setHeight(10);
		fontdata.setName("courier");
		final Font font = new Font(null, fontdata);

		gridlayout.numColumns = 1;
		gd.heightHint = 100;
		gd.widthHint = 450;

		area.setLayout(gridlayout);

		Label _pre = new Label(area, SWT.NULL);
		_pre.setText("Pre-Condition:");
		m_preCondition = new StyledText(area, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		m_preCondition.setLayoutData(gd);
		m_preCondition.addModifyListener(_modifyListener);
		m_preCondition.setFont(font);

		Label _action = new Label(area, SWT.NULL);
		_action.setText("Action:");
		m_action = new StyledText(area, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		m_action.setLayoutData(gd);
		m_action.addModifyListener(_modifyListener);
		m_action.setFont(font);

		Label _post = new Label(area, SWT.NULL);
		_post.setText("Post-Condition:");
		m_postCondition = new StyledText(area, SWT.BORDER | SWT.MULTI
				| SWT.WRAP);
		m_postCondition.setLayoutData(gd);
		m_postCondition.addModifyListener(_modifyListener);
		m_postCondition.setFont(font);
	}
}
