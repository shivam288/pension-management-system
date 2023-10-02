import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ProcessPensionService {

  pension_details_URL = "http://localhost:9090"
  constructor(private http: HttpClient) { }
  PensionDetails(PensionerDetail: any) {
    var final_url = this.pension_details_URL + "/ProcessPension";
    return this.http.post(final_url, PensionerDetail);
  }
}
