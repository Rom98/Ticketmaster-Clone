import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpotifyDetailsComponent } from './spotify-details.component';

describe('SpotifyDetailsComponent', () => {
  let component: SpotifyDetailsComponent;
  let fixture: ComponentFixture<SpotifyDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SpotifyDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SpotifyDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
