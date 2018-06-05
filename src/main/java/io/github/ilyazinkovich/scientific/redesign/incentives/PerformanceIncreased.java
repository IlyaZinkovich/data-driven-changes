package io.github.ilyazinkovich.scientific.redesign.incentives;

import io.github.ilyazinkovich.scientific.redesign.events.Event;

public class PerformanceIncreased implements Event {

  public final Long courierId;
  public final Performance performance;

  public PerformanceIncreased(final Long courierId, final Performance performance) {
    this.courierId = courierId;
    this.performance = performance;
  }
}
