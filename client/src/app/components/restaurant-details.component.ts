import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Restaurant } from '../models';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-restaurant-details',
  templateUrl: './restaurant-details.component.html',
  styleUrls: ['./restaurant-details.component.css']
})
export class RestaurantDetailsComponent implements OnInit, OnDestroy {
	
	// TODO Task 4 and Task 5
	// For View 3

  restaurant!: Restaurant
  restaurant$!: Subscription

  constructor(private svc: RestaurantService) {}

  ngOnInit(): void {
      this.restaurant$ = this.svc.getRestaurant$().subscribe(
        (data) => {this.restaurant = data}
      )
  }

  ngOnDestroy(): void {
      this.restaurant$.unsubscribe();
  }
}
