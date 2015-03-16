package org.myeslib.stack1.data;

import com.esotericsoftware.kryo.Kryo;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Before;
import org.junit.Test;
import org.myeslib.data.Snapshot;
import org.myeslib.sampledomain.aggregates.inventoryitem.InventoryItem;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Stack1KryoSnapshotTest {

    Kryo kryo ;

    @Before
    public void setup() {
        kryo = new Kryo();
        kryo.register(InventoryItem.class);
    }


    @Test
    public void getAggregateInstance() throws Exception {
        final InventoryItem item = create();
        final Snapshot<InventoryItem> snapshot = new Stack1KryoSnapshot<>(item, 1L, kryo);
        assertThat(snapshot.getAggregateInstance(), is(item));

    }

    @Test
    public void getVersion() throws Exception {
        final InventoryItem item = create();
        final Snapshot<InventoryItem> snapshot = new Stack1KryoSnapshot<>(item, 1L, kryo);
        assertThat(snapshot.getVersion(), is(1L));
    }

    @Test
    public void equals() throws Exception {
        final InventoryItem item = create();
        final Snapshot<InventoryItem> snapshot = new Stack1KryoSnapshot<>(item, 1L, kryo);
        assertThat(snapshot.equals(new Stack1KryoSnapshot<>(item, 1L, kryo)), is(true));
    }

    @Test
    public void mutatingAggregateDoesNotAffectSnapshot() throws Exception {
        final InventoryItem item = create();
        final InventoryItem identicalItem = (InventoryItem) BeanUtils.cloneBean(item);
        final Snapshot<InventoryItem> snapshot = new Stack1KryoSnapshot<>(item, 0L, kryo);
        assertThat(snapshot.getAggregateInstance(), is(item));
        final InventoryItem itemFromSnapshot = snapshot.getAggregateInstance();
        itemFromSnapshot.setDescription("notAnymore");
        assertThat(snapshot.getAggregateInstance(), is(identicalItem));
    }

    InventoryItem create() {
        final InventoryItem item = new InventoryItem();
        item.setId(UUID.randomUUID());
        item.setDescription("item4test");
        item.setAvailable(10);
        return item;
    }
}