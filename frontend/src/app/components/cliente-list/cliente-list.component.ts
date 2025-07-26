import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Cliente } from '../../models/cliente.model';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="card">
      <h2>Lista de Clientes</h2>
      
      <div *ngIf="loading" class="loading">
        Cargando clientes...
      </div>
      
      <div *ngIf="error" class="alert alert-danger">
        {{ error }}
      </div>
      
      <div *ngIf="!loading && !error">
        <table class="table">
          <thead>
            <tr>
              <th>RUT</th>
              <th>Nombre</th>
              <th>Apellido</th>
              <th>Email</th>
              <th>Tipo Cliente</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let cliente of clientes">
              <td>{{ cliente.rut }}</td>
              <td>{{ cliente.nombre }}</td>
              <td>{{ cliente.apellido }}</td>
              <td>{{ cliente.email }}</td>
              <td>{{ cliente.tipoCliente?.descripcion || cliente.codTipoCliente }}</td>
              <td>
                <a [routerLink]="['/clientes/detalle', cliente.id]" class="btn btn-primary mr-2">Ver</a>
                <a [routerLink]="['/clientes/editar', cliente.id]" class="btn btn-secondary mr-2">Editar</a>
                <button (click)="eliminarCliente(cliente)" class="btn btn-danger">Eliminar</button>
              </td>
            </tr>
          </tbody>
        </table>
        
        <div *ngIf="clientes.length === 0" class="text-center">
          <p>No hay clientes registrados.</p>
          <a routerLink="/clientes/nuevo" class="btn btn-success">Crear Primer Cliente</a>
        </div>
      </div>
    </div>
  `
})
export class ClienteListComponent implements OnInit {
  clientes: Cliente[] = [];
  loading = false;
  error: string | null = null;

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.loading = true;
    this.error = null;
    
    this.clienteService.getClientes().subscribe({
      next: (clientes) => {
        this.clientes = clientes;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar los clientes';
        this.loading = false;
        console.error('Error:', err);
      }
    });
  }

  eliminarCliente(cliente: Cliente): void {
    if (confirm(`¿Está seguro de eliminar al cliente ${cliente.nombre} ${cliente.apellido}?`)) {
      this.clienteService.deleteCliente(cliente.id!).subscribe({
        next: () => {
          this.cargarClientes(); // Recargar la lista
        },
        error: (err) => {
          alert('Error al eliminar el cliente');
          console.error('Error:', err);
        }
      });
    }
  }
}
