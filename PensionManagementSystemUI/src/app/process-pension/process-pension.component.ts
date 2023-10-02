import { Component, OnInit } from '@angular/core';
import { PensionDisbursementService } from 'src/services/pension-disbursement.service';
import { ProcessPensionService } from 'src/services/process-pension.service';
import { PensionerDetailService } from 'src/services/pensioner-detail.service';


@Component({
  selector: 'app-process-pension',
  templateUrl: './process-pension.component.html',
  styleUrls: ['./process-pension.component.css']
})

export class ProcessPensionComponent implements OnInit {
  pensionerDetails: any;
  sucess_code: any;
  bankName = '';
  aadhaarNumber = '';
  pensionAmount = '';
  bankServiceCharge = '';
  typeOfPension = '';
  names = '';

  constructor(private ProcessPension: ProcessPensionService, private pension_disbursement: PensionDisbursementService) { }

  ngOnInit(): void {
    this.pensionerDetails = this.pension_disbursement.getPensionerDetails();
    this.names = this.pensionerDetails.name;
    this.typeOfPension = this.pensionerDetails.typeOfPension;
    this.aadhaarNumber = this.pensionerDetails.aadhaarNumber;
    this.pensionAmount = this.pensionerDetails.pensionAmount;
    this.bankServiceCharge = this.pensionerDetails.bankServiceCharge;
    this.bankName = this.pensionerDetails.bankName;
    this.ProcessPension.PensionDetails(this.pensionerDetails).subscribe(
      response => {
        this.sucess_code = response;
        console.log(response);
      },
      error => {
        console.log(error);
      }
    )
  }
}
