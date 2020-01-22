package in.main.ratelimit;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@SpringBootApplication
public class Application implements WebMvcConfigurer {

  @Value("${api.hello.get.limit}")
  private int getHelloLimit;

  @Value("${api.hello.get.granularity}")
  private int getHelloGranularity;

  @Value("${api.world.get.limit}")
  private int getWorldLimit;

  @Value("${api.world.get.granularity}")
  private int getWorldGranularity;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(getRateLimitInterceptor(getHelloLimit, getHelloGranularity)).addPathPatterns("/hello");
    registry.addInterceptor(getRateLimitInterceptor(getWorldLimit, getWorldGranularity)).addPathPatterns("/world");
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  public RateLimitInterceptor getRateLimitInterceptor(int apiLimit, int granularity) {
    Refill refill = Refill.greedy(apiLimit, Duration.ofMinutes(granularity));
    Bandwidth limit = Bandwidth.classic(apiLimit, refill);
    Bucket bucket = Bucket4j.builder().addLimit(limit).build();
    return new RateLimitInterceptor(bucket, 1);
  }
}
