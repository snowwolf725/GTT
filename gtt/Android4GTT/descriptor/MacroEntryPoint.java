import java.io.File;
import java.io.InputStream;

import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android4gtt.runner.Runner;

public class TestFileTemplate extends ActivityInstrumentationTestCase2<ACTIVITYNAME> {
	private Runner runner = null;
	
	public TestFileTemplate() throws ClassNotFoundException {
		super("PKGNAME", ACTIVITYNAME.class);
	}
	
	public String getMethodName(){
		StackTraceElement e[] = Thread.currentThread().getStackTrace();
		String method = e[3].getMethodName();
		return method;
	}
	
	@Override
	protected void setUp() throws Exception {
		Resources resources= this.getInstrumentation().getContext().getResources();
		InputStream desc_file = resources.openRawResource(R.raw.android);
		
		runner = new Runner(this, desc_file);
		runner.setSolo(getInstrumentation(), getActivity());
		runner.setRes(PKGNAME.R.id.class);
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		getActivity().runOnUiThread(new Runnable(){
			public void run() {
				Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			}
		});
	}

	@Override
	public void tearDown() throws Exception {
		runner.finalize();
		clearApplicationData();
		getActivity().finish();
		super.tearDown();
	}

	public void testMacro() {
		runner.GTTTestScript(this.getMethodName(), "Macro Script");
	}
	
	public void clearApplicationData() {
		File cache = getActivity().getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				if (!s.equals("lib")) {
					deleteDir(new File(appDir, s));
				}
			}
		}
	}
		
	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
}
	

