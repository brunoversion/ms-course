package com.github.brunoversion.hrpayroll.FeignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.github.brunoversion.hrpayroll.entities.Worker;

@Component
@FeignClient(name = "hr-worker", url = "localhost:8001", path = "/workers")
public interface WorkerFeignClient {

	@GetMapping("/{id}")
	ResponseEntity<Worker> findById(@PathVariable Long id);
}
