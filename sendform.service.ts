import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HashLocationStrategy } from '@angular/common';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SendformService {

  private keyword: string;
  private category: string;
  private distance: number;
  private metric: string;
  private given_loc: string;
  private sent_loc: string;
  private link: string;
  private data: any;

  constructor(private http: HttpClient) {
    this.keyword = "";
    this.category = "";
    this.distance = 10;
    this.metric = "";
    this.given_loc = "";
    this.sent_loc = "";
    this.link = "";
  }

  get_currLoc(data: any) {
    //console.log(data);
    return this.http.get(data);
  }

  sendData(data: any, loc: string) {         // loc is users current location and given_loc is users typed location
    //console.log(data);

    this.keyword = data.Key;
    this.category = data.Cat;
    this.distance = data.Dist;
    this.metric = data.Met;
    this.given_loc = data.typed_loc;

    if (this.distance == null)
      this.distance = 10;

    if (this.given_loc != null && this.given_loc != "")
      this.sent_loc = this.given_loc;
    else
      this.sent_loc = loc;

    console.log(this.keyword, this.category, this.distance, this.metric, this.sent_loc);

    this.link = environment.node_url+'/form_data/?keyword=' + this.keyword + '&category=' + this.category + '&distance=' + this.distance + '&metric=' + this.metric + '&location=' + this.sent_loc;
    console.log(this.link);
    this.data = "";
    this.data = this.http.get(this.link);
    return this.data;

    // .subscribe ((data:any) => {
    //   this.data = "";
    //   this.data = data;
    //   return this.data;
    // });


  }

  get_event_id(id: string) {                 //Get event details after clicking on event name in table
    this.link = environment.node_url+'/event_data/?Id=' + id;

    return this.http.get(this.link)
  }

  get_spotify_data(event:string){
    this.link = environment.node_url+'/spotify_data/?Event_Name=' + event;

    return this.http.get(this.link)
  }

  get_venue_data(venue:string){
      this.link = environment.node_url+'/venue_data/?Venue_Name=' + venue.replace(/ /g,'+');

      console.log(this.link)
      return this.http.get(this.link)
  }

  get_suggestions(data:any){
    this.link = environment.node_url+'/suggestion_data/?keyword=' + data;

    console.log(this.link)
    return this.http.get(this.link)
  }
}
