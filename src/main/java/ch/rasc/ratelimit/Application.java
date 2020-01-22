package ch.rasc.ratelimit;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.github.bucket4j.grid.GridBucketState;
import io.github.bucket4j.grid.RecoveryStrategy;
import io.github.bucket4j.grid.hazelcast.Hazelcast;

@SpringBootApplication
public class Application implements WebMvcConfigurer {

  @Value("${get_limit_global}")
  private int getLimitGlobal;

  @Value("${get_limit_duration_global}")
  private int getLimitDurationGlobal;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    Refill refill = Refill.greedy(getLimitGlobal, Duration.ofMinutes(getLimitDurationGlobal));
    Bandwidth limit = Bandwidth.classic(getLimitGlobal, refill);
    Bucket bucket = Bucket4j.builder().addLimit(limit).build();
    registry.addInterceptor(new RateLimitInterceptor(bucket, 1)).addPathPatterns("/hello");
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
