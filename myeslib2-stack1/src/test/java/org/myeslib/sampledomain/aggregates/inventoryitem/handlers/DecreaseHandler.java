package org.myeslib.sampledomain.aggregates.inventoryitem.handlers;

import net.jcip.annotations.NotThreadSafe;
import org.myeslib.core.CommandHandler;
import org.myeslib.core.StatefulCommandHandler;
import org.myeslib.infra.Snapshot;
import org.myeslib.data.UnitOfWork;
import org.myeslib.data.UnitOfWorkId;
import org.myeslib.infra.InteractionContext;
import org.myeslib.infra.SnapshotReader;
import org.myeslib.infra.WriteModelJournal;
import org.myeslib.sampledomain.aggregates.inventoryitem.InventoryItem;
import org.myeslib.sampledomain.aggregates.inventoryitem.commands.DecreaseInventory;
import org.myeslib.stack1.infra.Stack1InteractionContext;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@NotThreadSafe
public class DecreaseHandler implements CommandHandler<DecreaseInventory>, StatefulCommandHandler {

    final WriteModelJournal<UUID> journal;
    final SnapshotReader<UUID, InventoryItem> snapshotReader;
    private Optional<UnitOfWork> unitOfWork = Optional.empty();

    @Inject
    public DecreaseHandler(WriteModelJournal<UUID> journal, SnapshotReader<UUID, InventoryItem> snapshotReader) {
        this.journal = journal;
        this.snapshotReader = snapshotReader;
    }

    public void handle(DecreaseInventory command) {
        final Snapshot<InventoryItem> snapshot = snapshotReader.getSnapshot(command.targetId());
        final InventoryItem aggregateRoot = snapshot.getAggregateInstance();
        final InteractionContext interactionContext = new Stack1InteractionContext(aggregateRoot);
        aggregateRoot.setInteractionContext(interactionContext);
        aggregateRoot.decrease(command.howMany());
        this.unitOfWork = Optional.of(UnitOfWork.create(UnitOfWorkId.create(), command.getCommandId(), snapshot.getVersion(), interactionContext.getEmittedEvents()));
        journal.append(command.targetId(), command, unitOfWork.get());
    }

    @Override
    public Optional<UnitOfWork> getUnitOfWork() {
        return unitOfWork;
    }
}
