package vttp2022.csf.assessment.server.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.RestView2;
import vttp2022.csf.assessment.server.models.Restaurant;
// import vttp2022.csf.assessment.server.repositories.MapCache;
import vttp2022.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepo;

	// @Autowired
	// private MapCache mapCache;

	// Task 2 
	// DO NOT CHNAGE THE METHOD'S NAME
	public List<String> getCuisines() {
		// Implmementation in here
		return restaurantRepo.getCuisines().stream()
		.distinct()
		.map(s -> this.slashConverter(s))
		.collect(Collectors.toList());
		
	}

	
	// Task 3 
	// DO NOT CHNAGE THE METHOD'S NAME
	public List<RestView2> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here
		return restaurantRepo.getRestaurantsByCuisine(this.underScoreConverter(cuisine)).stream()
		.distinct()
		.collect(Collectors.toList());
	}
	
	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public Optional<Restaurant> getRestaurant(String restaurant_id) {
		return restaurantRepo.getRestaurant(restaurant_id);
	}
	
	// TODO Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public void addComment(Comment comment) {
		// Implmementation in here
		
	}
	//
	// You may add other methods to this class
	private String slashConverter(String toConvert) {
		return toConvert.replace("/", "_");
	}

	private String underScoreConverter(String toConvert) {
		return toConvert.replace("_", "/");
	}

	public void getMapFromS3() {

	}
	
	public void getMapFromApi(String lat, String lng) {
		String baseUrl = "http://map.chuklee.com";
		String url = UriComponentsBuilder.fromUriString(baseUrl)
		.queryParam("lat", lat)
		.queryParam("lng", lng)
		.toUriString();


		RestTemplate template = new RestTemplate();
		RequestEntity<Void> req = RequestEntity.get(url).accept(MediaType.IMAGE_PNG).build();
		try {
            ResponseEntity<byte[]> res = template.exchange(req, byte[].class);
            byte[] inputArr = res.getBody();

        } catch (Exception e) {
            System.err.print(e);
        }
	}

	
}
