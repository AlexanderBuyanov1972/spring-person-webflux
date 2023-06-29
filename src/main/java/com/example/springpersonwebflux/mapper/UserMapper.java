package com.example.springpersonwebflux.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.example.springpersonwebflux.dto.UserDto;
import com.example.springpersonwebflux.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto map(UserEntity userEntity);

    @InheritInverseConfiguration
    UserEntity map(UserDto userDto);

}
