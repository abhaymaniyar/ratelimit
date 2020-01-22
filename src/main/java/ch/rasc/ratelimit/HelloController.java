package ch.rasc.ratelimit;



import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;

@RestController
public class HelloController {

  private final Bucket bucket;

  public HelloController() {
    long capacity = 10;
    Refill refill = Refill.greedy(10, Duration.ofMinutes(1));
    Bandwidth limit = Bandwidth.classic(capacity, refill);
    this.bucket = Bucket4j.builder().addLimit(limit).build();
  }

  @GetMapping("/hello")
  public String getHello() {
    return "Hello";
  }
}
