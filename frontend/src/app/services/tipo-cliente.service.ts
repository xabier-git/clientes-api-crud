import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TipoCliente } from '../models/cliente.model';

@Injectable({
  providedIn: 'root'
})
export class TipoClienteService {
  private apiUrl = '/api/tipos-cliente';

  constructor(private http: HttpClient) {}

  getTiposCliente(): Observable<TipoCliente[]> {
    return this.http.get<TipoCliente[]>(this.apiUrl);
  }

  getTipoClienteByCodigo(codigo: string): Observable<TipoCliente> {
    return this.http.get<TipoCliente>(`${this.apiUrl}/${codigo}`);
  }
}
