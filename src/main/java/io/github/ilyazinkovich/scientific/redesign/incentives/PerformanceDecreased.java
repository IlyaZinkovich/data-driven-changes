package io.github.ilyazinkovich.scientific.redesign.incentives;

public class PerformanceDecreased {

  public final Long courierId;
  public final Performance performance;

  public PerformanceDecreased(final Long courierId, final Performance performance) {
    this.courierId = courierId;
    this.performance = performance;
  }
}
