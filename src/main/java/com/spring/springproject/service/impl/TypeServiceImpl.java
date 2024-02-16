package com.spring.springproject.service.impl;

import com.spring.springproject.dto.TypeDto;
import com.spring.springproject.entities.Category;
import com.spring.springproject.entities.Type;
import com.spring.springproject.repositories.TypeRepository;
import com.spring.springproject.service.interfaces.TypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TypeServiceImpl implements TypeService {
    private final ModelMapper modelMapper;
    private final TypeRepository repository;

    @Autowired
    public TypeServiceImpl(ModelMapper modelMapper, TypeRepository repository) {
        this.modelMapper = modelMapper;
        this.repository = repository;
    }


    @Override
    public Page<TypeDto> findAll(Pageable pageable, String name) {
            Page<Type> types = repository.findAll(Type.builder()
                    .name(name)
                    .category(Category.builder().name("").build())
                    .build(), pageable);
            List<TypeDto> typeDtoList = types
                    .stream()
                    .map(type -> modelMapper.map(type, TypeDto.class))
                    .toList();
            return new PageImpl<>(typeDtoList, pageable, types.getTotalElements());
    }

    @Override
    public Set<TypeDto> findAll() {
        return repository.findAll()
                .stream()
                .map(type -> modelMapper.map(type, TypeDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public TypeDto findById(Integer id) {
        return repository.findById(id)
                .stream()
                .map(type -> modelMapper.map(type, TypeDto.class))
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional
    public TypeDto save(TypeDto object) {
        Type type = modelMapper.map(object, Type.class);
        type = repository.save(type);
        return modelMapper.map(type, TypeDto.class);
    }

    @Override
    @Transactional
    public void update(TypeDto object) {
        repository.update(modelMapper.map(object, Type.class));
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }


    @Override
    @Transactional
    public void update(Integer id, String name) {
        Type type = repository.findById(id).orElse(null);
        if (type != null) {
            type.setName(name);
            repository.update(type);
        }
    }

    @Override
    @Transactional
    public TypeDto save(String name) {
        Type type = new Type();
        type.setName(name);
        type = repository.save(type);
        return modelMapper.map(type, TypeDto.class);
    }
}
