import { TransitiveCompileNgModuleMetadata } from '@angular/compiler';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ImageModalComponent } from '../image-modal/image-modal.component';
import { trigger, transition, style, animate, state } from '@angular/animations';


@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.css'],
  animations: [
    trigger('slide2', [
      transition(':enter', [
        style({ transform: 'translateX(-100%)' }),
        animate('500ms ease-in', style({ transform: 'translateX(0%)' }))
      ]),
      transition(':leave', [
        animate('500ms ease-out', style({ transform: 'translateX(100%)' }))
      ])
    ]),
  ]
})
export class EventDetailsComponent implements OnInit {

  @Input() table_data: any = {};
  @Input() event_data: any = {};
  @Input() spotify_data: any = {};
  @Input() venue_data: any = {};
  @Input() slideDetails: boolean = false;
  @Input() showDetails: boolean = false;
  @Output() slideChange = new EventEmitter<boolean>();
  showDetailsView: boolean = false;


  date: string;
  event: string;
  id: string;
  genre: string;
  venue: string;
  artist: string;
  switch: string;

  constructor(private modalService: NgbModal) {
    this.switch = "details";
    this.date = "";
    this.event = "";
    this.id = "";
    this.genre = "";
    this.venue = "";
    this.artist = "";
  }

  ngOnInit(): void {
    this.showDetailsView = true;
    console.log("Event data "+this.event_data)
    console.log("Table data in event_details", this.table_data)
  }

  openImage(url: string) {
    const modalRef = this.modalService.open(ImageModalComponent, {
    });
    modalRef.componentInstance.id = url;
  }

  changeSlideDetails() {
    this.showDetailsView = false
    this.slideChange.emit(true);
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

}
