package com.jaggaer.mapper;

import com.jaggaer.dto.TaskDTO;
import com.jaggaer.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(target = "id", expression = "java(task.getId() != null ? String.valueOf(task.getId()) : null)")
    TaskDTO toDTO(Task task);

    @Mapping(target = "id", expression = "java(taskDTO.getId() != null ? Long.parseLong(taskDTO.getId()) : null)")
    Task toEntity(TaskDTO taskDTO);
}
