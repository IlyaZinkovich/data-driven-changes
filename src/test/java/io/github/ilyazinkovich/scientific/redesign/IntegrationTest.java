package io.github.ilyazinkovich.scientific.redesign;

import static io.github.ilyazinkovich.scientific.redesign.incentives.Performance.HIGH;
import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.POST;

import com.google.gson.Gson;
import io.github.ilyazinkovich.scientific.redesign.incentives.Performance;
import io.github.ilyazinkovich.scientific.redesign.incentives.UpdatePerformance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class IntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  private Gson gson = new Gson();

  @Test
  void test() {
    final long courierId = 1L;

    final UpdatePerformance updatePerformance = new UpdatePerformance(HIGH);

    final HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    final HttpEntity<String> httpEntity = new HttpEntity<>(gson.toJson(updatePerformance), headers);
    restTemplate.exchange(
        "http://localhost:{port}/incentives/performance/{courierId}",
        POST, httpEntity, String.class, port, courierId);
    final ResponseEntity<Performance> response =
        restTemplate.getForEntity("http://localhost:{port}/incentives/performance/{courierId}",
            Performance.class, port, courierId);

    assertEquals(response.getStatusCode(), HttpStatus.OK);
  }
}
