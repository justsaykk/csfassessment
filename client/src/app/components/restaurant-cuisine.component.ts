import { Location } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { RestaurantList } from '../models';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-restaurant-cuisine',
  templateUrl: './restaurant-cuisine.component.html',
  styleUrls: ['./restaurant-cuisine.component.css']
})
export class RestaurantCuisineComponent implements OnInit, OnDestroy{
	
	// TODO Task 3
	// For View 2

  listOfRestaurants!: RestaurantList
  listOfRestaurants$!: Subscription

  constructor(private svc: RestaurantService, 
    private _location: Location, 
    private router: Router) {}

  ngOnInit(): void {
    this.listOfRestaurants$ = this.svc.getRestaurantsByCuisine$().subscribe(
      (data) => {this.listOfRestaurants = data}
    )
  }

  ngOnDestroy(): void {
      this.listOfRestaurants$.unsubscribe();
  }

  onClick(id: string) {
    this.svc.getRestaurant(id)
    .then(
      (value) => {
        this.svc.setRestaurant(value);
      }
    )
    this.router.navigateByUrl("/view3") 
  }

  goBack():void {
    this._location.back();
  }

}
