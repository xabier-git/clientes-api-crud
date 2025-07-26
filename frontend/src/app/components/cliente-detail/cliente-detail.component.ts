import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { Cliente } from '../../models/cliente.model';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-cliente-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="card">
      <h2>Detalle del Cliente</h2>
      
      <div *ngIf="loading" class="loading">
        Cargando...
      </div>
      
      <div *ngIf="error" class="alert alert-danger">
        {{ error }}
      </div>
      
      <div *ngIf="!loading && !error && cliente">
        <div class="cliente-info">
          <div class="form-group">
            <label><strong>RUT:</strong></label>
            <p>{{ cliente.rut }}</p>
          </div>
          
          <div class="form-group">
            <label><strong>Nombre Completo:</strong></label>
            <p>{{ cliente.nombre }} {{ cliente.apellido }}</p>
          </div>
          
          <div class="form-group">
            <label><strong>Edad:</strong></label>
            <p>{{ cliente.edad }} años</p>
          </div>
          
          <div class="form-group">
            <label><strong>Email:</strong></label>
            <p>{{ cliente.email }}</p>
          </div>
          
          <div class="form-group">
            <label><strong>Tipo de Cliente:</strong></label>
            <p>{{ cliente.tipoCliente?.descripcion || cliente.codTipoCliente }}</p>
          </div>
          
          <div class="form-group" *ngIf="cliente.telefonos && cliente.telefonos.length > 0">
            <label><strong>Teléfonos:</strong></label>
            <ul>
              <li *ngFor="let telefono of cliente.telefonos">{{ telefono }}</li>
            </ul>
          </div>
          
          <div class="form-group" *ngIf="!cliente.telefonos || cliente.telefonos.length === 0">
            <label><strong>Teléfonos:</strong></label>
            <p><em>No hay teléfonos registrados</em></p>
          </div>
        </div>
        
        <div class="acciones mt-3">
          <a [routerLink]="['/clientes/editar', cliente.id]" class="btn btn-secondary mr-2">
            Editar Cliente
          </a>
          <a routerLink="/clientes" class="btn btn-primary">
            Volver a la Lista
          </a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .cliente-info {
      background: #f8f9fa;
      padding: 20px;
      border-radius: 4px;
      margin-bottom: 20px;
    }
    
    .cliente-info .form-group {
      margin-bottom: 15px;
      border-bottom: 1px solid #dee2e6;
      padding-bottom: 10px;
    }
    
    .cliente-info .form-group:last-child {
      border-bottom: none;
      margin-bottom: 0;
    }
    
    .cliente-info label {
      color: #495057;
      margin-bottom: 5px;
    }
    
    .cliente-info p {
      color: #212529;
      font-size: 16px;
      margin: 0;
    }
    
    .cliente-info ul {
      margin: 0;
      padding-left: 20px;
    }
    
    .cliente-info li {
      color: #212529;
      font-size: 16px;
      margin-bottom: 5px;
    }
    
    .acciones {
      display: flex;
      gap: 10px;
    }
  `]
})
export class ClienteDetailComponent implements OnInit {
  cliente: Cliente | null = null;
  loading = false;
  error: string | null = null;

  constructor(
    private clienteService: ClienteService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const id = +params['id'];
      if (id) {
        this.cargarCliente(id);
      }
    });
  }

  cargarCliente(id: number): void {
    this.loading = true;
    this.error = null;
    
    this.clienteService.getClienteById(id).subscribe({
      next: (cliente) => {
        this.cliente = cliente;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el cliente';
        this.loading = false;
        console.error('Error:', err);
      }
    });
  }
}
