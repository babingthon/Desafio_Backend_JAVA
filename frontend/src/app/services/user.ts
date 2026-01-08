import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserProfileDTO } from '../models/UserProfileDTO';
import { environment } from '../../environments/environment'; // Importe o environment

@Injectable({
  providedIn: 'root'
})
export class User {
  private http = inject(HttpClient);

  private readonly API_URL = `${environment.apiUrl}/users/me`;
  private readonly PASSWORD_URL = `${environment.apiUrl}/users/password`;

  getLoggedUserProfile(): Observable<UserProfileDTO> {
    return this.http.get<UserProfileDTO>(this.API_URL);
  }

  updatePassword(passwordData: any): Observable<void> {
    return this.http.patch<void>(this.PASSWORD_URL, passwordData);
  }
}
