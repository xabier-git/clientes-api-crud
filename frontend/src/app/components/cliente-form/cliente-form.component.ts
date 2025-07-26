import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Cliente, TipoCliente } from '../../models/cliente.model';
import { ClienteService } from '../../services/cliente.service';
import { TipoClienteService } from '../../services/tipo-cliente.service';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="card">
      <h2>{{ esEdicion ? 'Editar Cliente' : 'Nuevo Cliente' }}</h2>
      
      <div *ngIf="loading" class="loading">
        Cargando...
      </div>
      
      <div *ngIf="error" class="alert alert-danger">
        {{ error }}
      </div>
      
      <form *ngIf="!loading" [formGroup]="clienteForm" (ngSubmit)="onSubmit()">
        <div class="form-group">
          <label for="rut">RUT *</label>
          <input 
            type="text" 
            id="rut" 
            class="form-control" 
            formControlName="rut"
            placeholder="12345678-9">
          <div *ngIf="clienteForm.get('rut')?.invalid && clienteForm.get('rut')?.touched" class="alert alert-danger">
            El RUT es obligatorio
          </div>
        </div>

        <div class="form-group">
          <label for="nombre">Nombre *</label>
          <input 
            type="text" 
            id="nombre" 
            class="form-control" 
            formControlName="nombre">
          <div *ngIf="clienteForm.get('nombre')?.invalid && clienteForm.get('nombre')?.touched" class="alert alert-danger">
            El nombre es obligatorio
          </div>
        </div>

        <div class="form-group">
          <label for="apellido">Apellido *</label>
          <input 
            type="text" 
            id="apellido" 
            class="form-control" 
            formControlName="apellido">
          <div *ngIf="clienteForm.get('apellido')?.invalid && clienteForm.get('apellido')?.touched" class="alert alert-danger">
            El apellido es obligatorio
          </div>
        </div>

        <div class="form-group">
          <label for="edad">Edad *</label>
          <input 
            type="number" 
            id="edad" 
            class="form-control" 
            formControlName="edad"
            min="0" 
            max="150">
          <div *ngIf="clienteForm.get('edad')?.invalid && clienteForm.get('edad')?.touched" class="alert alert-danger">
            La edad debe estar entre 0 y 150 años
          </div>
        </div>

        <div class="form-group">
          <label for="email">Email *</label>
          <input 
            type="email" 
            id="email" 
            class="form-control" 
            formControlName="email">
          <div *ngIf="clienteForm.get('email')?.invalid && clienteForm.get('email')?.touched" class="alert alert-danger">
            Ingrese un email válido
          </div>
        </div>

        <div class="form-group">
          <label for="codTipoCliente">Tipo de Cliente *</label>
          <select 
            id="codTipoCliente" 
            class="form-control" 
            formControlName="codTipoCliente">
            <option value="">Seleccione un tipo</option>
            <option *ngFor="let tipo of tiposCliente" [value]="tipo.codigo">
              {{ tipo.descripcion }}
            </option>
          </select>
          <div *ngIf="clienteForm.get('codTipoCliente')?.invalid && clienteForm.get('codTipoCliente')?.touched" class="alert alert-danger">
            Debe seleccionar un tipo de cliente
          </div>
        </div>

        <div class="form-group">
          <label>Teléfonos</label>
          <div formArrayName="telefonos">
            <div *ngFor="let telefono of telefonosArray.controls; let i = index" class="form-group">
              <div style="display: flex; gap: 10px; align-items: center;">
                <input 
                  type="text" 
                  class="form-control" 
                  [formControlName]="i"
                  placeholder="Número de teléfono">
                <button 
                  type="button" 
                  class="btn btn-danger" 
                  (click)="eliminarTelefono(i)">
                  Eliminar
                </button>
              </div>
            </div>
          </div>
          <button 
            type="button" 
            class="btn btn-secondary mt-3" 
            (click)="agregarTelefono()">
            Agregar Teléfono
          </button>
        </div>

        <div class="form-group mt-3">
          <button 
            type="submit" 
            class="btn btn-success mr-2"
            [disabled]="clienteForm.invalid || enviando">
            {{ enviando ? 'Guardando...' : 'Guardar' }}
          </button>
          <button 
            type="button" 
            class="btn btn-secondary" 
            (click)="volver()">
            Cancelar
          </button>
        </div>
      </form>
    </div>
  `
})
export class ClienteFormComponent implements OnInit {
  clienteForm: FormGroup;
  tiposCliente: TipoCliente[] = [];
  esEdicion = false;
  clienteId: number | null = null;
  loading = false;
  error: string | null = null;
  enviando = false;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private tipoClienteService: TipoClienteService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.clienteForm = this.fb.group({
      rut: ['', [Validators.required]],
      nombre: ['', [Validators.required]],
      apellido: ['', [Validators.required]],
      edad: ['', [Validators.required, Validators.min(0), Validators.max(150)]],
      email: ['', [Validators.required, Validators.email]],
      codTipoCliente: ['', [Validators.required]],
      telefonos: this.fb.array([])
    });
  }

  get telefonosArray(): FormArray {
    return this.clienteForm.get('telefonos') as FormArray;
  }

  ngOnInit(): void {
    this.cargarTiposCliente();
    
    // Verificar si es edición
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.esEdicion = true;
        this.clienteId = +params['id'];
        this.cargarCliente(this.clienteId);
      } else {
        // Agregar un teléfono vacío para nuevos clientes
        this.agregarTelefono();
      }
    });
  }

  cargarTiposCliente(): void {
    this.tipoClienteService.getTiposCliente().subscribe({
      next: (tipos) => {
        this.tiposCliente = tipos;
      },
      error: (err) => {
        console.error('Error al cargar tipos de cliente:', err);
      }
    });
  }

  cargarCliente(id: number): void {
    this.loading = true;
    this.clienteService.getClienteById(id).subscribe({
      next: (cliente) => {
        this.clienteForm.patchValue({
          rut: cliente.rut,
          nombre: cliente.nombre,
          apellido: cliente.apellido,
          edad: cliente.edad,
          email: cliente.email,
          codTipoCliente: cliente.codTipoCliente
        });
        
        // Cargar teléfonos
        cliente.telefonos.forEach(telefono => {
          this.telefonosArray.push(this.fb.control(telefono));
        });
        
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el cliente';
        this.loading = false;
        console.error('Error:', err);
      }
    });
  }

  agregarTelefono(): void {
    this.telefonosArray.push(this.fb.control(''));
  }

  eliminarTelefono(index: number): void {
    this.telefonosArray.removeAt(index);
  }

  onSubmit(): void {
    if (this.clienteForm.invalid) {
      this.clienteForm.markAllAsTouched();
      return;
    }

    this.enviando = true;
    const clienteData: Cliente = this.clienteForm.value;
    
    // Filtrar teléfonos vacíos
    clienteData.telefonos = clienteData.telefonos.filter(tel => tel.trim() !== '');

    if (this.esEdicion && this.clienteId) {
      this.clienteService.updateCliente(this.clienteId, clienteData).subscribe({
        next: () => {
          this.router.navigate(['/clientes']);
        },
        error: (err) => {
          this.error = 'Error al actualizar el cliente';
          this.enviando = false;
          console.error('Error:', err);
        }
      });
    } else {
      this.clienteService.createCliente(clienteData).subscribe({
        next: () => {
          this.router.navigate(['/clientes']);
        },
        error: (err) => {
          this.error = 'Error al crear el cliente';
          this.enviando = false;
          console.error('Error:', err);
        }
      });
    }
  }

  volver(): void {
    this.router.navigate(['/clientes']);
  }
}
