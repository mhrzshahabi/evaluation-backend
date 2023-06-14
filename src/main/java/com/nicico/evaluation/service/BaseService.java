package com.nicico.evaluation.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.NICICOPageable;
import com.nicico.copper.common.domain.criteria.NICICOSpecification;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import com.nicico.evaluation.dto.InstanceDTO;
import com.nicico.evaluation.iservice.IBaseService;
import com.nicico.evaluation.repository.BaseRepository;
import com.nicico.evaluation.utility.CriteriaUtil;
import com.nicico.evaluation.utility.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.nicico.evaluation.utility.CriteriaUtil.makeCriteria;


@Transactional
@RequiredArgsConstructor
public abstract class BaseService<E, ID extends Serializable, INFO, CREATE, UPDATE, DELETE, DAO extends BaseRepository<E, ID>> implements IBaseService<E, ID, INFO, CREATE, UPDATE, DELETE> {

    @Autowired
    protected ModelMapper modelMapper;
    protected DAO dao;
    protected E entity;
    private Class<E> entityType;
    private Class<INFO> infoType;
    private Class<CREATE> createType;
    private Class<UPDATE> updateType;
    private Class<DELETE> deleteType;

    {
        entityType = (Class<E>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];

        infoType = (Class<INFO>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[2];

        createType = (Class<CREATE>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[3];

        updateType = (Class<UPDATE>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[4];

        deleteType = (Class<DELETE>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[5];
    }

    BaseService(E entity, DAO dao) {
        this.entity = entity;
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<INFO> list() {
        return mapEntityToInfo(dao.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public SearchDTO.SearchRs<INFO> search(SearchDTO.SearchRq rq) {
        return SearchUtil.search(dao, rq, e -> modelMapper.map(e, infoType));
    }

    public static <E, INFO, DAO extends JpaSpecificationExecutor<E>> SearchDTO.SearchRs<INFO> optimizedSearch(DAO dao, Function<E, INFO> converter, SearchDTO.SearchRq rq) throws NoSuchFieldException, IllegalAccessException {

        SearchDTO.SearchRs<INFO> searchRs = null;

        if (rq.getStartIndex() == null) {
            searchRs = SearchUtil.search(dao, rq, converter);
        } else {
            Page<E> all = dao.findAll(NICICOSpecification.of(rq), NICICOPageable.of(rq));
            List<E> list = all.getContent();

            long totalCount = all.getTotalElements();


            if (totalCount == 0) {

                searchRs = new SearchDTO.SearchRs<>();
                searchRs.setList(new ArrayList<INFO>());
                searchRs.setTotalCount(0L);

            } else {
                List<Long> ids = new ArrayList<>();
                Field field;
                for (E e : list) {
                    field = e.getClass().getDeclaredField("id");
                    field.setAccessible(true);
                    Object value = field.get(e);
                    ids.add((Long) value);
                }

                rq.setCriteria(makeCriteria("", null, EOperator.or, null));
                List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
                int page = 0;

                while (page * 1000 < ids.size()) {
                    page++;
                    criteriaRqList.add(makeCriteria("id", ids.subList((page - 1) * 1000, Math.min((page * 1000), ids.size())), EOperator.inSet, null));

                }
                rq.setCriteria(makeCriteria("", null, EOperator.or, criteriaRqList));
                rq.setStartIndex(null);

                searchRs = SearchUtil.search(dao, rq, converter);
            }
        }
        return searchRs;
    }

    @Override
    @Transactional(readOnly = true)
    public TotalResponse<INFO> search(NICICOCriteria rq) {
        return SearchUtil.search(dao, rq, e -> modelMapper.map(e, infoType));
    }

    @Override
    @Transactional
    public Boolean isExist(ID id) {
        final Optional<E> optional = dao.findById(id);
        return optional.isPresent();
    }

    @Override
    @Transactional
    public E get(ID id) {
        final Optional<E> optional = dao.findById(id);
        return optional.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<INFO> mapEntityToInfo(List<E> eList) {
        List<INFO> infoList = new ArrayList<>();
        Optional.ofNullable(eList).ifPresent(entities -> entities.forEach(entity -> infoList.add(modelMapper.map(entity, infoType))));
        return infoList;
    }

    public static <E, EXCEL, DAO extends JpaSpecificationExecutor<E>> byte[]  exportExcel(DAO dao, Function<E, EXCEL> converter, List<FilterDTO> criteria, String sheetName, String headerName) throws NoSuchFieldException, IllegalAccessException {
        SearchDTO.SearchRq request = CriteriaUtil.ConvertCriteriaToSearchRequest(criteria, Integer.MAX_VALUE, 0);
        SearchDTO.SearchRs<EXCEL> searchRs = BaseService.optimizedSearch(dao, converter, request);
        ExcelGenerator<EXCEL> excelGenerator = new ExcelGenerator<>(searchRs.getList());
//        List<E> data = dao.findAll(null);
//        ExcelGenerator<E> excelGenerator = new ExcelGenerator<E>(data);
        excelGenerator.generateSheet(sheetName, headerName);
        return excelGenerator.getExcel().toByteArray();
    }



}
