import { Component, OnInit } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { PensionerDetailService } from 'src/services/pensioner-detail.service';
import { PensionDisbursementService } from 'src/services/pension-disbursement.service';
import { Router } from '@angular/router';
import { Pensioner } from '../../models/pensioner';


@Component({
  selector: 'app-pensioner-detail',
  templateUrl: './pensioner-detail.component.html',
  styleUrls: ['./pensioner-detail.component.css']
})
export class PensionerDetailComponent implements OnInit {

  pensionDetails = {
    name: '',
    dateOfBirth: '',
    panNumber: '',
    typeOfPension: '',
    aadhaarNumber: '',
  }

  pensionerError = false;

  updatedPensionerDetails: Pensioner;
  constructor(private router: Router, private pensionerDetailService: PensionerDetailService, private pensionDisbursementService: PensionDisbursementService) { 
    this.updatedPensionerDetails = {}
  }

  ngOnInit(): void {
    //this.pensionDisbursementService.setPensionerDetails(this.updatedPensionerDetails);
  }
  onSubmit() {
    var params = new HttpParams()
      .set('name', this.pensionDetails.name.toString())
      .set('dob', this.pensionDetails.dateOfBirth)
      .set('pan', this.pensionDetails.panNumber)
      .set('aadhaar', this.pensionDetails.aadhaarNumber)
      .set('type', this.pensionDetails.typeOfPension)
    var Get_URL = params.toString();
    console.log(Get_URL)


    this.pensionerDetailService.PensionDetails(Get_URL).subscribe(
      (response: Pensioner) => {
        this.updatedPensionerDetails = response;
        console.log(response);
        if (response != null) {
          this.pensionDisbursementService.setPensionerDetails(this.updatedPensionerDetails);
          this.router.navigate(['/dashboard']);
        }else{
          this.pensionerError = true;
        }
        //window.location.href="/delete"
      },
      error => { console.log(error) }
    )
  }

}
