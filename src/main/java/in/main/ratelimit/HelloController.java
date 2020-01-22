package in.main.ratelimit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
  @GetMapping("/hello")
  public String getHello() {
    return "Hello";
  }

  @GetMapping("/world")
  public String getWorld() {
    return "World";
  }
}
