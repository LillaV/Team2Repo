import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, tap} from "rxjs";
import {User} from "../models/user";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Role} from "../models/role";

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  url:string = "http://localhost:8080/roles";

  userroleList$: BehaviorSubject<Role[]> = new BehaviorSubject<Role[]>([]);

  loadRoles():Observable<Role[]>{
    return this.http.get<Role[]>(this.url).pipe(
      tap(roles => this.userroleList$.next(roles))
    );
  }

  getRoles(): Observable<Role[]> {
    return this.userroleList$.asObservable();
  }

  constructor(private http: HttpClient) { }
}
