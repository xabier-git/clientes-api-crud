export interface TipoCliente {
  codigo: string;
  descripcion: string;
}

export interface Cliente {
  id?: number;
  rut: string;
  nombre: string;
  apellido: string;
  edad: number;
  email: string;
  codTipoCliente: string;
  telefonos: string[];
  tipoCliente?: TipoCliente;
}
