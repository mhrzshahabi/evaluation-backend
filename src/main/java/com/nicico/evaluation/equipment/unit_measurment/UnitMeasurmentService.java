
package com.nicico.evaluation.equipment.unit_measurment;

import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.common.ExceptionUtil;
import com.nicico.evaluation.equipment.standard_test_status.*;
import com.nicico.evaluation.exception.BadRequestException;
import com.nicico.evaluation.exception.ConflictException;
import com.nicico.evaluation.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
@AllArgsConstructor
@Service
@Slf4j
public class UnitMeasurmentService implements IUnitMeasurmentService {

    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final UnitMeasurmentRepository unitMeasurmentRepository;

    @Override
    public UnitMeasurment save(UnitMeasurment unitMeasurment){
        log.info("start UnitMeasurmentService.save() ");
        UnitMeasurment savedUnitMeasurment;
    try {


        savedUnitMeasurment = unitMeasurmentRepository.save(unitMeasurment);
            log.info("successful run  UnitMeasurmentService.save() ");

        }
        catch (ConstraintViolationException ex) {
        log.error("UnitMeasurmentService.save() :" ,messageSource.getMessage("duplicate_found", null, Locale.ENGLISH));
        throw new ConflictException(messageSource.getMessage("duplicate_found", null, Locale.ENGLISH));

        }
      catch (DataIntegrityViolationException ex) {
        log.error("UnitMeasurmentService.save() :" ,messageSource.getMessage("duplicate_found", null, Locale.ENGLISH));
        throw new ConflictException(messageSource.getMessage("duplicate_found", null, Locale.ENGLISH));

        }


    catch (BadRequestException ex){
        log.error("UnitMeasurmentService.save() :" ,messageSource.getMessage("bad_request", null, Locale.ENGLISH));
        throw new BadRequestException(messageSource.getMessage("bad_request", null, Locale.ENGLISH));
      }

        return savedUnitMeasurment;

    }


@Override
public UnitMeasurment update(UnitMeasurment unitMeasurment){
    log.info("start StandardTestStatusService.update() ");

    UnitMeasurment lastUnitMeasurment =getById(unitMeasurment.getId());

    if(!lastUnitMeasurment.getUpdatable()) {
        String message=    messageSource.getMessage("not_allowed_to_upadte_and_delete",null, Locale.ENGLISH);
        ExceptionUtil.sendAccessDeniedException(message);

    }


    UnitMeasurment savedStandardTest = null;
    try{
        lastUnitMeasurment.setTitle(unitMeasurment.getTitle());
        lastUnitMeasurment.setAbreviation(unitMeasurment.getAbreviation());

        savedStandardTest = unitMeasurmentRepository.save(lastUnitMeasurment);


        log.info("successful run  UnitMeasurmentService.update() ");

        }
        catch(DataIntegrityViolationException ex){
        log.error("UnitMeasurmentService.update() :",messageSource.getMessage("duplicate_found",null,Locale.ENGLISH));
        throw new ConflictException(messageSource.getMessage("duplicate_found",null,Locale.ENGLISH));}
        catch(NullPointerException ex){
        log.error("UnitMeasurment.update() :",messageSource.getMessage("Null value is not accepted",null,Locale.ENGLISH));
        throw new BadRequestException(messageSource.getMessage("Null value is not accepted",null, Locale.ENGLISH));}
        catch (Exception e) {}


    return savedStandardTest;
}

@Override
public UnitMeasurment getById(Long unitMeasurmentId){
        log.info("start UnitMeasurmentService.getById() ");

        Optional<UnitMeasurment>  optionalStandardTest = unitMeasurmentRepository.findById(unitMeasurmentId);
        if(!optionalStandardTest.isPresent()){
            String message=messageSource.getMessage("not_found_id",null,Locale.ENGLISH);
        log.error("UnitMeasurmentService.getById() :",message);
        throw new NotFoundException(message);
        }
        log.info("successful run  StandardTestService.getById() ");
        return optionalStandardTest.get();

        }

    @Override
    public List<UnitMeasurment> getAll() {
        return (List<UnitMeasurment>) unitMeasurmentRepository.findAll();

    }

@Override
public Page<UnitMeasurment> getAll(int page, int size){

        Pageable pageable=PageRequest.of(page,size,Sort.by(
        Sort.Order.desc("id")
        ));
        Page<UnitMeasurment> standardtestPage= unitMeasurmentRepository.findAll(pageable);
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
            UnitMeasurment lastSavedUnitMeasurment =getById(id);

            lastSavedUnitMeasurment.setDeleted(true);
            lastSavedUnitMeasurment.setDeletedDate(new Date());
            unitMeasurmentRepository.save(lastSavedUnitMeasurment);

        });

    }


    public SearchDTO.SearchRs<StandardTestStatusDTO> searchStandardTest(SearchDTO.SearchRq filter) throws InvalidDataAccessApiUsageException {

    return SearchUtil.search(unitMeasurmentRepository, filter, entity -> {
                return modelMapper.map(entity, new TypeToken<StandardTestStatusDTO>() {
        }.getType());
                    });
}




    private boolean checkExistUpdatableRecord(List<Long >ids){
        AtomicBoolean result= new AtomicBoolean(false);
        ids.forEach(id->{
            UnitMeasurment lastSavedUnitMeasurment =getById(id);
            if(!lastSavedUnitMeasurment.getUpdatable()) {

                result.set(true);
                return;
            }

        });
        return result.get();
    }

    }





