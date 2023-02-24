import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-cuisine-list',
  templateUrl: './cuisine-list.component.html',
  styleUrls: ['./cuisine-list.component.css']
})
export class CuisineListComponent implements OnInit, OnDestroy {

  listOfCuisines!: string[]
  listOfCuisines$!: Subscription

	// TODO Task 2
	// For View 1
  constructor(private svc: RestaurantService, private router:Router) {}

  ngOnInit(): void {
    this.svc.getCuisineList();
    this.listOfCuisines$ = this.svc.getCuisineList$().subscribe(
      (data) => this.listOfCuisines = data
    )
  }

  ngOnDestroy(): void {
      console.log("View 1 destroyed");
      this.listOfCuisines$.unsubscribe();
  }

  onClick(cuisine:string) {
    this.svc.getRestaurantsByCuisine(cuisine);
    this.router.navigateByUrl("/view2");
  }
}
