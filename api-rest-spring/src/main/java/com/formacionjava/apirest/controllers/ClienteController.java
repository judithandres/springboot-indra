package com.formacionjava.apirest.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.formacionjava.apirest.entity.Cliente;
import com.formacionjava.apirest.service.ClienteService;

@RestController
@RequestMapping("/api")
public class ClienteController {

		@Autowired
		private ClienteService servicio;
		
		@GetMapping("/clientes")
		public List<Cliente> cliente (){
			return servicio.findAll();
		}
		@GetMapping("/clientes/{id}")
		public ResponseEntity<?> clienteShow(@PathVariable Long id){
			Cliente cliente = null;
			
			Map<String,Object> response = new HashMap<>();
			try  {
				cliente = servicio.findById(id);
			}catch (DataAccessException e){
				response.put("mensaje", "Error al relizar busqueda de id");
				response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);

			}
			
			if(cliente == null) {
				response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);

			}
			return new ResponseEntity<Cliente>(cliente,HttpStatus.OK);
		}
		
		/*@GetMapping("/clientes/{id}")
		public Cliente clienteShow(@PathVariable Long id) {
			return servicio.findById(id);
		}*/
		
		/*@PostMapping("/clientes")
		@ResponseStatus(HttpStatus.CREATED)
		public Cliente saveCliente(@RequestBody Cliente cliente) {
			return servicio.save(cliente);
		}*/
		
		@PostMapping("/clientes")
		@ResponseStatus(HttpStatus.CREATED)
		public ResponseEntity<?> saveCliente(@RequestBody Cliente cliente) {
			
			Cliente clienteNew = null;
			
			Map<String,Object> response = new HashMap<>();
			
			try {
				clienteNew = servicio.save(cliente);
			} catch(DataAccessException e) {
				response.put("mensaje", "Error al realizar insert a la base de datos");
				response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("mensaje", "El cliente ha sido creado con éxito");
			response.put("cliente", clienteNew);
			
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
		}
		
		
		
		
		
		/*@PutMapping("/clientes/{id}")
		@ResponseStatus(HttpStatus.CREATED)
		public Cliente updateCliente(@RequestBody Cliente cliente, @PathVariable Long id) {
			Cliente clienteUpdate = servicio.findById(id);
			
			clienteUpdate.setNombre(cliente.getNombre());
			clienteUpdate.setApellido(cliente.getApellido());
			clienteUpdate.setEmail(cliente.getEmail());
			clienteUpdate.setTelefono(cliente.getTelefono());
			
			return servicio.save(clienteUpdate);
		}
		*/
		
		@PutMapping("/clientes/{id}")
		@ResponseStatus(HttpStatus.CREATED)
		public ResponseEntity<?> updateCliente(@RequestBody Cliente cliente, @PathVariable Long id) {
			
			Cliente clienteActual = servicio.findById(id);
			Map<String,Object> response = new HashMap<>();

				try {
					clienteActual.setNombre(cliente.getNombre());
					clienteActual.setApellido(cliente.getApellido());
					clienteActual.setEmail(cliente.getEmail());
					clienteActual.setTelefono(cliente.getTelefono());
					clienteActual.setCreateAt(cliente.getCreateAt());
					
					servicio.save(clienteActual);
					
				}catch (DataAccessException e){
					
					response.put("mensaje", "Error al realizar la actualizacion a la base de datos");
					response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
					return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
				response.put("mensaje", "El cliente ha sido actualizado con éxito");
				response.put("cliente", clienteActual);
				
				return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
				
		}
			
			
		//@DeleteMapping("/clientes/{id}")
		//@ResponseStatus(HttpStatus.OK)
		//public void deleteCliente(@PathVariable Long id) {
		//	servicio.delete(id);
		//}
		/*@DeleteMapping("/clientes/{id}")
		@ResponseStatus(HttpStatus.OK)
		public Cliente deleteCliente(@PathVariable Long id) {
			Cliente clienteBorrado = servicio.findById(id);
			servicio.delete(id);
			return clienteBorrado;
		}*/
		
		@DeleteMapping("/clientes/{id}")
		public ResponseEntity<?> deleteCliente(@RequestBody Cliente cliente, @PathVariable Long id) {
			
			Cliente clienteBorrado = servicio.findById(id);
			Map<String,Object> response = new HashMap<>();
			
			try {
				if(clienteBorrado == null) {
					response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos")));
					return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);

				}
				String nombreFotoAnterior = cliente.getImagen();
				
				if(nombreFotoAnterior !=null && nombreFotoAnterior.length()>0) {
					Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
					File archivoFotoAnterior = rutaFotoAnterior.toFile();
					
					if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
						archivoFotoAnterior.delete();
					}

				}
				
				servicio.delete(id);
				
			}catch (DataAccessException e){
				
				response.put("mensaje", "Error al realizar la eliminacion de un cliente");
				response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("mensaje", "El cliente ha sido eliminado con éxito");
			response.put("cliente", clienteBorrado);
			
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
			

		}
		
		
		@PostMapping("/clientes/upload")
		public ResponseEntity<?>uploadImagen(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
					
			Map<String,Object> response = new HashMap<>();
			
			Cliente cliente = servicio.findById(id);
			
			if(!archivo.isEmpty()) {
				
				//String nombreArchivo = archivo.getOriginalFilename();
				String nombreArchivo= UUID.randomUUID().toString()+"_"+archivo.getOriginalFilename().replace(" ","");
				Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
				
					try {
						Files.copy(archivo.getInputStream(), rutaArchivo);
					} catch(IOException e) {
						response.put("mensaje", "Error al realizar la eliminacion de un cliente");
						response.put("error", e.getMessage().concat("_ ").concat(e.getCause().getMessage()));
						
						return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
					}
					
					String nombreFotoAnterior = cliente.getImagen();
					
					if(nombreFotoAnterior !=null && nombreFotoAnterior.length()>0) {
						Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
						File archivoFotoAnterior = rutaFotoAnterior.toFile();
						
						if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
							archivoFotoAnterior.delete();
						}

					}
					cliente.setImagen(nombreArchivo);
					servicio.save(cliente);
					response.put("cliente", cliente);
					response.put("mensaje","Subida correcta de imagen"+ nombreArchivo);
					
					
					
				} else {
					response.put("Archivo", "archivo vacio");

				}
			
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);

			}
			
		}
		
		
		
		
		

