package fun.inject.inject.wrapper.impl.other;

import fun.inject.inject.wrapper.Wrapper;
import fun.utils.Classes;
import fun.utils.Methods;

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
