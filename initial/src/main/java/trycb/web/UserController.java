package trycb.web;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import trycb.service.User;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final Bucket bucket;

    @Autowired
    public UserController(Bucket bucket) {
        this.bucket = bucket;
    }

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public Object login(@RequestParam String user, @RequestParam String password) {
        // WORKSHOP STEP 5: Creating API Endpoints
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public Object createLogin(@RequestBody String json) {
        // WORKSHOP STEP 5: Creating API Endpoints
    }

    @RequestMapping(value="/flights", method=RequestMethod.POST)
    public Object book(@RequestBody String json) {
        // WORKSHOP STEP 5: Creating API Endpoints
    }

    @RequestMapping(value="/flights", method=RequestMethod.GET)
    public Object booked(@RequestParam String username) {
        // WORKSHOP STEP 5: Creating API Endpoints
    }

}
