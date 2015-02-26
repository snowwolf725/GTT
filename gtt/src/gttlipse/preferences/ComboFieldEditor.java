/**
 * 
 */
package gttlipse.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * @author ¤ýºaÄQ
 * 
 *         created first in project GTTlipse.preferences
 * 
 */
public class ComboFieldEditor extends FieldEditor {
	private Combo _combo;

	/**
	 * Old text value.
	 */
	/**
	 * Creates a Combo field editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public ComboFieldEditor(String name, String labelText, Composite parent) {
		init(name, labelText);
		createControl(parent);
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		GridData gd = (GridData) _combo.getLayoutData();
		gd.horizontalSpan = numColumns - 1;

		// We only grab excess space if we have to
		// If another field editor has more columns then
		// we assume it is setting the width.
		gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		getLabelControl(parent);

		_combo = getComboControl(parent);
		_combo.setLayoutData(createGridData(numColumns));
	}

	private GridData createGridData(int numColumns) {
		GridData gd = new GridData();
		gd.horizontalSpan = numColumns - 1;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		return gd;
	}

	@Override
	protected void doLoad() {
		if (_combo == null)
			return;

		String value = getPreferenceStore().getString(getPreferenceName());
		_combo.setText(value);
	}

	@Override
	protected void doLoadDefault() {
		if (_combo == null)
			return;

		String value = getPreferenceStore().getDefaultString(
				getPreferenceName());
		_combo.setText(value);
	}

	@Override
	protected void doStore() {
		getPreferenceStore().setValue(getPreferenceName(), _combo.getText());
	}

	@Override
	public int getNumberOfControls() {
		return 2;
	}

	/**
	 * Returns this field editor's text control.
	 * <p>
	 * The control is created if it does not yet exist
	 * </p>
	 * 
	 * @param parent
	 *            the parent
	 * @return the text control
	 */
	public Combo getComboControl(Composite parent) {
		if (_combo == null) {
			_combo = createCombo(parent);
			_combo.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					_combo = null;
				}
			});
		} else {
			checkParent(_combo, parent);
		}

		return _combo;
	}

	private Combo createCombo(Composite parent) {
		Combo c = new Combo(parent, SWT.DROP_DOWN);
		c.setFont(parent.getFont());
		return c;
	}

	public void addItem(String item) {
		_combo.add(item);
	}
}
