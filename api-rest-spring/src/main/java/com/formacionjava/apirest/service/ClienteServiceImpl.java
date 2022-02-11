package com.formacionjava.apirest.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionjava.apirest.dao.ClienteDao;
import com.formacionjava.apirest.entity.Cliente;
import com.formacionjava.apirest.entity.Region;

@Service
public class ClienteServiceImpl implements ClienteService {
	
	@Autowired
	private ClienteDao clienteDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		
		return (List<Cliente>) clienteDao.findAll();
	}
	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		
		return  clienteDao.findById(id).orElse(null);
	}
	@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		
		return clienteDao.save(cliente);
	}
	@Override
	public void delete(Long id) {
		
		clienteDao.deleteById(id);
		
	}
	@Override
	@Transactional
	public List<Region> findAllRegiones() {
		return clienteDao.findAllRegiones();
	}
	
	
}
