package com.example.kanban.service;

import com.example.kanban.exception.ResourceNotFoundException;
import com.example.kanban.api.dto.ResponsibleRequest;
import com.example.kanban.repository.ResponsibleRepository;
import com.example.kanban.mapper.ResponsibleMapper;
import com.example.kanban.domain.Responsible;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponsibleServiceTest {

    @Mock
    private ResponsibleRepository responsibleRepository;

    @Mock
    private ResponsibleMapper responsibleMapper;

    @InjectMocks
    private ResponsibleService responsibleService;

    private ResponsibleRequest validRequest;
    private Responsible responsible;

    @BeforeEach
    void setUp() {
        validRequest = new ResponsibleRequest("Test User", "test@kanban.com", "Tester");
        responsible = new Responsible();
        responsible.setId(1L);
        responsible.setName("Test User");
        responsible.setEmail("test@kanban.com");
    }

    @Test
    void create_ShouldReturnResponsible_WhenEmailIsUnique() {
        // Arrange
        when(responsibleRepository.findByEmail(validRequest.email())).thenReturn(Optional.empty());
        when(responsibleMapper.toEntity(validRequest)).thenReturn(responsible);
        when(responsibleRepository.save(responsible)).thenReturn(responsible);

        Responsible created = responsibleService.create(validRequest);

        assertNotNull(created);
        assertEquals(responsible.getEmail(), created.getEmail());
        verify(responsibleRepository, times(1)).save(responsible);
    }

    @Test
    void create_ShouldThrowException_WhenEmailExists() {

        when(responsibleRepository.findByEmail(validRequest.email())).thenReturn(Optional.of(responsible));

        assertThrows(DataIntegrityViolationException.class, () -> responsibleService.create(validRequest));
        verify(responsibleRepository, never()).save(any(Responsible.class));
    }

    @Test
    void findById_ShouldReturnResponsible_WhenFound() {

        when(responsibleRepository.findById(1L)).thenReturn(Optional.of(responsible));

        Responsible found = responsibleService.findById(1L);

        assertEquals(1L, found.getId());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {

        when(responsibleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> responsibleService.findById(99L));
    }

    @Test
    void update_ShouldUpdateResponsible_WhenFoundAndEmailIsUnique() {

        ResponsibleRequest updateRequest = new ResponsibleRequest("Updated Name", "new@kanban.com", "Manager");
        Responsible existing = responsible;

        when(responsibleRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(responsibleRepository.findByEmail(updateRequest.email())).thenReturn(Optional.empty());
        when(responsibleRepository.save(existing)).thenReturn(existing);

        Responsible updated = responsibleService.update(1L, updateRequest);

        assertNotNull(updated);
        verify(responsibleMapper, times(1)).updateEntityFromRequest(existing, updateRequest);
    }

    @Test
    void update_ShouldThrowException_WhenEmailExistsOnAnotherUser() {

        ResponsibleRequest updateRequest = new ResponsibleRequest("Updated Name", "another@kanban.com", "Manager");
        Responsible otherUser = new Responsible();
        otherUser.setId(2L);
        otherUser.setEmail("another@kanban.com");

        when(responsibleRepository.findById(1L)).thenReturn(Optional.of(responsible));
        when(responsibleRepository.findByEmail(updateRequest.email())).thenReturn(Optional.of(otherUser));

        assertThrows(DataIntegrityViolationException.class, () -> responsibleService.update(1L, updateRequest));
        verify(responsibleRepository, never()).save(any(Responsible.class));
    }

    @Test
    void delete_ShouldCallDelete_WhenExists() {

        when(responsibleRepository.existsById(1L)).thenReturn(true);
        doNothing().when(responsibleRepository).deleteById(1L);

        responsibleService.delete(1L);

        verify(responsibleRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {

        when(responsibleRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> responsibleService.delete(99L));
        verify(responsibleRepository, never()).deleteById(anyLong());
    }
}