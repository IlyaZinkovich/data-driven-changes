package io.github.ilyazinkovich.scientific.redesign.incentives;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

import io.github.ilyazinkovich.scientific.redesign.events.EventStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IncentivesApi {

  private static final Logger log = LoggerFactory.getLogger(IncentivesApi.class);

  private final EventStream eventStream;
  private final Map<Long, Performance> couriersPerformance = new ConcurrentHashMap<>();

  @Autowired
  public IncentivesApi(final EventStream eventStream) {
    this.eventStream = eventStream;
  }

  @PostMapping(path = "/incentives/performance/{courierId}")
  public ResponseEntity updatePerformance(
      @PathVariable final Long courierId, @RequestBody final UpdatePerformance command) {
    couriersPerformance.put(courierId, command.performance);
    eventStream.append(new PerformanceIncreased(courierId, command.performance));
    return new ResponseEntity(ACCEPTED);
  }

  @GetMapping(path = "/incentives/performance/{courierId}")
  public ResponseEntity<Performance> getPerformance(
      @PathVariable final Long courierId) {
    final Performance courierPerformance = couriersPerformance.get(courierId);
    return new ResponseEntity<>(courierPerformance, OK);
  }
}
