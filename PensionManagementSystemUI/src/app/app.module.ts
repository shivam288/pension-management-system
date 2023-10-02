import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthGuard } from 'src/services/auth.guard';
import { AuthInterceptor } from 'src/services/auth.interceptor';
import { AuthorizationService } from 'src/services/authorization.service';
import { PensionDisbursementService } from 'src/services/pension-disbursement.service';
import { PensionerDetailService } from 'src/services/pensioner-detail.service';
import { ProcessPensionService } from 'src/services/process-pension.service';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthorizationComponent } from './authorization/authorization.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PensionerDetailComponent } from './pensioner-detail/pensioner-detail.component';
import { ProcessPensionComponent } from './process-pension/process-pension.component';



@NgModule({
  declarations: [
    AppComponent,
    AuthorizationComponent,
    DashboardComponent,
    ProcessPensionComponent,
    PensionerDetailComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatCardModule,
    HttpClientModule,
  ],
  providers: [AuthGuard, AuthorizationService, PensionDisbursementService, ProcessPensionService, PensionerDetailService, [{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }]],
  bootstrap: [AppComponent]
})
export class AppModule { }
