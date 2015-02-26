package gtt.runner;

public class LaunchData {
	public static final String DEFAULT_CLASSPATH = ".";

	private String launchArgument = "";//web testing  up or next page
	private String launchClassName = "";//web windows title
	private String launchClassPath = DEFAULT_CLASSPATH;//web testing is url

	public LaunchData() {

	}

	public static LaunchData create(String clsPath, String clsName,
			String argu) {
		LaunchData info = new LaunchData();
		info.setClassPath(clsPath);
		info.setClassName(clsName);
		info.setArgument(argu);
		return info;
	}

	public String getClassName() {
		return launchClassName;
	}

	public void setClassName(String classname) {
		launchClassName = classname;
	}

	public void setClassPath(String classpath) {
		this.launchClassPath = classpath;
	}

	public String getClasspath() {
		return launchClassPath;
	}

	public String getArgument() {
		return launchArgument;
	}

	public void setArgument(String arg) {
		launchArgument = arg;
	}
}
