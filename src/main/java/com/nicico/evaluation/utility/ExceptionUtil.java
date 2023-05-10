package com.nicico.evaluation.utility;

import com.nicico.evaluation.enums.EntityManagement;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExceptionUtil {

    private final EntityManager entityManager;

    /**
     * @param exception arising when delete an record before deleting its children
     * @return hashmap containing table name and column id
     */
    private Map<String, String> getTableAndColumnCausedException(DataIntegrityViolationException exception) {
        Map<String, String> result = new HashMap<>();
        String constraintName = com.nicico.copper.base.ExceptionUtil.findDbConstraintName(exception).orElse(null);

        if (constraintName == null) {
            return null;
        }

        constraintName = constraintName.replaceAll("\"", "");

        String query = String.format(
                """
                        SELECT
                            a.table_name,
                            a.column_name
                        FROM
                               all_cons_columns   a
                            -- JOIN all_constraints    c ON a.owner = c.owner
                            -- AND a.constraint_name = c.constraint_name
                            -- JOIN all_constraints    c_pk ON c.r_owner = c_pk.owner
                            -- AND c.r_constraint_name = c_pk.constraint_name
                        WHERE
                            a.constraint_name = '%s'
                            """
                , constraintName);

        List<?> resultList = entityManager.createNativeQuery(query).getResultList();

        if (resultList == null || resultList.isEmpty()) {
            return null;
        }

        Object[] columns = (Object[]) resultList.get(0);

        result.put("tableName", columns[0].toString());
        result.put("parentColumnName", columns[1].toString());

        return result;
    }

    /**
     * @param exception arising when delete an record before deleting its children
     * @return hashmap containing table name, column id and column name
     */
    private Map<String, String> getTableAndColumnTitleAndRecordId(DataIntegrityViolationException exception) {
        Map<String, String> tableAndColumnCausedException = getTableAndColumnCausedException(exception);

        if (tableAndColumnCausedException == null) {
            return null;
        }

        String tableName = tableAndColumnCausedException.get("tableName");

        String query = String.format(
                """
                        SELECT
                             column_name
                         FROM
                             all_tab_columns
                         WHERE
                             table_name = '%s'
                             AND column_name LIKE (
                                 CASE
                                     WHEN column_name LIKE '%%TITLE%%' THEN
                                         '%%TITLE%%'
                                     WHEN column_name LIKE '%%CODE%%' THEN
                                         '%%CODE%%'
                                     WHEN column_name LIKE 'ID' THEN
                                         'ID'
                                 END
                             )
                                    """,
                tableName
        );

        List<?> resultList = entityManager.createNativeQuery(query).getResultList();

        String columnTitle = null;

        if (resultList != null && !resultList.isEmpty()) {
            if (resultList.size() == 1) {
                columnTitle = resultList.get(0).toString();
            } else {
                columnTitle = resultList.stream().map(Object::toString).filter(s -> s.contains("_title".toUpperCase()) || s.contains("_code".toUpperCase()) || s.contains("_id".toUpperCase())).findFirst().orElse("id");
            }
        }

        tableAndColumnCausedException.put("columnName", columnTitle);

        return tableAndColumnCausedException;
    }

    /**
     * @param exception arising when delete an record before deleting its children
     * @param parentId  is as foreign key in some tables
     * @return list of records from the tables that have parentId as foreign key
     */
    public String getRecordsByParentId(DataIntegrityViolationException exception, Long parentId) {
        Map<String, String> result = getTableAndColumnTitleAndRecordId(exception);

        if (result == null) {
            return null;
        }

        String tableName = result.get("tableName");
        String parentColumnName = result.get("parentColumnName");
        String columnName = result.get("columnName");

        String query = String.format(
                """
                            SELECT
                                %s
                            FROM
                                %s
                            WHERE
                                %s = %s
                        """
                , columnName, tableName, parentColumnName, parentId);

        List<?> resultList = entityManager.createNativeQuery(query).getResultList();

        if (resultList == null || resultList.isEmpty()) {
            return null;
        }

        List<String> titlesOrCodes = new ArrayList<>(
                resultList.stream().map(Object::toString).toList()
        );

        StringBuilder sb = new StringBuilder();

        sb.append(" این آیتم با ");
        sb.append(resultList.size());
        sb.append(" مورد از بخش (");
        try {
            sb.append(EntityManagement.valueOf(tableName).getTitle());
        } catch (IllegalArgumentException e) {
            sb.append("نامشخص");
        }
        sb.append(") ");
        sb.append("با عناوین/کد/آی دی (");
        titlesOrCodes.forEach(titleOrCode -> sb.append(titleOrCode).append("، "));
        sb.deleteCharAt(sb.lastIndexOf("،"));
        sb.deleteCharAt(sb.lastIndexOf(" "));
        sb.append(")");
        sb.append(" وابستگی دارد و امکان حذف آن وجود ندارد.");

        return sb.toString();
    }


}
