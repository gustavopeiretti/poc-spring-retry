package com.example.demo.repository;

import com.example.demo.model.RetryModel;
import org.springframework.data.repository.CrudRepository;

public interface RetriesRepository extends CrudRepository<RetryModel, Long> {
}
