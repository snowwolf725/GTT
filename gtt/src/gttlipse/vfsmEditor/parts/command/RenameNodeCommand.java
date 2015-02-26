/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.command;

import gttlipse.vfsmEditor.model.State;

import org.eclipse.gef.commands.Command;


/**
 * @author zhanghao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RenameNodeCommand extends Command {

    private State m_State;
    private String m_newName;
    private String m_oldName;

    public void setName(String name) {
        m_newName = name;
    }

    public void setState(State node) {
        m_State = node;
    }

    public void execute() {
        m_oldName = this.m_State.getName();
        m_State.setName(m_newName);
    }

    public void redo() {
        m_State.setName(m_newName);
    }

    public void undo() {
        m_State.setName(m_oldName);
    }

    public String getLabel() {
        return "Rename Node";
    }
}