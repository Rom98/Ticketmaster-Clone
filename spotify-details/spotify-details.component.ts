import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-spotify-details',
  templateUrl: './spotify-details.component.html',
  styleUrls: ['./spotify-details.component.css']
})
export class SpotifyDetailsComponent implements OnInit {

  @Input() table_data:any = {};
  @Input() spotify_data:any = {};

  signal:boolean=false;

  constructor() { }

  ngOnInit(): void {
    console.log(this.spotify_data)
  }

}
