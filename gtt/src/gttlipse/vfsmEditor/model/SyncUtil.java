package gttlipse.vfsmEditor.model;

import java.util.Iterator;

public class SyncUtil {

	// �H target �ӧ�s src �����e
	public static void sync(AbstractSuperState source, AbstractSuperState target) {
		// �P�B���A
		syncStates(source, target);
		// �P�B�s�u
		syncConnections(source, target);
	}

	private static void syncStates(AbstractSuperState source,
			AbstractSuperState target) {
		// target �S�������A�Asrc �N�����
		Iterator<State> site = source.getAll().iterator();
		while (site.hasNext()) {
			State s = site.next();
			if (target.getChildrenByName(s.getName()) != null)
				continue;
			source.removeState(s);
			// ���s���o iterator
			site = source.getAll().iterator();
		}

		// target �s�W�����A�Asource �]�o��۷s�W
		Iterator<State> tite = target.getAll().iterator();
		while (tite.hasNext()) {
			State s = tite.next();
			if (source.getChildrenByName(s.getName()) != null)
				continue;
			State ns = s.clone();
			if(source.getParent()!=null)
				ns.setParent(source.getParent());
			source.addState(ns);

			// ���s���o iterator
			tite = target.getAll().iterator();
		}
	}

	private static void syncConnections(AbstractSuperState source,
			AbstractSuperState target) {
		// target �s�W���s�u�A source �o�n��
		// ����clone �Ҧ��s�u�Y�i�A���ƪ��s�u�O���|�A�W�[��
		ConnectionBase.cloneConnections(target, source);
	}

}
