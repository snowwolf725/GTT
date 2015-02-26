package gtt.macro.macroStructure;

import java.text.NumberFormat;

/**
 * refactoing by usnig dumb-object
 * 2010/03/31
 * @author zwshen
 *
 */
public class MacroBadSmellData implements Cloneable {
	/**
	 * add by soriel
	 */
	private int m_totalBadSmellScore = 0;
	private int m_badSmellScore = 0;
	private int m_colorR = 0;
	private int m_colorG = 0;
	private int m_colorB = 0;
	
	public MyColor COLOR_RED = new MyColor(236, 22, 22);
	public MyColor COLOR_YELLOW = new MyColor(236, 236, 22);
	public MyColor COLOR_GREEN = new MyColor(22, 236, 22);
	
	public MacroBadSmellData clone() {
		MacroBadSmellData data = new MacroBadSmellData();
		data.setBadSmellScore(m_badSmellScore);
		data.setTotalBadSmellScore(m_totalBadSmellScore);
		data.setRGB(m_colorR, m_colorG, m_colorB);
		return data;
	}
	
	public MyColor getColor() {
		return new MyColor(getColorR(), getColorG(), getColorB());
	}
	
	public int getColorR() {
		return m_colorR;
	}
	
	public int getColorG() {
		return m_colorG;
	}
	
	public int getColorB() {
		return m_colorB;
	}
	
	public void setRGB(int r, int g, int b) {
		m_colorR = r;
		m_colorG = g;
		m_colorB = b;
	}
	
	public void setRGB(MyColor c) {
		m_colorR = c.getRed();
		m_colorG = c.getGreen();
		m_colorB = c.getBlue();
	}

	public void setTotalBadSmellScore(int usage) {
		m_totalBadSmellScore = usage;
	}

	public int getTotalBadSmellScore() {
		return m_totalBadSmellScore;
	}

	public void setBadSmellScore(int usage) {
		m_badSmellScore = usage;
	}

	public int getBadSmellScore() {
		return m_badSmellScore;
	}

	public String getBadSmellRate() {
		float result = 0;
		if (m_totalBadSmellScore != 0) {
			result = ((float) m_badSmellScore / (float) m_totalBadSmellScore) * 100;
		}
		// get 1 digit after decimal point
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		return nf.format(result);
	}

	public String getBadSmellInfo() {
		return " : " + getBadSmellRate() + "%" + " (" + getBadSmellScore()
				+ " / " + getTotalBadSmellScore() + ")";
	}

}
