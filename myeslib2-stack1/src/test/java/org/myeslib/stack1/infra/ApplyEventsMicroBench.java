package org.myeslib.stack1.infra;

import org.myeslib.data.Event;
import org.myeslib.sampledomain.aggregates.inventoryitem.InventoryItem;
import org.myeslib.sampledomain.aggregates.inventoryitem.events.InventoryDecreased;
import org.myeslib.sampledomain.aggregates.inventoryitem.events.InventoryIncreased;
import org.myeslib.sampledomain.aggregates.inventoryitem.events.InventoryItemCreated;
import org.myeslib.stack1.EventBusApplyEventsFunction;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

public class ApplyEventsMicroBench {

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(ApplyEventsMicroBench.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void multiMethodEngine() {

        BiFunction<InventoryItem, List<Event>, InventoryItem> engine = new Stack1ApplyEventsFunction<>();

        InventoryItem item = new InventoryItem();

        engine.apply(item, events());

      //  assert(item.getAvailable()==0);

    }


    @Benchmark
    public void eventBusEngine() {

        BiFunction<InventoryItem, List<Event>, InventoryItem> engine = new EventBusApplyEventsFunction<>();

        InventoryItem item = new InventoryItem();

        engine.apply(item, events());

       // assert(item.getAvailable()==0);

    }

    List<Event> events() {

        List<Event> events = new ArrayList<>();

        events.add(InventoryItemCreated.create(UUID.randomUUID(), "item1"));

        for (int i=0; i<100; i++) {
            events.add(InventoryIncreased.create(1));
            events.add(InventoryDecreased.create(1));
        }

        return events;

    }


}
