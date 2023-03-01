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
import org.springframework.web.util.UriComponentsBuilder;

import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.RestView2;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.repositories.MapCache;
import vttp2022.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepo;

	@Autowired
	private S3Service S3Svc;

	@Autowired
	private MapCache mapCache;

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
	
	// Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public Optional<Restaurant> getRestaurant(String restaurant_id) {
		Optional<Restaurant> resOpt = restaurantRepo.getRestaurant(restaurant_id);
		if (resOpt.isEmpty()) {
			return Optional.empty();
		}

		Restaurant restaurant = resOpt.get();
		// Get Coordinates
		String lat = Float.toString(restaurant.getCoordinates().getLatitude());
		String lng = Float.toString(restaurant.getCoordinates().getLongitude());

		// Get map from S3
		String mapUrl = this.getMapUrlFromS3(restaurant_id, lat, lng);
		restaurant.setMapURL(mapUrl);
		return Optional.of(restaurant);
	}
	
	// Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public void addComment(Comment comment) {
		restaurantRepo.addComment(comment);
	}

	private String slashConverter(String toConvert) {
		return toConvert.replace("/", "_");
	}

	private String underScoreConverter(String toConvert) {
		return toConvert.replace("_", "/");
	}


	private String getMapUrlFromS3(String id, String lat, String lng) {
		Optional<String> url = mapCache.getMap(id);
		if (url.isEmpty()) {
			System.out.println("Map not found in S3");
			this.getMapFromApi(id, lat, lng);
			return mapCache.getMap(id).get();
		}
		return url.get();
	}
	
	private void getMapFromApi(String id, String lat, String lng) {
		String baseUrl = "http://map.chuklee.com";
		String url = UriComponentsBuilder.fromUriString(baseUrl)
		.queryParam("lat", lat)
		.queryParam("lng", lng)
		.toUriString();


		RestTemplate template = new RestTemplate();
		System.out.println("Calling Chuk's API");
		RequestEntity<Void> req = RequestEntity.get(url).accept(MediaType.IMAGE_PNG).build();
		try {
            ResponseEntity<byte[]> res = template.exchange(req, byte[].class);
            byte[] inputArr = res.getBody();
			S3Svc.upload(inputArr, id);
        } catch (Exception e) {
            System.err.print(e);
        }
	}


}
