import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
// import { FormsModule } from '@angular/forms';                 // For working of ngSubmit etc
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RoundprogressModule } from 'angular-svg-round-progressbar';
import { AgmCoreModule } from '@agm/core';
import { EventsTableComponent } from './events-table/events-table.component';
import { EventDetailsComponent } from './event-details/event-details.component';
import { SpotifyDetailsComponent } from './spotify-details/spotify-details.component';
import { VenueDetailsComponent } from './venue-details/venue-details.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { MatSelectModule } from '@angular/material/select';
import {MatTooltipModule} from '@angular/material/tooltip'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FavouritesComponent } from './favourites/favourites.component';
import { ImageModalComponent } from './image-modal/image-modal.component';




@NgModule({
  declarations: [
    AppComponent,
    EventsTableComponent,
    EventDetailsComponent,
    SpotifyDetailsComponent,
    VenueDetailsComponent,
    FavouritesComponent,
    ImageModalComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    RoundprogressModule,
    AgmCoreModule.forRoot({
      apiKey:"AIzaSyCd5XFiIA-FI2wo7kgEyOAge9Pb8Cymk3s"
    }),
    NgbModule,
    MatTooltipModule,
    BrowserAnimationsModule,
    MatAutocompleteModule,
    MatSelectModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
