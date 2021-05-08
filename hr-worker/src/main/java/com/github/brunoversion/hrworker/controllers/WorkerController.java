package com.github.brunoversion.hrworker.controllers;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.brunoversion.hrworker.dtos.WorkerDTO;
import com.github.brunoversion.hrworker.entities.Worker;
import com.github.brunoversion.hrworker.repositories.WorkerRepository;

@RestController
@RequestMapping("workers")
public class WorkerController {
	
	@Autowired
	private WorkerRepository repository;

	@GetMapping
	public ResponseEntity<Page<WorkerDTO>> findAll(@PageableDefault(size = 10) Pageable pageable) {
		return ResponseEntity.ok(repository.findAll(pageable).map(w -> new WorkerDTO(w)));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<WorkerDTO> findById(@PathVariable Long id) {		
		Optional<Worker> worker = repository.findById(id);
		if (worker.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(new WorkerDTO(worker.get()));
	}
	
	@PostMapping
	public ResponseEntity<WorkerDTO> insert (@Valid @RequestBody WorkerDTO dto) {
		Worker workerSaved = repository.save(new Worker(null, dto.getName(), dto.getDailyIncome()));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(workerSaved.getId()).toUri();
		WorkerDTO wDTO = new WorkerDTO(workerSaved);
		return ResponseEntity.created(uri).body(wDTO);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<WorkerDTO> replace(@PathVariable Long id, @Valid @RequestBody WorkerDTO dto) {
		Optional<Worker> target = repository.findById(id);
		if (target.isEmpty()) {
			return ResponseEntity.notFound().build();
		}		
		BeanUtils.copyProperties(dto, target.get(), "id");
		Worker workerSaved = repository.save(target.get());
		return ResponseEntity.ok(new WorkerDTO(workerSaved));		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Optional<Worker> worker = repository.findById(id);
		if (worker.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		repository.delete(worker.get());
		return ResponseEntity.noContent().build();
	}
	
}