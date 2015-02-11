package org.myeslib.stack1.infra.dao;

import org.myeslib.core.Command;
import org.myeslib.core.CommandId;
import org.myeslib.data.UnitOfWork;

import java.util.List;

public interface UnitOfWorkDao<K> {

    List<UnitOfWork> getFull(K id);

    List<UnitOfWork> getPartial(K id, Long biggerThanThisVersion);

    void append(K targetId, CommandId commandId, Command command, UnitOfWork unitOfWork);

    Command getCommand(CommandId commandId);
}