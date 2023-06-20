package com.nicico.evaluation.feignClient;

import com.nicico.evaluation.dto.HrmPersonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "hrmClient", url ="${nicico.hrmClient}")
public interface HrmFeignClient {

    @GetMapping("/persons/profile/{nationalCode}")
    HrmPersonDTO getPersonProfileByNationalCode(@PathVariable("nationalCode") String nationalCode, @RequestHeader("Authorization") String token);
}
