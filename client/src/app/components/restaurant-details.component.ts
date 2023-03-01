import { Location } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Comment, Restaurant, RestaurantView3 } from '../models';
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

  restaurantView3!: RestaurantView3
  restaurantView3$!: Subscription
  commentForm!: FormGroup

  constructor(private svc: RestaurantService, private fb: FormBuilder, private _location: Location) {}

  ngOnInit(): void {
      this.restaurantView3$ = this.svc.getRestaurantView3$().subscribe(
        (data: RestaurantView3) => {
          this.restaurantView3 = data
        }
      )

      this.commentForm = this.fb.group({
        name: this.fb.control('', [Validators.required]),
        rating: this.fb.control('', [Validators.required]),
        text: this.fb.control('', [Validators.required])
      })
  }

  ngOnDestroy(): void {
      this.restaurantView3$.unsubscribe();
  }

  onSubmit() {
    this.svc.postComment({
      name: this.commentForm.value.name,
      rating: this.commentForm.value.rating,
      restaurantId: this.restaurantView3?.id,
      text: this.commentForm.value.text
    } as Comment
    )
  }
  
  goBack() {
    this._location.back();
  }
}
