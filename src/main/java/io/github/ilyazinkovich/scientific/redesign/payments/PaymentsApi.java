package io.github.ilyazinkovich.scientific.redesign.payments;

import static io.github.ilyazinkovich.scientific.redesign.incentives.Performance.HIGH;
import static io.github.ilyazinkovich.scientific.redesign.incentives.Performance.NORMAL;
import static io.github.ilyazinkovich.scientific.redesign.incentives.Performance.OUTSTANDING;
import static org.springframework.http.HttpStatus.ACCEPTED;

import com.google.common.collect.ImmutableMap;
import io.github.ilyazinkovich.scientific.redesign.events.Event;
import io.github.ilyazinkovich.scientific.redesign.events.EventStream;
import io.github.ilyazinkovich.scientific.redesign.incentives.Performance;
import io.github.ilyazinkovich.scientific.redesign.incentives.PerformanceDecreased;
import io.github.ilyazinkovich.scientific.redesign.incentives.PerformanceIncreased;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentsApi {

  private static final Logger log = LoggerFactory.getLogger(PaymentsApi.class);

  private final Map<Performance, Double> performanceIncentives =
      ImmutableMap.<Performance, Double>builder()
          .put(NORMAL, 0D)
          .put(HIGH, 0.10D)
          .put(OUTSTANDING, 0.20D)
          .build();
  private final Map<Long, Performance> couriersPerformance = new ConcurrentHashMap<>();

  @Autowired
  public PaymentsApi(final EventStream eventStream) {
    eventStream.subscribe(this::consumeEvent);
  }

  private void consumeEvent(final Event event) {
    if (event instanceof PerformanceIncreased) {
      final PerformanceIncreased increased = (PerformanceIncreased) event;
      couriersPerformance.put(increased.courierId, increased.performance);
    } else if (event instanceof PerformanceDecreased) {
      final PerformanceDecreased decreased = (PerformanceDecreased) event;
      couriersPerformance.put(decreased.courierId, decreased.performance);
    }
  }

  @PostMapping(path = "/payments")
  public ResponseEntity pay(@RequestBody final Pay command) {
    final Performance courierPerformance = couriersPerformance.get(command.courierId);
    final Double incentive = Optional.ofNullable(performanceIncentives.get(courierPerformance))
        .orElse(0D);
    final BigDecimal amountWithIncentive =
        BigDecimal.valueOf(1.0D + incentive).multiply(command.amount);
    log.info("Paid {} dollars to courier {}", amountWithIncentive, command.courierId);
    return new ResponseEntity(ACCEPTED);
  }
}
