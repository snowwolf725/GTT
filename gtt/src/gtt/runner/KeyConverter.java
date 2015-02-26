package gtt.runner;

import java.awt.event.KeyEvent;

public class KeyConverter {
	static final String FILITER_STRING = ",./;=[\\]";

	public static int CharToKeyCode(char c) {
		if (isAlpha(c)) {
			// 使用小寫字母
			return Character.toUpperCase(c);
		}

		if (isNumber(c)) {
			// 數字字元
			return c;
		}

		int index = FILITER_STRING.indexOf(c);

		if (index >= 0)
			return c;

		switch (c) {
		case '~':
			return '`';// KeyEvent.VK_INVERTED_EXCLAMATION_MARK;
		case '!':
			return '1';// KeyEvent.VK_EXCLAMATION_MARK;
		case '@':
			return '2';// KeyEvent.VK_AT;
		case '#':
			return '3';// KeyEvent.VK_NUMBER_SIGN;
		case '$':
			return '4';// KeyEvent.VK_DOLLAR;
		case '%':
			return '5';// KeyEvent.VK_EURO_SIGN;
		case '^':
			return '6';// KeyEvent.VK_CIRCUMFLEX;
		case '&':
			return '7';// KeyEvent.VK_AMPERSAND;
		case '*':
			return '8';// KeyEvent.VK_ASTERISK;
		case '(':
			return '9';// KeyEvent.VK_LEFT_PARENTHESIS;
		case ')':
			return '0';// KeyEvent.VK_RIGHT_PARENTHESIS;
		case '_':
			return '-';// KeyEvent.VK_MINUS;
		case '+':
			return '=';// KeyEvent.VK_PLUS;
		case '"':
			String s = "'";
			return s.charAt(0);
		case '<':
			return ',';// KeyEvent.VK_LESS;
		case '>':
			return '.';// KeyEvent.VK_GREATER;
		case '{':
			return '[';// KeyEvent.VK_BRACELEFT;
		case '}':
			return ']';// KeyEvent.VK_BRACERIGHT;
		case ':':
			return ';';// KeyEvent.VK_COLON;
			// case '_': return '';//KeyEvent.VK_UNDERSCORE;
		case '?':
			return '/';// KeyEvent.VK_SPACE;
		case '|':
			return '\\';
		case ' ':
			return ' ';
		}

		return 0;
	}

	private static boolean isAlpha(char c) {
		return c >= KeyEvent.VK_A && c <= KeyEvent.VK_Z;
	}

	private static boolean isNumber(char c) {
		return c >= KeyEvent.VK_0 && c <= KeyEvent.VK_9;
	}

	public static char StringToKeyChar(String item) {
		if (item.equals("Backspace") || item.equals("Delete")
				|| item.equals("Enter") || item.equals("Esc"))
			return (char) StringToKeyCode(item);

		return 65535;
	}

