package io.github.ilyazinkovich.scientific.redesign.events;

import java.util.List;
import java.util.function.Consumer;

public class EventStream {

  private final List<Event> events;
  private final List<Consumer<Event>> consumers;

  public EventStream(final List<Event> events, final List<Consumer<Event>> consumers) {
    this.events = events;
    this.consumers = consumers;
  }

  public void append(final Event event) {
    consumers.forEach(c -> c.accept(event));
    events.add(event);
  }

  public void subscribe(final Consumer<Event> consumer) {
    events.forEach(consumer);
    consumers.add(consumer);
  }
}
