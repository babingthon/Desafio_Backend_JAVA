package com.example.kanban.mapper;

import com.example.kanban.domain.Responsible;
import com.example.kanban.api.dto.ResponsibleRequest;
import com.example.kanban.api.dto.ResponsibleResponse;
import org.springframework.stereotype.Component;

@Component
public class ResponsibleMapper {

    public Responsible toEntity(ResponsibleRequest request) {
        Responsible entity = new Responsible();
        entity.setName(request.name());
        entity.setEmail(request.email());
        entity.setJobTitle(request.jobTitle());
        return entity;
    }

    public void updateEntityFromRequest(Responsible entity, ResponsibleRequest request) {
        entity.setName(request.name());
        entity.setEmail(request.email());
        entity.setJobTitle(request.jobTitle());
    }

    public ResponsibleResponse toResponse(Responsible entity) {
        return new ResponsibleResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getJobTitle(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}