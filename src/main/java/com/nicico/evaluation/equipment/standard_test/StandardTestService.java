//
//package com.nicico.evaluation.equipment.standard_test;
//
//import com.nicico.copper.common.domain.criteria.SearchUtil;
//import com.nicico.copper.common.dto.search.SearchDTO;
//import com.nicico.evaluation.common.ExceptionUtil;
//import com.nicico.evaluation.equipment.standard_test_status.*;
//import com.nicico.evaluation.exception.BadRequestException;
//import com.nicico.evaluation.exception.ConflictException;
//import com.nicico.evaluation.exception.NotFoundException;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.hibernate.exception.ConstraintViolationException;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.TypeToken;
//import org.springframework.context.MessageSource;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.dao.InvalidDataAccessApiUsageException;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.Optional;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//
//@AllArgsConstructor
//@Service
//@Slf4j
//public class StandardTestService implements IStandardTestService {
//
//    private final MessageSource messageSource;
//    private final ModelMapper modelMapper;
//    private final StandardTestRepository standardTestRepository;
//
//
//    @Override
//    public StandardTest save(StandardTest standardTest){
//        log.info("start StandardTestService.save() ");
//        StandardTest savedStandardTest=null;
//    try {
//
//
//        savedStandardTest = standardTestRepository.save(savedStandardTest);
//            log.info("successful run  StandardTestService.save() ");
//
//        }
//        catch (ConstraintViolationException ex) {
//        log.error("StandardTestService.save() :" ,messageSource.getMessage("duplicate_found", null, Locale.ENGLISH));
//        throw new ConflictException(messageSource.getMessage("duplicate_found", null, Locale.ENGLISH));
//
//        }
//      catch (DataIntegrityViolationException ex) {
//        log.error("StandardTestService.save() :" ,messageSource.getMessage("duplicate_found", null, Locale.ENGLISH));
//        throw new ConflictException(messageSource.getMessage("duplicate_found", null, Locale.ENGLISH));
//
//        }
//
//
//    catch (BadRequestException ex){
//        log.error("StandardTestStatusService.save() :" ,messageSource.getMessage("bad_request", null, Locale.ENGLISH));
//        throw new BadRequestException(messageSource.getMessage("bad_request", null, Locale.ENGLISH));
//      }
//
//        return savedStandardTest;
//
//    }
//
//
//@Override
//public StandardTest update(StandardTest standardTest){
//    log.info("start StandardTestService.update() ");
//
//    StandardTest lastSavedStandardTest =getById(standardTest.getId());
//
//    if(!lastSavedStandardTest.getUpdatable()) {
//        String message=    messageSource.getMessage("not_allowed_to_upadte_and_delete",null, Locale.ENGLISH);
//        ExceptionUtil.sendAccessDeniedException(message);
//
//    }
//
//
//    StandardTest savedStandardTest = null;
//    try{
//            lastSavedStandardTest.setTitle(savedStandardTest.getTitle());
//            lastSavedStandardTest.setDescription(savedStandardTest.getDescription());
//
//        savedStandardTest = standardTestRepository.save(lastSavedStandardTest);
//
//
//        log.info("successful run  StandardTestService.update() ");
//
//        }
//        catch(DataIntegrityViolationException ex){
//        log.error("StandardTestStatusService.update() :",messageSource.getMessage("duplicate_found",null,Locale.ENGLISH));
//        throw new ConflictException(messageSource.getMessage("duplicate_found",null,Locale.ENGLISH));}
//        catch(NullPointerException ex){
//        log.error("StandardTestStatusService.update() :",messageSource.getMessage("Null value is not accepted",null,Locale.ENGLISH));
//        throw new BadRequestException(messageSource.getMessage("Null value is not accepted",null, Locale.ENGLISH));}
//        catch (Exception e) {}
//
//
//    return savedStandardTest;
//}
//
//@Override
//public StandardTest getById(Long standardtestId){
//        log.info("start StandardTestService.getById() ");
//
//        Optional<StandardTest>  optionalStandardTest = standardTestRepository.findById(standardtestId);
//        if(!optionalStandardTest.isPresent()){
//            String message=messageSource.getMessage("not_found_id",null,Locale.ENGLISH);
//        log.error("StandardTestService.getById() :",message);
//        throw new NotFoundException(message);
//        }
//        log.info("successful run  StandardTestService.getById() ");
//        return optionalStandardTest.get();
//
//        }
//
//    @Override
//    public List<StandardTest> getAll() {
//        return (List<StandardTest>) standardTestRepository.findAll();
//
//    }
//
//@Override
//public Page<StandardTest> getAll(int page, int size){
//
//        Pageable pageable=PageRequest.of(page,size,Sort.by(
//        Sort.Order.desc("id")
//        ));
//        Page<StandardTest> standardtestPage= standardTestRepository.findAll(pageable);
//        return standardtestPage;
//
//        }
//
//@Override
//@Transactional
//public void delete(List<Long> ids){
//        log.info("start StandardTestService.delete() ");
//boolean existUpdatable=checkExistUpdatableRecord(ids);
//
//if(existUpdatable){
//
//    String message=    messageSource.getMessage("not_allowed_to_upadte_and_delete",null, Locale.ENGLISH);
//    ExceptionUtil.sendAccessDeniedException(message);
//
//}
//    deleteAllByIds(ids);
//   // standardTestRepository.deleteByIdIn(ids);
//        log.info("successful run  StandardTestService.delete() ");
//        }
//
//
//    public void deleteAllByIds(List<Long> ids) {
//        ids.forEach(id -> {
//        StandardTest lastSavedStandardTest =getById(id);
//
//            lastSavedStandardTest.setDeleted(true);
//            lastSavedStandardTest.setDeletedDate(new Date());
//            standardTestRepository.save(lastSavedStandardTest);
//
//        });
//
//    }
//
//
//    public SearchDTO.SearchRs<StandardTestStatusDTO> searchStandardTest(SearchDTO.SearchRq filter) throws InvalidDataAccessApiUsageException {
//
//    return SearchUtil.search(standardTestRepository, filter, entity -> {
//                return modelMapper.map(entity, new TypeToken<StandardTestStatusDTO>() {
//        }.getType());
//                    });
//}
//
//
//
//
//    private boolean checkExistUpdatableRecord(List<Long >ids){
//        AtomicBoolean result= new AtomicBoolean(false);
//        ids.forEach(id->{
//            StandardTest lastSavedStandardTest =getById(id);
//            if(!lastSavedStandardTest.getUpdatable()) {
//
//                result.set(true);
//                return;
//            }
//
//        });
//        return result.get();
//    }
//
//    }
//
//
//
//
//
