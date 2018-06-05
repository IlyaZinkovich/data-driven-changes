package io.github.ilyazinkovich.scientific.redesign;

import io.github.ilyazinkovich.scientific.redesign.events.EventStream;
import java.util.ArrayList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

  @Bean
  EventStream eventStream() {
    return new EventStream(new ArrayList<>(), new ArrayList<>());
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
