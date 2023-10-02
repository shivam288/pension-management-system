import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Pensioner } from '../models/pensioner';

@Injectable({
  providedIn: 'root'
})
export class PensionDisbursementService {

  pension_disbursement_URL = "http://localhost:9091"
  pensionerDetails: Pensioner;

  constructor(private http: HttpClient) {
    this.pensionerDetails = {}
  }
  processPension(ProcessPensionInput: any) {
    return this.http.post(`${this.pension_disbursement_URL}/DisbursePension`, ProcessPensionInput);
  }
  getBankServiceCharge(bankName: string) {
    return this.http.post(`${this.pension_disbursement_URL}/getServiceCharge`, bankName);
  }
  setPensionerDetails(pensionDetail: Pensioner) {
    this.pensionerDetails = pensionDetail;
    console.log("value set=>", this.pensionerDetails);
  }
  getPensionerDetails() {
    console.log("setted value=>", this.pensionerDetails);
    return this.pensionerDetails;
  }
}
