import { TestBed } from '@angular/core/testing';

import { SendformService } from './sendform.service';

describe('SendformService', () => {
  let service: SendformService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SendformService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
