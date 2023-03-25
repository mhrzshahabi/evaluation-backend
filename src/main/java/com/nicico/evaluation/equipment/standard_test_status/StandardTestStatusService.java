
package com.nicico.evaluation.equipment.standard_test_status;

import com.nicico.evaluation.common.ExceptionUtil;
import com.nicico.evaluation.exception.BadRequestException;
import com.nicico.evaluation.exception.ConflictException;
import com.nicico.evaluation.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.search.SearchDTO;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
@AllArgsConstructor
@Service
@Slf4j
public class StandardTestStatusService implements IStandardTestStatusService {

    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final StandardTestStatusRepository standardTestStatusRepository;


    @Override
    public StandardTestStatus save(StandardTestStatus standardtest){
        log.info("start StandardTestService.save() ");
        StandardTestStatus savedStandardTest;
    try {


        savedStandardTest = standardTestStatusRepository.save(standardtest);
            log.info("successful run  StandardTestStatusService.save() ");

        }
        catch (ConstraintViolationException ex) {
        log.error("StandardTestStatusService.save() :" ,messageSource.getMessage("duplicate_found", null, Locale.ENGLISH));
        throw new ConflictException(messageSource.getMessage("duplicate_found", null, Locale.ENGLISH));

        }
      catch (DataIntegrityViolationException ex) {
        log.error("StandardTestStatysService.save() :" ,messageSource.getMessage("duplicate_found", null, Locale.ENGLISH));
        throw new ConflictException(messageSource.getMessage("duplicate_found", null, Locale.ENGLISH));

        }


    catch (BadRequestException ex){
        log.error("StandardTestStatusService.save() :" ,messageSource.getMessage("bad_request", null, Locale.ENGLISH));
        throw new BadRequestException(messageSource.getMessage("bad_request", null, Locale.ENGLISH));
      }

        return savedStandardTest;

    }


@Override
public StandardTestStatus update(StandardTestStatus standardtest){
    log.info("start StandardTestStatusService.update() ");

    StandardTestStatus lastSavedStandardTest =getById(standardtest.getId());

    if(!lastSavedStandardTest.getUpdatable()) {
        String message=    messageSource.getMessage("not_allowed_to_upadte_and_delete",null, Locale.ENGLISH);
        ExceptionUtil.sendAccessDeniedException(message);

    }


    StandardTestStatus savedStandardTest = null;
    try{
            lastSavedStandardTest.setTitle(standardtest.getTitle());
            lastSavedStandardTest.setDescription(standardtest.getDescription());
            lastSavedStandardTest.setTitleColor(standardtest.getTitleColor());

        savedStandardTest = standardTestStatusRepository.save(lastSavedStandardTest);


        log.info("successful run  StandardTestService.update() ");

        }
        catch(DataIntegrityViolationException ex){
        log.error("StandardTestStatusService.update() :",messageSource.getMessage("duplicate_found",null,Locale.ENGLISH));
        throw new ConflictException(messageSource.getMessage("duplicate_found",null,Locale.ENGLISH));}
        catch(NullPointerException ex){
        log.error("StandardTestStatusService.update() :",messageSource.getMessage("Null value is not accepted",null,Locale.ENGLISH));
        throw new BadRequestException(messageSource.getMessage("Null value is not accepted",null, Locale.ENGLISH));}
        catch (Exception e) {}


    return savedStandardTest;
}

@Override
public StandardTestStatus getById(Long standardtestId){
        log.info("start StandardTestService.getById() ");

        Optional<StandardTestStatus>  optionalStandardTest = standardTestStatusRepository.findById(standardtestId);
        if(!optionalStandardTest.isPresent()){
            String message=messageSource.getMessage("not_found_id",null,Locale.ENGLISH);
        log.error("StandardTestService.getById() :",message);
        throw new NotFoundException(message);
        }
        log.info("successful run  StandardTestService.getById() ");
        return optionalStandardTest.get();

        }

    @Override
    public List<StandardTestStatus> getAll() {
        return (List<StandardTestStatus>) standardTestStatusRepository.findAll();

    }

@Override
public Page<StandardTestStatus> getAll(int page, int size){

        Pageable pageable=PageRequest.of(page,size,Sort.by(
        Sort.Order.desc("id")
        ));
        Page<StandardTestStatus> standardtestPage= standardTestStatusRepository.findAll(pageable);
        return standardtestPage;

        }

@Override
@Transactional
public void delete(List<Long> ids){
        log.info("start StandardTestService.delete() ");
boolean existUpdatable=checkExistUpdatableRecord(ids);

if(existUpdatable){
    String message=    messageSource.getMessage("not_allowed_to_upadte_and_delete",null, Locale.ENGLISH);
    ExceptionUtil.sendAccessDeniedException(message);
}
    deleteAllByIds(ids);
   // standardTestRepository.deleteByIdIn(ids);
        log.info("successful run  StandardTestService.delete() ");
        }


    public void deleteAllByIds(List<Long> ids) {
        ids.forEach(id -> {
        StandardTestStatus lastSavedStandardTest =getById(id);

            lastSavedStandardTest.setDeleted(true);
            lastSavedStandardTest.setDeletedDate(new Date());
            standardTestStatusRepository.save(lastSavedStandardTest);

        });

    }


    public SearchDTO.SearchRs<StandardTestStatusDTO> searchStandardTest(SearchDTO.SearchRq filter) throws InvalidDataAccessApiUsageException {

    return SearchUtil.search(standardTestStatusRepository, filter, entity -> {
                return modelMapper.map(entity, new TypeToken<StandardTestStatusDTO>() {
        }.getType());
                    });
}





    private boolean checkExistUpdatableRecord(List<Long >ids){
        AtomicBoolean result= new AtomicBoolean(false);
        ids.forEach(id->{
            StandardTestStatus lastSavedStandardTest =getById(id);
            if(!lastSavedStandardTest.getUpdatable()) {

                result.set(true);
                return;
            }

        });
        return result.get();
    }

    }





