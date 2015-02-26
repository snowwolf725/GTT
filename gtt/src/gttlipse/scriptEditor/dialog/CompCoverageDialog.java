/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.eventmodel.IComponent;

import java.awt.Dimension;
import java.util.Vector;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.experimental.chart.swt.ChartComposite;



/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class CompCoverageDialog extends TitleAreaDialog {
	
	private Vector<IComponent> m_comps;
	private Vector<IComponent> m_comps_used;

	/**
	 * @param parentShell
	 */
	public CompCoverageDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	public CompCoverageDialog(Shell parentShell,Vector<IComponent> comps,Vector<IComponent> comps_used) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		m_comps = comps;
		m_comps_used = comps_used;
	}

	protected Control createDialogArea(Composite parent) {
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 500;
		data.heightHint = 500;
		
		parent.setBounds(50, 50, 500, 500);
		DefaultPieDataset dataset = setDataset();
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setRefreshBuffer(true);
		chartPanel.setPreferredSize(new Dimension(500,500));
		final ChartComposite frame = new ChartComposite(parent, SWT.NONE, chart,false);
		frame.pack(true);
		frame.setRedraw(true);
		frame.setLayoutData(data);
		
		return parent;
	}
	
	private DefaultPieDataset setDataset(){
		double all = m_comps.size();
		double used = m_comps_used.size();
		double coverage = used/all;
		double noncoverage = (all - used)/all;
		java.text.DecimalFormat nf = new java.text.DecimalFormat("###,##0.000"); 
		DefaultPieDataset defaultpiedataset = new DefaultPieDataset();
		defaultpiedataset.setValue("Coverage\n"+nf.format(coverage), coverage);
		defaultpiedataset.setValue("Non-Coverage\n"+nf.format(noncoverage), noncoverage);
		return defaultpiedataset;
	}
	
	private JFreeChart createChart(PieDataset piedataset){
		JFreeChart jfreechart = ChartFactory.createPieChart("Coverage Report",piedataset,true,true,false);
		return jfreechart;
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);
		Button btnAdd = createButton(parent, SWT.OK, "OK", true);
		btnAdd.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

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
}
