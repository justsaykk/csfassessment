// Do not change these interfaces
export interface Restaurant {
	restaurantId: string
	name: string
	cuisine: string
	address: string
	coordinates: number[]
}

export interface Comment {
	name: string
	rating: number
	restaurantId: string
	text: string
}

export interface CuisineList {
	result: string[]
}

export interface RestaurantList {
	cuisine: string | null
	result: [{
		name: string,
		id: string
	}] | null
}

export interface RestaurantView3 {
	id: string | null
	name: string | null
	cuisine: string | null
	address: string | null
	map: string | null
}