package net.fun.inject.inject.wrapper.impl.other;

import net.fun.inject.inject.wrapper.Wrapper;
import net.fun.utils.Classes;
import net.fun.utils.Methods;

import javax.swing.text.WrappedPlainView;

public class TeamWrapper extends Wrapper {
    public TeamWrapper(Object o) {
        super(Classes.Team);
        this.obj=o;
    }

    public boolean isSameTeam(TeamWrapper team) {
        return (boolean)Methods.isSameTeam.invoke(obj,team.obj);
    }
}
