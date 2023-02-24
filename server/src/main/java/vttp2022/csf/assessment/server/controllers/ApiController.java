package vttp2022.csf.assessment.server.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.RestView2;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.services.CommentService;
import vttp2022.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*")
public class ApiController {

    @Autowired
    private RestaurantService rSvc;

    @Autowired
    private CommentService commentService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCuisines() {
        List<String> listOfCuisines = rSvc.getCuisines();
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for (String cuisine : listOfCuisines) {
            jab.add(cuisine);
        }

        String response = Json.createObjectBuilder()
        .add("result", jab)
        .build()
        .toString();

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{cuisine}/restaurants",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRestaurantsByCuisine(
        @PathVariable(name = "cuisine") String cuisine
    ) {
        String titleCaseCuisine = cuisine.substring(0,1).toUpperCase() + cuisine.substring(1);
        List<RestView2> listOfRestaurants = rSvc.getRestaurantsByCuisine(titleCaseCuisine);
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for (RestView2 restaurant : listOfRestaurants) {
            jab.add(restaurant.toJson());
        }

        String response = Json.createObjectBuilder()
        .add("cuisine", titleCaseCuisine)
        .add("result", jab)
        .build()
        .toString();

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRestaurant(
        @PathVariable(name = "id") String restaurant_id
    ) {
        // Get restaurant object
        Optional<Restaurant> restaurantOptional = rSvc.getRestaurant(restaurant_id);
        Restaurant restaurant = restaurantOptional.get();

        // Get Coordinates
        Float lat = restaurant.getCoordinates().getLatitude();
        Float lng = restaurant.getCoordinates().getLongitude();

        // Get map from Api
        // Maybe Later


        JsonObject restaurantObj = Json.createObjectBuilder()
        .add("name", restaurant.getName())
        .add("cuisine", restaurant.getCuisine())
        .add("address", restaurant.getAddress())
        .add("mapUrl", restaurant.getMapURL())
        .build();

        System.out.println(restaurantObj.toString());

        return null;
    }

    @PostMapping(path = "/comments")
    public ResponseEntity<String> postComments(
        @RequestBody Comment comment
    ) {
        if (commentService.saveComment(comment)) {
            JsonObject response = Json.createObjectBuilder()
            .add("message", "Comment posted")
            .build();
            return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
        } else {
            JsonObject response = Json.createObjectBuilder()
            .add("message", "Unsuccessful post")
            .build();
            return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
        }
   }
}
