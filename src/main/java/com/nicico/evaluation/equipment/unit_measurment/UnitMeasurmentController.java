//package com.nicico.evaluation.equipment.unit_measurment;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nicico.copper.common.domain.ConstantVARs;
//import com.nicico.copper.common.dto.search.SearchDTO;
//import com.nicico.copper.core.util.report.ReportUtil;
//import com.nicico.evaluation.common.PageDTO;
//import com.nicico.evaluation.equipment.standard_test_status.StandardTestStatusDTO;
//import com.nicico.evaluation.exception.NotFoundException;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import net.sf.jasperreports.engine.data.JsonDataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//
//import javax.imageio.ImageIO;
//import javax.net.ssl.HttpsURLConnection;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//import java.io.ByteArrayInputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//@Transactional
//@RestController
//@RequestMapping("/api/unitmeasurment")
//public class UnitMeasurmentController {
//
//    private final IUnitMeasurmentService unitMeasurmentService;
//    private final UnitMeasurmentMapper unitMeasurmentMapper;
//    private final  ObjectMapper objectMapper;
//    private final  ReportUtil reportUtil;
//
//    @Autowired
//    public UnitMeasurmentController(IUnitMeasurmentService unitMeasurmentService, UnitMeasurmentMapper unitMeasurmentMapper, ObjectMapper objectMapper, ReportUtil reportUtil) {
//        this.unitMeasurmentService = unitMeasurmentService;
//        this.unitMeasurmentMapper = unitMeasurmentMapper;
//        this.objectMapper = objectMapper;
//        this.reportUtil = reportUtil;
//    }
//
//    @ApiOperation(value = "Add new UnitMeasurment")
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "Added successfully"),
//            @ApiResponse(code = 401, message = "You are not authorized"),
//            @ApiResponse(code = 409, message = "It is duplicate"),
//            @ApiResponse(code = 500, message = "Server error")
//    })
//    @PostMapping("/")
//   //@PreAuthorize("hasAuthority('HSEVFOUR_STANDARDTEST_API_CREATE')")
//    public  ResponseEntity<Void> addNewUnitMeasurment(
//            @Valid @RequestBody UnitMeasurmentDTO unitMeasurmentDTO )
//    {
//        UnitMeasurment unitMeasurment  = unitMeasurmentMapper.toUnitMeasurment(unitMeasurmentDTO);
//        unitMeasurmentService.save (unitMeasurment);
//        return ResponseEntity.status(HttpsURLConnection.HTTP_CREATED).build();
//
//    }
//
//    @ApiOperation(value = "Get UnitMeasurment By Id")
//    @GetMapping(value = "/{id}")
//   //@PreAuthorize("hasAuthority('HSEVFOUR_STANDARDTEST_FILTER_ENABLE')")
//    public ResponseEntity<UnitMeasurmentDTO> getStandardTestById(@PathVariable("id") Long id) {
//
//        UnitMeasurment unitMeasurment =  unitMeasurmentService.getById(id);
//        UnitMeasurmentDTO unitMeasurmentDTO  = unitMeasurmentMapper.toUnitMeasurmentDTO(unitMeasurment);
//        return  ResponseEntity.ok(unitMeasurmentDTO);
//
//    }
//
//    @ApiOperation(value = "Get All UnitMeasurment ")
//    @GetMapping(value = "/all")
//       // @PreAuthorize("hasAuthority('HSEVFOUR_STANDARDTEST_FILTER_ENABLE')")
//    public ResponseEntity<List<UnitMeasurmentDTO>> getAllStandardTest() {
//
//        List<UnitMeasurment>  unitMeasurmentList =  unitMeasurmentService.getAll();
//        List<UnitMeasurmentDTO>  unitMeasurmentDTOList  = unitMeasurmentMapper.toUnitMeasurmentDTOList(unitMeasurmentList);
//        return  ResponseEntity.ok(unitMeasurmentDTOList);
//
//    }
//
//
//    @ApiOperation(value = "Get All UnitMeasurment Pagination ")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successfully get questions"),
//            @ApiResponse(code = 401, message = "You are not authorized"),
//            @ApiResponse(code = 404, message = "Not found StandardTest"),
//            @ApiResponse(code = 500, message = "Server error")
//    })
//    @GetMapping("/{page}/{size}")
//  // @PreAuthorize("hasAuthority('HSEVFOUR_STANDARDTEST_FILTER_ENABLE')")
//    public ResponseEntity<PageDTO> getAll(@PathVariable(value = "page") int page, @PathVariable(value = "size") int size) {
//
//        Page<UnitMeasurment> unitMeasurmentPage =  unitMeasurmentService.getAll(page, size);
//        List<UnitMeasurmentDTO> unitMeasurmentDTOList=unitMeasurmentMapper.toUnitMeasurmentDTOList(unitMeasurmentPage.getContent());
//
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setCurrentPage(page);
//        pageDTO.setPageSize(size);
//        pageDTO.setTotalPage(unitMeasurmentPage.getTotalPages());
//        pageDTO.setTotalItems(unitMeasurmentPage.getTotalElements());
//        pageDTO.setList(unitMeasurmentDTOList);
//
//        return ResponseEntity.ok(pageDTO);
//
//    }
//
//
//    @ApiOperation(value = "Update UnitMeasurment ")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successfully get questions"),
//            @ApiResponse(code = 401, message = "You are not authorized"),
//            @ApiResponse(code = 404, message = "Not found StandardTest"),
//            @ApiResponse(code = 500, message = "Server error")
//    })
//    @PutMapping("/")
//   // @PreAuthorize("hasAuthority('U_STANDARDTEST')")
// // @PreAuthorize("hasAuthority('HSEVFOUR_STANDARDTEST_API_UPDATE')")
//    public ResponseEntity<Void> update(
//                @Valid @RequestBody UnitMeasurmentDTO unitMeasurmentDTO )
//      {
//
//        UnitMeasurment unitMeasurment  = unitMeasurmentMapper.toUnitMeasurment(unitMeasurmentDTO);
//        unitMeasurmentService.update(unitMeasurment);
//        return ResponseEntity.status(HttpsURLConnection.HTTP_OK).build();
//
//    }
//
//    @ApiOperation(value = "Delete List Of StandardTest ")
//    @DeleteMapping(value = "/")
//  //@PreAuthorize("hasAuthority('HSEVFOUR_STANDARDTEST_API_DELETE')")
//    public ResponseEntity<Void> deleteStandardTestById(@RequestBody List<Long> unitMeasurmentIds ) {
//
//        unitMeasurmentService.delete(unitMeasurmentIds);
//        return ResponseEntity.status(HttpsURLConnection.HTTP_OK).build();
//    }
//
//
//@ApiOperation("get all StandardTest by required filter ")
//@PostMapping(path = "/search")
// //@PreAuthorize("hasAuthority('HSEVFOUR_STANDARDTEST_FILTER_ENABLE')")
//public ResponseEntity<SearchDTO.SearchRs<StandardTestStatusDTO>> getAllStandardTest(@RequestBody SearchDTO.SearchRq filter) throws NotFoundException {
//    return new ResponseEntity<>(unitMeasurmentService.searchStandardTest(filter), HttpStatus.OK);
//}
//
//
//
//
//
//
//
//    @GetMapping(value = "/print/{type}")
//    //@PreAuthorize("hasAuthority('HSEVFOUR_STANDARDTEST_REPORT_ENABLE')")
//    public void printStandardTestReport(HttpServletResponse response, @PathVariable String type ) throws Exception {
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put(ConstantVARs.REPORT_TYPE, type.toLowerCase());
//        parameters.put("logo_nicico", ImageIO.read(this.getClass().getResourceAsStream("/report/nicico-logo.png")));
//
//        String jsonData = "{" + "\"ItemDataset\": " + objectMapper.writeValueAsString(unitMeasurmentService.getAll()) + "}";
//        JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8)));
//        reportUtil.export("/report/standardtest.jasper", parameters, jsonDataSource, response);
//
//    }
//
//    @PostMapping(value = "/print/{type}")
//    //@PreAuthorize("hasAuthority('HSEVFOUR_STANDARDTEST_REPORT_ENABLE')")
//    public void printStandardTestReport(@RequestBody SearchDTO.SearchRq filter,HttpServletResponse response, @PathVariable String type ) throws Exception {
//
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put(ConstantVARs.REPORT_TYPE, type.toLowerCase());
//        parameters.put("logo_nicico", ImageIO.read(this.getClass().getResourceAsStream("/report/nicico-logo.png")));
//
//        String jsonData =  objectMapper.writeValueAsString(unitMeasurmentService.searchStandardTest(filter)) ;
//        jsonData=jsonData.replace("list","ItemDataset");
//        JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8)));
//        reportUtil.export("/report/standardtest.jasper", parameters, jsonDataSource, response);
//
//    }
//
//
//
//
//
//}
