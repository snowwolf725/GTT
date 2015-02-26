package gttlipse.vfsmEditor.model;

import java.util.Iterator;

public class SyncUtil {

	// 以 target 來更新 src 的內容
	public static void sync(AbstractSuperState source, AbstractSuperState target) {
		// 同步狀態
		syncStates(source, target);
		// 同步連線
		syncConnections(source, target);
	}

	private static void syncStates(AbstractSuperState source,
			AbstractSuperState target) {
		// target 沒有的狀態，src 就不能用
		Iterator<State> site = source.getAll().iterator();
		while (site.hasNext()) {
			State s = site.next();
			if (target.getChildrenByName(s.getName()) != null)
				continue;
			source.removeState(s);
			// 重新取得 iterator
			site = source.getAll().iterator();
		}

		// target 新增的狀態，source 也得跟著新增
		Iterator<State> tite = target.getAll().iterator();
		while (tite.hasNext()) {
			State s = tite.next();
			if (source.getChildrenByName(s.getName()) != null)
				continue;
			State ns = s.clone();
			if(source.getParent()!=null)
				ns.setParent(source.getParent());
			source.addState(ns);

			// 重新取得 iterator
			tite = target.getAll().iterator();
		}
	}

	private static void syncConnections(AbstractSuperState source,
			AbstractSuperState target) {
		// target 新增的連線， source 得要有
		// 直接clone 所有連線即可，重複的連線是不會再增加的
		ConnectionBase.cloneConnections(target, source);
	}

}
