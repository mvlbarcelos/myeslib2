package org.myeslib.stack1.infra;

import org.myeslib.data.*;
import org.myeslib.infra.UnitOfWorkDao;
import org.myeslib.infra.UnitOfWorkJournal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

public class Stack1Journal<K> implements UnitOfWorkJournal<K> {

    static final Logger logger = LoggerFactory.getLogger(Stack1Journal.class);

    private final UnitOfWorkDao<K> dao;
    private final List<Consumer<EventMessage>> consumers;

    @Inject
    public Stack1Journal(UnitOfWorkDao<K> dao, List<Consumer<EventMessage>> consumers) {
        checkNotNull(dao);
        this.dao = dao;
        checkNotNull(consumers);
        this.consumers = consumers;
    }

    @Override
    public void append(K targetId, CommandId commandId, Command command, UnitOfWork unitOfWork) {
        try {
            dao.append(targetId, commandId, command, unitOfWork);
            for (Consumer<EventMessage> consumer : consumers) {
                logger.debug("consumer.post {}", unitOfWork);
                for (Event event : unitOfWork.getEvents()) {
                    consumer.accept(new EventMessage(EventId.create(), event));
                }
            }
        } catch (Exception e) {
            throw e ;
        }
    }

}
