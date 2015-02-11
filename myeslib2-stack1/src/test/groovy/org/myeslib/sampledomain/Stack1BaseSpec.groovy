package org.myeslib.sampledomain

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.util.Modules
import org.myeslib.core.Event
import org.myeslib.data.UnitOfWorkId
import org.myeslib.sampledomain.aggregates.inventoryitem.InventoryItemModule
import org.myeslib.stack1.infra.dao.UnitOfWorkDao
import org.myeslib.stack1.infra.helpers.DatabaseHelper
import spock.lang.Specification

import java.util.function.Supplier

import static org.mockito.Mockito.when

abstract class Stack1BaseSpec<K>  extends Specification {

    static Injector injector;

    @Inject
    UnitOfWorkDao<K> unitOfWorkDao;
    @Inject
    Supplier<UnitOfWorkId> uowIdSupplier;

    protected abstract commandBus()

    protected <C> C command(C cmd) {
        commandBus().post(cmd)
        return C
    }

    protected List<Event> lastCmdEvents(K id) {
        unitOfWorkDao.getFull(id).get(1).events
    }

    def setup() {
        injector.injectMembers(this);
        injector.getInstance(DatabaseHelper.class).initDb();
        when(uowIdSupplier.get()).thenReturn(UnitOfWorkId.create(), expUowId)
    }
}
