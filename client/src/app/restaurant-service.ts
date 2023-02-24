import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, firstValueFrom, map, Observable, take } from 'rxjs'
import { Restaurant, Comment, CuisineList, RestaurantList, RestaurantView3 } from './models'

@Injectable()
export class RestaurantService {
	
	private BASE_URL = "http://localhost:8080/api"
	private _listOfCuisines = new BehaviorSubject<string[]>([]);
	private _listOfRestaurants = new BehaviorSubject<RestaurantList>({
		cuisine: null,
		result: null,
	});
	private _restaurant = new BehaviorSubject<Restaurant>({
		restaurantId: "",
		name: "",
		cuisine: "",
		address: "",
		coordinates: []
	})

	constructor(private http: HttpClient) {}

	// TODO Task 2
	// DO NOT CHANGE THE METHOD'S NAME
	public getCuisineList() {
		let queryUrl = this.BASE_URL;
		
		firstValueFrom<any>(
			this.http.get<CuisineList>(queryUrl)
			.pipe(
				take(1),
				map(
					(res) => this._listOfCuisines.next(res.result)
				)
			)
		)

	}

	public getCuisineList$(): Observable<string[]> {
		return this._listOfCuisines;
	}

	// TODO Task 3 
	// DO NOT CHNAGE THE METHOD'S NAME
	public getRestaurantsByCuisine(cuisine: string) {
		// Implememntation in here
		let queryUrl = `${this.BASE_URL}/${cuisine}/restaurants`
		let headers = new HttpHeaders()
		.set("Accept", "application/json")

		firstValueFrom<any>(
			this.http.get<RestaurantList>(queryUrl, {headers})
			.pipe(
				take(1),
				map(
					res => this._listOfRestaurants.next(res)
				)
			)
		)

	}

	public getRestaurantsByCuisine$(): Observable<RestaurantList> {
		return this._listOfRestaurants;
	}
	
	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public getRestaurant(restaurant: string): Promise<Restaurant> {
		let queryUrl = `${this.BASE_URL}/${restaurant}`
	
		return firstValueFrom<any>(
			this.http.get<Restaurant>(
				queryUrl
			)
		)
	}

	public setRestaurant(restaurant: Restaurant): void {
		this._restaurant.next(restaurant);
	}

	public getRestaurant$(): Observable<Restaurant> {
		return this._restaurant;
	}



	// TODO Task 5
	// Use this method to submit a comment
	// DO NOT CHANGE THE METHOD'S NAME OR SIGNATURE
	// public postComment(comment: Comment): Promise<any> {
	// 	// Implememntation in here
	// }
}
