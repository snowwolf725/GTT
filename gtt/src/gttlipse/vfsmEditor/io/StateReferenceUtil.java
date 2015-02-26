package gttlipse.vfsmEditor.io;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.State;

public class StateReferenceUtil {
	static final String MainPart_Prefix1 = "Main::Main::";
	static final String MainPart_Prefix2 = "ROOT::Main::";

	private static boolean isForMainPart(String path) {
		return path.indexOf(MainPart_Prefix1) == 0
				|| path.indexOf(MainPart_Prefix2) == 0;
	}

	static final String DeclPart_Prefix1 = "Main::";
	static final String DeclPart_Prefix2 = "ROOT::FSM::";

	private static boolean isForDeclPart(String path) {
		return path.indexOf(DeclPart_Prefix1) == 0
				|| path.indexOf(DeclPart_Prefix2) == 0;
	}

	public static State findTarget(AbstractSuperState main,
			AbstractSuperState decl, String target) {
		if (isForMainPart(target)) {
			target = target.replace(MainPart_Prefix1, "");
			target = target.replace(MainPart_Prefix2, "");
			// 從main 找
			return find(main, target);
		}
		if (isForDeclPart(target)) {
			target = target.replace(DeclPart_Prefix1, "");
			target = target.replace(DeclPart_Prefix2, "");
			// 從 decl 找
			return find(decl, target);
		}
		return null; // 不正確的路徑
	}

	private static State find(State root, String path) {
		String[] paths = path.split("::");
		State target = root;

		for (int i = 0; i < paths.length; i++) {
			target = target.getChildrenByName(paths[i]);
			if (target == null)
				return null;
		}
		
		// ok find it.
		return target;
	}

}