	public static int StringToKeyCode(String key) {
		if (key.equals("None")) // 20040305 ,art
		{
			return KeyEvent.VK_CLEAR;
		}
		if (key.equals("Alt")) {
			return KeyEvent.VK_ALT;
		}
		if (key.equals("Ctrl")) {
			return KeyEvent.VK_CONTROL;
		}
		if (key.equals("Shift")) {
			return KeyEvent.VK_SHIFT;
		}
		if (key.equals("Enter")) {
			return KeyEvent.VK_ENTER;
		}
		if (key.equals("Tab")) {
			return KeyEvent.VK_TAB;
		}
		if (key.equals("CapsLock")) {
			return KeyEvent.VK_CAPS_LOCK;
		}
		if (key.equals("F1")) {
			return KeyEvent.VK_F1;
		}
		if (key.equals("F2")) {
			return KeyEvent.VK_F2;
		}
		if (key.equals("F3")) {
			return KeyEvent.VK_F3;
		}
		if (key.equals("F4")) {
			return KeyEvent.VK_F4;
		}
		if (key.equals("F5")) {
			return KeyEvent.VK_F5;
		}
		if (key.equals("F6")) {
			return KeyEvent.VK_F6;
		}
		if (key.equals("F7")) {
			return KeyEvent.VK_F7;
		}
		if (key.equals("F8")) {
			return KeyEvent.VK_F8;
		}
		if (key.equals("F9")) {
			return KeyEvent.VK_F9;
		}
		if (key.equals("F10")) {
			return KeyEvent.VK_F10;
		}
		if (key.equals("F11")) {
			return KeyEvent.VK_F11;
		}
		if (key.equals("F12")) {
			return KeyEvent.VK_F12;
		}
		if (key.equals("Esc")) {
			return KeyEvent.VK_ESCAPE;
		}
		if (key.equals("PrtSc")) {
			return KeyEvent.VK_PRINTSCREEN;
		}
		if (key.equals("ScrLk")) {
			return KeyEvent.VK_SCROLL_LOCK;
		}
		if (key.equals("Pause")) {
			return KeyEvent.VK_PAUSE;
		}
		if (key.equals("Insert")) {
			return KeyEvent.VK_INSERT;
		}
		if (key.equals("Delete")) {
			return KeyEvent.VK_DELETE;
		}
		if (key.equals("Home")) {
			return KeyEvent.VK_HOME;
		}
		if (key.equals("End")) {
			return KeyEvent.VK_END;
		}
		if (key.equals("PgUp")) {
			return KeyEvent.VK_PAGE_UP;
		}
		if (key.equals("PgDn")) {
			return KeyEvent.VK_PAGE_DOWN;
		}
		if (key.equals("NumLk")) {
			return KeyEvent.VK_NUM_LOCK;
		}
		if (key.equals("Backspace")) {
			return KeyEvent.VK_BACK_SPACE;
		}
		if (key.equals("Left")) {
			return KeyEvent.VK_LEFT;
		}
		if (key.equals("Right")) {
			return KeyEvent.VK_RIGHT;
		}
		if (key.equals("Up")) {
			return KeyEvent.VK_UP;
		}

		if (key.equals("Down")) {
			return KeyEvent.VK_DOWN;
		}

		return -1;
	}

	public static String KeyCodeToString(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_CLEAR:
			return "None"; // 20040305, art
		case KeyEvent.VK_ALT:
			return "Alt";
		case KeyEvent.VK_CONTROL:
			return "Ctrl";
		case KeyEvent.VK_SHIFT:
			return "Shift";
		case KeyEvent.VK_ENTER:
			return "Enter";
		case KeyEvent.VK_TAB:
			return "Tab";
		case KeyEvent.VK_CAPS_LOCK:
			return "CapsLock";
		case KeyEvent.VK_F1:
			return "F1";
		case KeyEvent.VK_F2:
			return "F2";
		case KeyEvent.VK_F3:
			return "F3";
		case KeyEvent.VK_F4:
			return "F4";
		case KeyEvent.VK_F5:
			return "F5";
		case KeyEvent.VK_F6:
			return "F6";
		case KeyEvent.VK_F7:
			return "F7";
		case KeyEvent.VK_F8:
			return "F8";
		case KeyEvent.VK_F9:
			return "F9";
		case KeyEvent.VK_F10:
			return "F10";
		case KeyEvent.VK_F11:
			return "F11";
		case KeyEvent.VK_F12:
			return "F12";
		case KeyEvent.VK_ESCAPE:
			return "Esc";
		case KeyEvent.VK_PRINTSCREEN:
			return "PrtSc";
		case KeyEvent.VK_SCROLL_LOCK:
			return "ScrLk";
		case KeyEvent.VK_PAUSE:
			return "Pause";
		case KeyEvent.VK_INSERT:
			return "Insert";
		case KeyEvent.VK_DELETE:
			return "Delete";
		case KeyEvent.VK_HOME:
			return "Home";
		case KeyEvent.VK_END:
			return "End";
		case KeyEvent.VK_PAGE_UP:
			return "PgUp";
		case KeyEvent.VK_PAGE_DOWN:
			return "PgDn";
		case KeyEvent.VK_NUM_LOCK:
			return "NumLk";
		case KeyEvent.VK_BACK_SPACE:
			return "Backspace";
		case KeyEvent.VK_LEFT:
			return "Left";
		case KeyEvent.VK_RIGHT:
			return "Right";
		case KeyEvent.VK_UP:
			return "Up";
		case KeyEvent.VK_DOWN:
			return "Down";

		}
		return "";
	}
}