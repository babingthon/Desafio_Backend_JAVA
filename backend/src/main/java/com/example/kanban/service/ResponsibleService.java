package com.example.kanban.service;

import com.example.kanban.exception.ResourceNotFoundException;
import com.example.kanban.api.dto.ResponsibleRequest;
import com.example.kanban.repository.ResponsibleRepository;
import com.example.kanban.mapper.ResponsibleMapper;
import com.example.kanban.domain.Responsible;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ResponsibleService implements IResponsibleService {

    private final ResponsibleRepository responsibleRepository;
    private final ResponsibleMapper responsibleMapper;

    @Override
    public Responsible create(ResponsibleRequest request) {
        if (responsibleRepository.findByEmail(request.email()).isPresent()) {
            throw new DataIntegrityViolationException("Email is already in use.");
        }

        Responsible responsible = responsibleMapper.toEntity(request);

        return responsibleRepository.save(responsible);
    }

    @Override
    public Responsible update(Long id, ResponsibleRequest request) {
        Responsible responsible = findById(id);

        if (responsibleRepository.findByEmail(request.email()).isPresent() &&
                !responsible.getEmail().equals(request.email())) {
            throw new DataIntegrityViolationException("Email is already in use by another responsible.");
        }

        responsibleMapper.updateEntityFromRequest(responsible, request);

        return responsibleRepository.save(responsible);
    }

    @Override
    @Transactional(readOnly = true)
    public Responsible findById(Long id) {
        return responsibleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Responsible not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Responsible> findAll(Pageable pageable) {
        // Requisito: Performance (Paginação)
        return responsibleRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        if (!responsibleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Responsible not found with ID: " + id);
        }
        responsibleRepository.deleteById(id);
    }
}