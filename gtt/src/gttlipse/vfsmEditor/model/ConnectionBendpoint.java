package gttlipse.vfsmEditor.model;

import org.eclipse.draw2d.geometry.Dimension;

public class ConnectionBendpoint extends Element {

//	private final static float WEIGHT = 0.5f;

	private Dimension m_d1, m_d2;

	public ConnectionBendpoint() {
	}

	public Dimension firstRelativeDim() {
		return m_d1;
	}

	public Dimension secondRelativeDim() {
		return m_d2;
	}

	public void setDimension(Dimension dim1, Dimension dim2) {
		m_d1 = dim1;
		m_d2 = dim2;
	}

	public void setD1(int w, int h) {
		m_d1 = new Dimension(w, h);
	}

	public void setD2(int w, int h) {
		m_d2 = new Dimension(w, h);
	}

	public Dimension getD1() {
		return m_d1;
	}

	public Dimension getD2() {
		return m_d2;
	}

}
