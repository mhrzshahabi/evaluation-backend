package com.nicico.evaluation.group;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/group")
@Validated
@AllArgsConstructor
public class GroupController {

    private final GroupMapper groupMapper;
    private final IGroupService groupService;

    @GetMapping("/")
    public ResponseEntity<List<GroupDTO.retrieve>> index(){
        List<Group> groups = groupService.getAll();
        List<GroupDTO.retrieve> groupDtoRetrieve = groupMapper.toGroupDtoRetrieves(groups);
        return  ResponseEntity.ok(groupDtoRetrieve);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO.retrieve> getOne(@PathVariable @Min(1) Integer id){
        Group group = groupService.getById(id);
        GroupDTO.retrieve groupDto = groupMapper.toGroupDtoRetrieve(group);
        return ResponseEntity.ok(groupDto);
    }


    @PostMapping("/create")
    public ResponseEntity<Group> create(@RequestBody @Valid GroupDTO.create reqGroupForm) {
        Group inputGroup = groupMapper.toGroup(reqGroupForm);
        return ResponseEntity.ok(groupService.create(inputGroup));
    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Integer> remove(@PathVariable @Min(1) Integer id){
        groupService.remove(id);
        return ResponseEntity.ok(1);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Group> update(@PathVariable @Min(1) Integer id, @RequestBody @Valid GroupDTO.create reqGroupForm){
        Group updateGroup = groupMapper.toGroup(reqGroupForm);
        return ResponseEntity.ok(groupService.update(id, updateGroup));
    }


}
