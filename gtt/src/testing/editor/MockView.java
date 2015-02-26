package testing.editor;

import gtt.editor.view.ITestScriptView;

class MockView implements ITestScriptView {

	int level = 0;

//	public int getViewLevel() {
//		level++;
//		if (level == 1)
//			return ITestScriptView.SIMPLE_VIEW_LEVEL;
//		if (level == 2)
//			return ITestScriptView.NORMAL_VIEW_LEVEL;
//		if (level == 3)
//			return ITestScriptView.DETAIL_VIEW_LEVEL;
//
//		return 9999; // error view level
//	}

	public String acquireFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

	public String acquireInput() {
		// TODO Auto-generated method stub
		return null;
	}

	public void showMessage(String msg) {
		// TODO Auto-generated method stub

	}

	public void updateUI() {
		// TODO Auto-generated method stub

	}

	public String acquireInput(String msg) {
		return msg;
	}

}
