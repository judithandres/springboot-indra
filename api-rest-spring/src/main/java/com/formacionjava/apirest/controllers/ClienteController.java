package com.formacionjava.apirest.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
		public Cliente clienteShow(@PathVariable Long id) {
			return servicio.findById(id);
		}
		
		@PostMapping("/clientes")
		@ResponseStatus(HttpStatus.CREATED)
		public Cliente saveCliente(@RequestBody Cliente cliente) {
			return servicio.save(cliente);
		}
		@PutMapping("/clientes/{id}")
		@ResponseStatus(HttpStatus.CREATED)
		public Cliente updateCliente(@RequestBody Cliente cliente, @PathVariable Long id) {
			Cliente clienteUpdate = servicio.findById(id);
			
			clienteUpdate.setNombre(cliente.getNombre());
			clienteUpdate.setApellido(cliente.getApellido());
			clienteUpdate.setEmail(cliente.getEmail());
			clienteUpdate.setTelefono(cliente.getTelefono());
			
			return servicio.save(clienteUpdate);
		}
		
		
		//@DeleteMapping("/clientes/{id}")
		//@ResponseStatus(HttpStatus.OK)
		//public void deleteCliente(@PathVariable Long id) {
		//	servicio.delete(id);
		//}
		@DeleteMapping("/clientes/{id}")
		@ResponseStatus(HttpStatus.OK)
		public Cliente deleteCliente(@PathVariable Long id) {
			Cliente clienteBorrado = servicio.findById(id);
			servicio.delete(id);
			return clienteBorrado;
		}
		
		
		
		
		
		
		
}
