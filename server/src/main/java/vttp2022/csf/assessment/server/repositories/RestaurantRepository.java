package vttp2022.csf.assessment.server.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.RestView2;
import vttp2022.csf.assessment.server.models.Restaurant;

@Repository
public class RestaurantRepository {

	@Autowired
	private MongoTemplate template;

	// Task 2
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	//  
	public List<String> getCuisines() {
		ProjectionOperation projectFields = Aggregation.project("cuisine").andExclude("_id");
		Aggregation pipeline = Aggregation.newAggregation(projectFields);
		AggregationResults<Document> results = template.aggregate(pipeline, "restaurants", Document.class);

		List<String> queryResult = new ArrayList<>();

		results.forEach(d -> {
			queryResult.add(d.getString("cuisine"));
		});

		return queryResult;
	}

	// Task 3
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	//  
	public List<RestView2> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here
		MatchOperation matchOps = Aggregation.match(Criteria.where("cuisine").is(cuisine.toString()));
		ProjectionOperation projectFields = Aggregation.project("name", "restaurant_id").andExclude("_id");
		Aggregation pipeline = Aggregation.newAggregation(matchOps,projectFields);

		AggregationResults<Document> results = template.aggregate(pipeline, "restaurants", Document.class);

		List<RestView2> queryResult = new ArrayList<>();

		results.forEach(
			doc -> {
				queryResult.add(new RestView2(doc));
			}
		);

		return queryResult;
	}

	// Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	//
	public Optional<Restaurant> getRestaurant(String restaurant_id) {
		MatchOperation match = Aggregation.match(Criteria.where("restaurant_id").is(restaurant_id.toString()));
		ProjectionOperation project = Aggregation.project(
			"restaurant_id", 
			"name", 
			"cuisine")
			.andExpression("$address.coord").as("coordinates")
			.andExpression("""
					concat(
						"$address.building",
							", ",
							"$address.street",
							", ",
							"$address.zipcode",
							", ",
							"$borough"
					)
					""")
			.as("address")
			.andExclude("_id");
		
		Aggregation pipeline = Aggregation.newAggregation(match, project);
		AggregationResults<Document> results = template.aggregate(pipeline, "restaurants", Document.class);

		Restaurant restaurant = new Restaurant();
		results.forEach(
			(r) -> {
			restaurant.setRestaurantId(r.getString("restaurant_id"));
			restaurant.setName(r.getString("name"));
			restaurant.setCuisine(r.getString("cuisine"));
			restaurant.setAddress(r.getString("address"));
			LatLng latlng = new LatLng();
			List<Double> latlngArr = r.getList("coordinates", Double.class);
			System.out.printf("Address: %s\n", restaurant.getAddress());
			
			latlng.setLatitude(latlngArr.get(0).floatValue());
			latlng.setLongitude(latlngArr.get(1).floatValue());
			restaurant.setCoordinates(latlng);
			}
		);
		return Optional.of(restaurant);
	}

	// TODO Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	//  
	public void addComment(Comment comment) {
		// Implmementation in here
		template.insert(comment, "comments");
	}
		
	// You may add other methods to this class
}
