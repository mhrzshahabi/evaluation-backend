package com.nicico.evaluation.group;

import java.util.List;


public interface IGroupService {
    List<Group> getAll();
    Group getById(Integer id);
    Group create(Group newGroup);
    void remove(Integer id);
    Group update(Integer id, Group updateGroup);
}

