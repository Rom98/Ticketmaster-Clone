import { Component } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { SendformService } from './sendform.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, startWith, filter, switchMap } from 'rxjs/operators';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],

})

export class AppComponent {
  title = 'collect-info';

  private info: any;
  private loc: string;
  table_data: any;
  keyw: string;
  fav_active: boolean = false;
  table_active: boolean = false;
  isFieldDisabled: boolean = true;
  isDataFetched: boolean = false;
  displayProgressbar: boolean = false;
  signal:boolean=false;
  keySubmit:boolean=false;
  formdata: FormGroup;
  field_disabled:boolean=true;
  take_loc:boolean=false;
  bool:boolean=true;

  filteredOptions: Observable<any> | any;

  constructor(private formBuilder: FormBuilder, private sendf: SendformService, private http: HttpClient) {
    this.formdata = formBuilder.group({
      Key: ["",[Validators.required]],
      Cat: ["Default"],
      Dist: [""],
      Met: ["miles"],
      typed_loc: [{ value: "", disabled: this.isFieldDisabled },[Validators.required]],
    });
    this.loc = "";
    this.table_data = "";
    this.keyw = "";
  }




   ngOnInit() {
        this.loc = "";
    this.table_data = "";
    this.keyw = "";
    this.formdata = this.formBuilder.group({
      Key: ["",[Validators.required,this.check_whitespaces]],
      Cat: ["Default"],
      Dist: [""],
      Met: ["miles"],
      typed_loc: [{ value: "", disabled: this.isFieldDisabled },[Validators.required]],
    });
    console.log(this.formdata.value);                                    //get current user location
    console.log("Function initialized");
    this.sendf.get_currLoc("https://ipinfo.io/json?token=aeeda3eba907a1")
      .subscribe(
        data => {
          this.info = data;
          this.loc = this.info.loc;
        }
      );
    if(this.formdata.value.Key!=null){
    this.filteredOptions = this.formdata.controls.Key.valueChanges
      .pipe(
        startWith(''),
        switchMap(value => this._filter(value))
      );
      }
  }

  private _filter(value: string) {
    console.log("Keyword: " + value)
    const filterValue = value.toLowerCase();

    let link = '/suggestion_data/?keyword=' + value;

    return this.http.get(link)
      .pipe(
        filter(data => !!data)
        , map((data) => {
          return data
        })
      )

  }

  disableField(param: boolean) {
    if (param){
      this.formdata.get('typed_loc')!.disable();
      this.formdata.value.typed_loc== "";
    }
    else{
      this.formdata.get('typed_loc')!.enable()
      this.field_disabled = false;
    }
  }
  checkLoc() {                 //function to enable search button if curr location is found.
    if (this.loc.length != 0)
      return false;
    else
      return true;
  }

  check_whitespaces(control:AbstractControl){
      var key = control.value;
      if (key.length>0){
      if (key.trim().length==0) {
        return {'space':true};
      }
      else
        return null;
  }
  return null;
}


  onSubmit() {
    this.keySubmit = true;
    //this.bool = this.check_whitespaces();
    if(this.field_disabled ==false && this.formdata.value.typed_loc== ""){
      this.take_loc = true;
    }
    else{

    if(this.formdata.value.Key!=""&& this.formdata.controls.Key.invalid==false){
    this.isDataFetched = false;
    this.displayProgressbar = true

    console.log("Submit Data: " + this.formdata.value);
    this.fav_active = false;
    this.table_active = true;

    this.sendf.sendData(this.formdata.value, this.loc)
      .subscribe((res: any) => {
        this.table_data = res;
        console.log("Data of main page "+this.table_data.results)
        if(this.table_data.results == undefined)
          this.signal = true;
        else
          this.signal = false;
        console.log("error on main page "+this.signal)
        this.isDataFetched = true;
        this.displayProgressbar = false;
        console.log(this.table_data);
      })

    }
  }

  }


 clearData() {
    this.ngOnInit();
    this.keySubmit=false;
    this.take_loc=false;
    this.disableField(true);
    console.log(this.formdata.value);

    this.fav_active = false;
    this.table_active = false;
    this.isFieldDisabled = true;
    this.isDataFetched = false;
    this.displayProgressbar = false;
    this.signal = false;
  }

}
