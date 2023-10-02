import { Component, OnInit } from '@angular/core';
import { PensionDisbursementService } from 'src/services/pension-disbursement.service';



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  pensionerDetails: any;
  bankName = '';
  //bankServiceCharge :any
  ProcessPensionInput = {
    aadhaarNumber: '',
    pensionAmount: '',
    bankServiceCharge: ''
  }
  //ProcessCode : any
  constructor(private pension_disbursement: PensionDisbursementService) {
  }

  ngOnInit(): void {
    this.pensionerDetails = this.pension_disbursement.getPensionerDetails();
    this.ProcessPensionInput.aadhaarNumber = this.pensionerDetails.aadhaarNumber;
    this.ProcessPensionInput.pensionAmount = this.pensionerDetails.pensionAmount;
    this.ProcessPensionInput.bankServiceCharge = this.pensionerDetails.bankServiceCharge;
    this.bankName = this.pensionerDetails.bankName;
  }

}
