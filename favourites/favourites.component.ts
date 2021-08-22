import { Component, OnInit, Input } from '@angular/core';
import { SendformService } from '../sendform.service';
import { trigger, transition, style, animate, state } from '@angular/animations';


@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css'],
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
export class FavouritesComponent implements OnInit {
  favdata: any = [];

  event_data: any;
  venue_data: any;
  spotify_data: string[];
  spotifyloaded: boolean;
  @Input()
  table_data: any;
  showToolTip: boolean = false;
  slideStart: boolean = false;
  hideTable: boolean = false;


  signal: boolean = false;

  constructor(private sendf: SendformService) {
    this.spotify_data = new Array();
    this.spotifyloaded = false;
  }

  favourites: any
  ngOnInit(): void {
    this.favourites = { ...localStorage };
    this.getfav();
    console.log(this.favdata);
    if (this.favdata.length == 0)
      this.signal = true;

  }
  getfav() {
    var j = 0;
    for (let i in this.favourites) {
      let sd = JSON.parse(this.favourites[i]);
      this.favdata[j] = sd;
      j++;
    }
  }

  Slide_Left(id: string, event: string, venue: string) {

    this.slideStart = true;
    this.hideTable = true;
    console.log("Favourites " + id, event, venue)

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

      })

    this.sendf.get_venue_data(venue)
      .subscribe((res: any) => {
        this.venue_data = "";
        this.venue_data = res;
        console.log(this.venue_data);
      })
  }

  delete_item(id: string) {

    for (var i = 0; i < this.favdata.length; i++) {
      if (this.favdata[i].id == id) {
        this.favdata.splice(i, 1)
        localStorage.removeItem(id);
      }
    }

    if (this.favdata.length == 0)
      this.signal = true;

  }

  slideChangedHandler(value: boolean) {
    if (this.slideStart)
      this.slideStart = false
    else
      this.slideStart = true
    this.hideTable = false;
    console.log("Value in fav compoennet: " + this.slideStart)

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




