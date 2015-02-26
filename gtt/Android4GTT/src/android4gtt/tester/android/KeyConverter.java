package android4gtt.tester.android;

import android.view.KeyEvent;

public class KeyConverter {

	public static int StringToKeyCode(String key) {
		if (key.equals("None")) // 20040305 ,art
		{
			return KeyEvent.KEYCODE_CLEAR;
		}
		if (key.equals("Alt")) {
			return KeyEvent.KEYCODE_ALT_LEFT;
		}
		if (key.equals("Shift")) {
			return KeyEvent.KEYCODE_SHIFT_LEFT;
		}
		if (key.equals("Enter")) {
			return KeyEvent.KEYCODE_ENTER;
		}
		if (key.equals("Tab")) {
			return KeyEvent.KEYCODE_TAB;
		}
		if (key.equals("Backspace")) {
			return KeyEvent.KEYCODE_DEL;
		}
		if (key.equals("Home")) {
			return KeyEvent.KEYCODE_HOME;
		}
		if (key.equals("Menu")) {
			return KeyEvent.KEYCODE_MENU;
		}	
		if (key.equals("PgUp")) {
			return KeyEvent.KEYCODE_PAGE_UP;
		}
		if (key.equals("PgDn")) {
			return KeyEvent.KEYCODE_PAGE_DOWN;
		}
		if (key.equals("Back")) {
			return KeyEvent.KEYCODE_BACK;
		}
		if (key.equals("Left")) {
			return KeyEvent.KEYCODE_DPAD_LEFT;
		}
		if (key.equals("Right")) {
			return KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		if (key.equals("Up")) {
			return KeyEvent.KEYCODE_DPAD_UP;
		}
		if (key.equals("Down")) {
			return KeyEvent.KEYCODE_DPAD_DOWN;
		}
		if (key.equals("1")) {
			return KeyEvent.KEYCODE_1;
		}
		if (key.equals("2")) {
			return KeyEvent.KEYCODE_2;
		}
		if (key.equals("3")) {
			return KeyEvent.KEYCODE_3;
		}
		if (key.equals("4")) {
			return KeyEvent.KEYCODE_4;
		}
		if (key.equals("5")) {
			return KeyEvent.KEYCODE_5;
		}
		if (key.equals("6")) {
			return KeyEvent.KEYCODE_6;
		}
		if (key.equals("7")) {
			return KeyEvent.KEYCODE_7;
		}
		if (key.equals("8")) {
			return KeyEvent.KEYCODE_8;
		}
		if (key.equals("9")) {
			return KeyEvent.KEYCODE_9;
		}
		if (key.equals("0")) {
			return KeyEvent.KEYCODE_0;
		}
		if (key.equals("a")) {
			return KeyEvent.KEYCODE_A;
		}
		if (key.equals("b")) {
			return KeyEvent.KEYCODE_B;
		}
		if (key.equals("c")) {
			return KeyEvent.KEYCODE_C;
		}
		if (key.equals("d")) {
			return KeyEvent.KEYCODE_D;
		}
		if (key.equals("e")) {
			return KeyEvent.KEYCODE_E;
		}
		if (key.equals("f")) {
			return KeyEvent.KEYCODE_F;
		}
		if (key.equals("g")) {
			return KeyEvent.KEYCODE_G;
		}
		if (key.equals("h")) {
			return KeyEvent.KEYCODE_H;
		}
		if (key.equals("i")) {
			return KeyEvent.KEYCODE_I;
		}
		if (key.equals("j")) {
			return KeyEvent.KEYCODE_J;
		}
		if (key.equals("k")) {
			return KeyEvent.KEYCODE_K;
		}
		if (key.equals("l")) {
			return KeyEvent.KEYCODE_L;
		}
		if (key.equals("m")) {
			return KeyEvent.KEYCODE_M;
		}
		if (key.equals("n")) {
			return KeyEvent.KEYCODE_N;
		}
		if (key.equals("o")) {
			return KeyEvent.KEYCODE_O;
		}
		if (key.equals("p")) {
			return KeyEvent.KEYCODE_P;
		}
		if (key.equals("q")) {
			return KeyEvent.KEYCODE_Q;
		}
		if (key.equals("r")) {
			return KeyEvent.KEYCODE_R;
		}
		if (key.equals("s")) {
			return KeyEvent.KEYCODE_S;
		}
		if (key.equals("t")) {
			return KeyEvent.KEYCODE_T;
		}
		if (key.equals("u")) {
			return KeyEvent.KEYCODE_U;
		}
		if (key.equals("v")) {
			return KeyEvent.KEYCODE_V;
		}
		if (key.equals("w")) {
			return KeyEvent.KEYCODE_W;
		}
		if (key.equals("x")) {
			return KeyEvent.KEYCODE_X;
		}
		if (key.equals("y")) {
			return KeyEvent.KEYCODE_Y;
		}
		if (key.equals("z")) {
			return KeyEvent.KEYCODE_Z;
		}
		if (key.equals("Space")) {
			return KeyEvent.KEYCODE_SPACE;
		}
		if (key.equals("@")) {
			return KeyEvent.KEYCODE_AT;
		}
		if (key.equals("#")) {
			return KeyEvent.KEYCODE_POUND;
		}
		if (key.equals("\\")) {
			return KeyEvent.KEYCODE_BACKSLASH;
		}
		if (key.equals(".")) {
			return KeyEvent.KEYCODE_PERIOD;
		}
		if (key.equals(",")) {
			return KeyEvent.KEYCODE_COMMA;
		}
		if (key.equals(";")) {
			return KeyEvent.KEYCODE_SEMICOLON;
		}
		if (key.equals("+")) {
			return KeyEvent.KEYCODE_PLUS;
		}
		if (key.equals("-")) {
			return KeyEvent.KEYCODE_MINUS;
		}
		if (key.equals("*")) {
			return KeyEvent.KEYCODE_STAR;
		}
		if (key.equals("/")) {
			return KeyEvent.KEYCODE_SLASH;
		}
		if (key.equals("=")) {
			return KeyEvent.KEYCODE_EQUALS;
		}
		if (key.equals("[")) {
			return KeyEvent.KEYCODE_LEFT_BRACKET;
		}
		if (key.equals("]")) {
			return KeyEvent.KEYCODE_RIGHT_BRACKET;
		}
		return -1;
	}
}