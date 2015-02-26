package gttlipse.vfsmEditor.view.dialog;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class GraphBuilderDialog extends TrayDialog {

	private static int m_vertex = 0;
	private static int m_edge = 0;

	public GraphBuilderDialog(Shell parentShell, int vertex, int edge) {
		super(parentShell);

		m_vertex = vertex;
		m_edge = edge;

		this.create();
		this.getShell().setText("IGraph Builder Dialog");
	}

	protected Control createDialogArea(Composite parent) {
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout areaLayout = new GridLayout();
		areaLayout.numColumns = 1;
		area.setLayout(areaLayout);

		final GridData layoutdata = new GridData();
		layoutdata.horizontalAlignment = GridData.CENTER;
		layoutdata.verticalAlignment = GridData.CENTER;
		area.setLayoutData(layoutdata);
		createIGBInfoPane(area);
		return area;
	}

	private void createIGBInfoPane(Composite parent) {
		final Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		group.setText("IGB Counter");

		Label label1 = new Label(group, SWT.SINGLE);
		String text1 = "Vertex numbers : " + m_vertex;
		label1.setText(text1);
		String text2 = "Edge numbers : " + m_edge;
		Label label2 = new Label(group, SWT.SINGLE);
		label2.setText(text2);
		Button btn = new Button(group, SWT.PUSH);
		btn.setText("IGB Info");
//		final Shell shell = this.getShell();

		btn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
			}
		});

		// my layout
		group.setBounds(5, 5, 350, 30);
		label1.setBounds(10, 20, 250, 20);
		label2.setBounds(10, 40, 250, 20);
		btn.setBounds(260, 15, 80, 20);
	}

	public void setVertexNumber(int vertex) {
		m_vertex = vertex;
	}

	public void setEdgeNumber(int edge) {
		m_edge = edge;
	}
}
