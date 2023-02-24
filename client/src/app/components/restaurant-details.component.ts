import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Restaurant } from '../models';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-restaurant-details',
  templateUrl: './restaurant-details.component.html',
  styleUrls: ['./restaurant-details.component.css']
})


/* 
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
*/
export class RestaurantDetailsComponent implements OnInit, OnDestroy {
	
	// TODO Task 4 and Task 5
	// For View 3

  restaurant!: Restaurant
  restaurant$!: Subscription
  commentForm!: FormGroup

  constructor(private svc: RestaurantService, private fb: FormBuilder) {}

  ngOnInit(): void {
      this.restaurant$ = this.svc.getRestaurant$().subscribe(
        (data) => {this.restaurant = data}
      )

      this.commentForm = this.fb.group({
        name: this.fb.control('', [Validators.required]),
        rating: this.fb.control('', [Validators.required]),
        text: this.fb.control('', [Validators.required])
      })
  }

  ngOnDestroy(): void {
      this.restaurant$.unsubscribe();
  }

  onSubmit() {
    let formData = this.commentForm.value
    let id = this.restaurant.restaurantId
    // this.svc.postComment()
  }
}
