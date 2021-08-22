import { AgmCoreModule } from '@agm/core';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-venue-details',
  templateUrl: './venue-details.component.html',
  styleUrls: ['./venue-details.component.css'],
})

export class VenueDetailsComponent implements OnInit {

  @Input() venue_data:any = {};

  lat :any
  lon : any
  signal:boolean=false;
  // gotvenue:boolean=true;

  constructor() { }

  ngOnInit(): void {
    // if(this.venue_data.results.length==0){
    //   this.gotvenue=false;
    // }
  //   this.lat = parseFloat(this.venue_data.results[0].latitude);
  //   this.lon = parseFloat(this.venue_data.results[0].longitude);
  //   console.log("LATITUDE AND LONGITUDE",this.lat, this.lon)
  // }
  }


}
