import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'Error desconocido';
        
        if (error.error instanceof ErrorEvent) {
          // Error del lado del cliente
          errorMessage = `Error: ${error.error.message}`;
        } else {
          // Error del lado del servidor
          if (error.status === 0) {
            errorMessage = 'No se puede conectar con el servidor. Verifica que la API esté ejecutándose en http://localhost:8080';
          } else {
            errorMessage = `Error ${error.status}: ${error.message}`;
          }
        }
        
        console.error('Error HTTP:', errorMessage);
        return throwError(() => new Error(errorMessage));
      })
    );
  }
}
