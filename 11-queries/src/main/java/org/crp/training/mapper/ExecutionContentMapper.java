package org.crp.training.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ExecutionContentMapper {

    @Select("select execution.id_ as execId, item.id_ as itemId  from ACT_RU_EXECUTION execution inner join "
            + "ACT_CO_CONTENT_ITEM item on item.PROC_INST_ID_ = execution.PROC_INST_ID_  where \n"
            + "execution.BUSINESS_KEY_ = '${businessKey}' and item.NAME_= '${itemName}'")
    List<Map<String, String>> getExecutionWithContentByBusinessKeyAndName(@Param("businessKey") String businessKey, @Param("itemName")String itemName);

}
