import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { NgModule } from '@angular/core';
import { AgmCoreModule } from '@agm/core';
import { SendformService } from '../sendform.service';
import { HttpClient } from '@angular/common/http';
import { trigger, transition, style, animate, state } from '@angular/animations';
import { TooltipPosition } from '@angular/material/tooltip';




@Component({
  selector: 'app-events-table',
  templateUrl: './events-table.component.html',
  styleUrls: ['./events-table.component.css'],
  animations: [
    trigger('slide1', [
      transition(':enter', [
        style({ transform: 'translateX(100%)' }),
        animate('500ms ease-in', style({ transform: 'translateX(0)' }))
      ]),
      transition(':leave', [
        animate('500ms ease-out', style({ transform: 'translateX(100%)' }))
      ])
    ]),
  ]
})
export class EventsTableComponent implements OnChanges {


  event_data: any;
  spotify_data: string[];
  venue_data: any;
  spotifyloaded: boolean;
  @Input()
  table_data: any;

  date: string;
  event: string;
  id: string;
  genre: string;
  venue: string;
  artist: string;
  slideStart: boolean = false;
  hideTable: boolean = false;
  isAnimationDisabled: boolean = true;
  showToolTip: boolean = false;
  previousID: string;
  previousArtist: string;
  previousVenue: string;

  state1 = "closed";
  state2 = "closed";

  event_details_display: boolean;
  localStorageService: any;
  constructor(private sendf: SendformService, private http: HttpClient) {
    this.event_details_display = true;
    this.spotifyloaded = false;
    this.spotify_data = new Array();
    this.date = "";
    this.event = "";
    this.id = "";
    this.genre = "";
    this.venue = "";
    this.artist = "";
    this.previousArtist = "";
    this.previousID = "";
    this.previousVenue = "";
  }

  ngOnChanges(): void {
    this.spotify_data = new Array();
    this.isAnimationDisabled = true;

  }

  add_event(date: string, event: string, id: string, genre: string, venue: string, artist: string) {
    this.date = date;
    this.event = event;
    this.id = id;
    this.genre = genre;
    this.venue = venue;
    this.artist = artist;

    if (localStorage.getItem(this.id) != undefined) {
      //data = localStorage.getItem(this.id);
      localStorage.removeItem(this.id);
    }
    else
      localStorage.setItem(this.id, JSON.stringify({ "date": this.date, "artist": this.artist, "event": this.event, "id": this.id, "genre": this.genre, "venue": this.venue }))
  }

  checkId(id: string) {
    if (localStorage.getItem(id) != undefined)
      return true;
    else
      return false;
  }

  Slide_Left(id: string, event: string, venue: string)         // get Event Details
  {
    this.previousArtist = event;
    this.previousID = id;
    this.previousVenue = venue;
    this.isAnimationDisabled = false;
    this.hideTable = true;
    console.log(id);
    this.slideStart = true;
    //this.event_details_display = false;
    this.sendf.get_event_id(id)
      .subscribe((res: any) => {
        this.event_data = "";
        this.event_data = res;
        console.log(this.event_data);


        var arr = []
        arr = this.event_data.results[0].Artist.split(" | ")
        this.spotify_data = new Array();
        for (let artist of arr) {
          this.sendf.get_spotify_data(artist)
            .subscribe((res: any) => {
              if (res.results.length != 0)
                this.spotify_data.push(res.results[0]);
              // this.spotify_data.sort((a:any, b:any) =>  {
              //   return (a["Name"] - b["Name"]);
              // })
            })
          this.spotifyloaded = true;
        }

      });


    this.sendf.get_venue_data(venue)
      .subscribe((res: any) => {
        this.venue_data = "";
        this.venue_data = res;
        console.log(this.venue_data);
      });


    (this.state1 == "closed") ? this.state1 = "open" : this.state1 = "closed";
    (this.state2 == "closed") ? this.state2 = "open" : this.state2 = "closed";

  }

  slideChangedHandler(value: boolean) {
    if (this.slideStart)
      this.slideStart = false
    else
      this.slideStart = true
    this.hideTable = false;
  }

  getEventName(name: string) {
    var index = 0;
    if (name.length > 35) {
      if (name[34] != ' ') {
        for (var i = 35; i > 0; i--) {
          if (name[i] == ' ') {
            index = i;
            break;
          }
        }
      }
      if (index == 0) {
        index = 35;
      }
      this.showToolTip = true;
      return name.slice(0, index) + "..."
    }
    else
      return name
  }

}
