import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
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

	private _restaurantView3 = new BehaviorSubject<RestaurantView3>({
		id: "",
		name: "",
		cuisine: "",
		address: "",
		map: "",
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
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public getRestaurant(restaurant: string): Promise<Restaurant> {
		let queryUrl = `${this.BASE_URL}/${restaurant}`
		
				/* 
				*	server response:
				* {
					restaurant_id: string
					name: string
					cuisine: string
					address: string
					map: string
					coordinates: string[]
				* }
				*/
		return firstValueFrom<any>(
			this.http.get<any>(
				queryUrl
			).pipe(
				take(1),
				map(r => {
					// Manipulate coordinates
					let coordinates: number[] = [];
					r.coordinates.map((el:string) => {coordinates.push(Number.parseInt(el))})

					let view3Data: RestaurantView3 = {
						id: r.restaurant_id,
						name: r.name,
						cuisine: r.cuisine,
						address: r.address,
						map: r.map
					} as RestaurantView3
					this._restaurantView3.next(view3Data);

					return {
						restaurantId: r.restaurant_id,
						name: r.name,
						cuisine: r.cuisine,
						address: r.address,
						coordinates: coordinates
					} as Restaurant
				})
			)
		)
	}

	public setRestaurant(restaurant: Restaurant): void {
		this._restaurant.next(restaurant);
	}

	public getRestaurantView3$(): Observable<RestaurantView3> {
		return this._restaurantView3;
	}

	public getRestaurant$(): Observable<Restaurant> {
		return this._restaurant;
	}



	// TODO Task 5
	// Use this method to submit a comment
	// DO NOT CHANGE THE METHOD'S NAME OR SIGNATURE
	public postComment(comment: Comment): Promise<any> {
		let queryUrl = `${this.BASE_URL}/comments`
		let headers = new HttpHeaders()
		.set("Content-Type", "application/json")
		.set("Accept", "appliction/json")

		return firstValueFrom<any>(
			this.http.post<any>(queryUrl, comment, {headers})
		).then(
			() => {console.log("Posted!")}
		)
	}
}
