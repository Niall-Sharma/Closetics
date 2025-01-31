package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;

@RestController
class WelcomeController {
    int[] points = new int[] {5,2,3,4,1,2,6};
    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309 Niall";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        if(!name.equals("points")){
            return name + " Does Not Exist: 404";
        }
        return Arrays.toString(points);
    }
}
