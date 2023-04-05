package com.nicico.evaluation.service;

import com.nicico.evaluation.iservice.IGroupService;
import com.nicico.evaluation.model.Group;
import com.nicico.evaluation.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GroupService implements IGroupService {

    private GroupRepository groupRepository;

    @Override
    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group getById(Integer id) {
        if(id <= 0) throw new IllegalArgumentException("id must be bigger than");
        return groupRepository.findById(id).orElseThrow();
    }

    @Override
    public Group create(Group newGroup) {
        Objects.requireNonNull(newGroup);

        return groupRepository.save(newGroup);
    }

    @Override
    public void remove(Integer id) {
        if(id <= 0) throw new IllegalArgumentException("id must be bigger than");
        groupRepository.deleteById(id);
    }

    @Override
    public Group update(Integer id, Group updateGroup) {
        Objects.requireNonNull(updateGroup);

        Group group =  groupRepository.findById(id).orElseThrow();
        group.setCode(updateGroup.getCode());
        group.setTitle(updateGroup.getTitle());
        group.setDefinitionAllowed(updateGroup.getDefinitionAllowed());

        return groupRepository.save(group);
    }
}
