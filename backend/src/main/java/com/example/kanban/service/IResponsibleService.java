package com.example.kanban.service;

import com.example.kanban.domain.Responsible;
import com.example.kanban.api.dto.ResponsibleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IResponsibleService {
    Responsible create(ResponsibleRequest request);
    Responsible update(Long id, ResponsibleRequest request);
    Responsible findById(Long id);
    Page<Responsible> findAll(Pageable pageable);
    void delete(Long id);
}