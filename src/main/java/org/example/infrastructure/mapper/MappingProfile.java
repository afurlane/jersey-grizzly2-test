package org.example.infrastructure.mapper;

import org.modelmapper.ModelMapper;

public interface MappingProfile {
    ModelMapper configure(ModelMapper modelMapper);
}
