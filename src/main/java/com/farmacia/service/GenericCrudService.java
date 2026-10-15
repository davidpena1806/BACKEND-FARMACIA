package com.farmacia.service;

import com.farmacia.entity.Activable;
import com.farmacia.entity.BaseEntity;
import com.farmacia.exception.BusinessException;
import com.farmacia.exception.ResourceNotFoundException;
import com.farmacia.repository.BaseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Patrón Template Method: define el flujo CRUD común y las subclases personalizan
 * filtros (buildSpecification), orden (defaultSort) y validaciones (beforeSave).
 */
public abstract class GenericCrudService<T extends BaseEntity> {

    protected final BaseRepository<T> repository;
    private final String nombreEntidad;

    protected GenericCrudService(BaseRepository<T> repository, String nombreEntidad) {
        this.repository = repository;
        this.nombreEntidad = nombreEntidad;
    }

    @Transactional(readOnly = true)
    public List<T> findAll(Map<String, String> filters) {
        Specification<T> spec = buildSpecification(filters == null ? Map.of() : filters);
        return spec == null ? repository.findAll(defaultSort()) : repository.findAll(spec, defaultSort());
    }

    @Transactional(readOnly = true)
    public T findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(nombreEntidad + " con Id " + id + " no encontrado."));
    }

    /** Sin @Transactional a propósito: el findById final se hace en una sesión nueva y devuelve las relaciones cargadas. */
    public T create(T entity) {
        entity.setId(null);
        beforeSave(entity);
        T saved = repository.save(entity);
        return repository.findById(saved.getId()).orElse(saved);
    }

    public T update(Long id, T entity) {
        findById(id);
        entity.setId(id);
        beforeSave(entity);
        repository.save(entity);
        return findById(id);
    }

    /** Baja lógica si la entidad es Activable; borrado físico en caso contrario. */
    @Transactional
    public void delete(Long id) {
        T entity = findById(id);
        if (entity instanceof Activable activable) {
            activable.setActivo(false);
            repository.save(entity);
        } else {
            repository.delete(entity);
        }
    }

    // ---------- Puntos de extensión ----------

    protected Specification<T> buildSpecification(Map<String, String> filters) {
        return null;
    }

    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.ASC, "id");
    }

    protected void beforeSave(T entity) {
        // por defecto no hace nada
    }

    // ---------- Utilidades para filtros ----------

    protected Specification<T> and(Specification<T> base, Specification<T> other) {
        return base == null ? other : base.and(other);
    }

    protected Specification<T> fieldEquals(String field, Object value) {
        return (root, query, cb) -> cb.equal(root.get(field), value);
    }

    protected Long longParam(Map<String, String> filters, String key) {
        String value = filters.get(key);
        if (value == null || value.isBlank()) return null;
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            throw new BusinessException("El parámetro '" + key + "' debe ser numérico.");
        }
    }

    protected Boolean boolParam(Map<String, String> filters, String key) {
        String value = filters.get(key);
        return (value == null || value.isBlank()) ? null : Boolean.valueOf(value.trim());
    }

    protected String stringParam(Map<String, String> filters, String key) {
        String value = filters.get(key);
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    protected LocalDate dateParam(Map<String, String> filters, String key) {
        String value = filters.get(key);
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDate.parse(value.trim());
        } catch (Exception ex) {
            throw new BusinessException("El parámetro '" + key + "' debe tener formato yyyy-MM-dd.");
        }
    }
}
