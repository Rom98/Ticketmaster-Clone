import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-image-modal',
  templateUrl: './image-modal.component.html',
  styleUrls: ['./image-modal.component.css']
})

export class ImageModalComponent implements OnInit {

  @Input() id:string;

  constructor(public modalService: NgbActiveModal) {
    this.id = "";
   }

  ngOnInit(): void {
    console.log(this.id)
  }


}
