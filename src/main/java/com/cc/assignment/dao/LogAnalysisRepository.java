package com.cc.assignment.dao;

import com.cc.assignment.entity.EventDetail;
import org.springframework.data.repository.CrudRepository;

public interface LogAnalysisRepository extends CrudRepository<EventDetail, String> {
}
