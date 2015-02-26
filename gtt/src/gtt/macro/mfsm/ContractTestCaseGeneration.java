package gtt.macro.mfsm;

import gtt.editor.configuration.ConfigurationData;
import gtt.editor.configuration.IConfiguration;
import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.OracleNode;
import gtt.macro.mfsm.ETSBuilder;
import gttlipse.testgen.algorithm.TestCaseGenerator;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.testsuite.TestSuite;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class ContractTestCaseGeneration {
	private MacroDocument m_MacroDoc;

	private String m_filename = "FILENAME";
	private String m_output = "";
	private int m_level;

	public static final int ALL_COVERAGE = 0;
	public static final int STATE_COVERAGE = 1;
	public static final int EDGE_COVERAGE = 2;
	public static final int CEDGE_COVERAGE = 3;

	private int m_CoverageOption = ALL_COVERAGE;

	public void setCoverageOption(int opt) {
		m_CoverageOption = opt;
	}

	public ContractTestCaseGeneration() {
	}

	IConfiguration m_Configuration = new ConfigurationData();

	public void run(String gttfile, int level, String output) {
		m_filename = gttfile;
		m_output = output;
		m_level = level;
		m_MacroDoc = MacroDocument.createFromFile(m_filename);

		buildTestCase();
	}

	public void run(String gttfile, int opt) {
		m_filename = gttfile;
		m_CoverageOption = opt;
		if (m_CoverageOption == STATE_COVERAGE)
			m_output = "testcases_sc";
		if (m_CoverageOption == EDGE_COVERAGE)
			m_output = "testcases_ec";
		if (m_CoverageOption == CEDGE_COVERAGE)
			m_output = "testcase_cc";
		m_level = ETSBuilder.ANY_LEVEL;
		m_MacroDoc = MacroDocument.createFromFile(m_filename);

		buildTestCase();
	}

	private void buildTestCase() {
		try {
			IGraph ets = generateETS();
			
			outETSInformation(ets);
			
			//	generateTestCase(ets);
		} catch (Exception exp) {
			exp.printStackTrace();
			return;
		}

	}

	private IGraph generateETS() {
		System.out.println("Generate ETS...");
		ETSBuilder builder = new ETSBuilder(m_MacroDoc.getMacroScript());
		builder.build(m_level);

		builder.saveSimpleGraph(m_output + ".ets");

		return builder.getGraph();
	}

	public void writeOutMacroTestCase() {
		// 儲存macro中所有產生的 test cases
		m_MacroDoc.saveFile(m_output + ".gtt");

		// Configuratiion information
		m_Configuration.openFile(m_filename);
		m_Configuration.saveFile(m_output + ".gtt");

	}

	private void outETSInformation(IGraph g) {
		System.out.println("METS State: " + g.vertices().size());
		System.out.println("METS Edge: " + g.edges().size());
	}

	public void generateTestCaseByAlgorithm(IGraph g) {
		TestCaseGenerator gen = new TestCaseGenerator(g);

		if (m_CoverageOption == ALL_COVERAGE
				|| m_CoverageOption == STATE_COVERAGE)
			coverage(gen.forStateCoverage(), m_output + "_SC.txt");

		if (m_CoverageOption == ALL_COVERAGE
				|| m_CoverageOption == EDGE_COVERAGE)
			coverage(gen.forTransitionCoverage(), m_output + "_EC.txt");

		if (m_CoverageOption == ALL_COVERAGE
				|| m_CoverageOption == CEDGE_COVERAGE)
			coverage(gen.forConcesutiveCoverage(), m_output + "_EC.txt");
	}

	private void coverage(TestSuite suite, String desc) {
		System.out.println("\t" + desc + " - test cases:" + suite.size()
				+ ", Total Length: " + suite.getTotalSize() + ", Avg Length: "
				+ suite.getAverageLength());

		output(desc, suite);
		toMacroTestCases(suite);
	}

	private void output(String desc, TestSuite suite) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(desc));

			int i = 0;
			for (i = 0; i < suite.size(); i++) {
				Object[] testcase = suite.getTestCase(i);
				writer.write("Test Case " + i + "(" + testcase.length + ") - ");
				for (int j = 0; j < testcase.length; j++) {
					writer.write(testcase[j].toString() + ", ");
				}
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void toMacroTestCases(TestSuite suite) {
		for (int i = 0; i < suite.size(); i++) {
			Object[] testcase = suite.getTestCase(i);
			processTestCase("TEST" + (i + 1), testcase);
		}
	}

	private void processTestCase(String name, Object[] events) {
		MacroEventNode testcase = MacroEventNode.create(name);
		for (int j = 0; j < events.length; j++) {
			if (events[j] instanceof MacroEventNode) {
				MacroEventNode me = (MacroEventNode) events[j];
				testcase.add(new MacroEventCallerNode(me.getPath()));
				// 每一個event 之後都加上一個Test Oracle
				OracleNode on = new OracleNode();
				// 設定子目錄 - 以方便統一存放Test Oracle
				on.getOracleData().setSubDir(name);
				testcase.add(on);
			}
		}
		m_MacroDoc.getMacroScript().add(testcase);
	}
}