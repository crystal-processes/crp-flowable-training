package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.crp.training.mapper.ExecutionContentMapper;
import org.flowable.cmmn.spring.impl.test.FlowableCmmnSpringExtension;
import org.flowable.common.engine.impl.cmd.CustomSqlExecution;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.runtime.Clock;
import org.flowable.content.api.ContentItem;
import org.flowable.content.api.ContentService;
import org.flowable.content.engine.impl.persistence.entity.ContentItemEntityImpl;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.cmd.AbstractCustomSqlExecution;
import org.flowable.engine.impl.test.JobTestHelper;
import org.flowable.engine.impl.test.TestHelper;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTestHelper;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.google.common.collect.ImmutableMap;

@SpringBootTest
@ExtendWith(FlowableSpringExtension.class)
@ActiveProfiles("test")
class QueriesTest {

	@Autowired
	RuntimeService runtimeService;
	@Autowired
	ContentService contentService;
	@Autowired
	ManagementService managementService;

	@Test
	@Deployment(resources = "org/crp/training/my-process.bpmn20.xml")
	void nativeQueryExample() {
		ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder().processDefinitionKey("my-process").businessKey("testProcess").start();
		createContentItem(processInstance.getId());

		List<Execution> executions = runtimeService.createNativeExecutionQuery().sql(
				"select execution.* from " + managementService.getTableName(Execution.class) + " execution inner join "
						+"ACT_CO_CONTENT_ITEM item on item.PROC_INST_ID_ = execution.PROC_INST_ID_  where "
						+ "execution.BUSINESS_KEY_ = 'testProcess' and item.NAME_= 'testItem'"
		).list();

		assertThat(executions).extracting(Execution::getProcessInstanceId).containsExactly(processInstance.getId());
	}

	@Test
	@Deployment(resources = "org/crp/training/my-process.bpmn20.xml")
	void myBatisCustomQuery() {
		ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder().processDefinitionKey("my-process").businessKey("testProcess").start();
		String contentItem = createContentItem(processInstance.getId());

		List<Map<String, String>> executionsWithContent = managementService.executeCustomSql(
				new AbstractCustomSqlExecution<ExecutionContentMapper, List<Map<String, String>>>(ExecutionContentMapper.class) {

					@Override
					public List<Map<String, String>> execute(ExecutionContentMapper executionContentMapper) {
						return executionContentMapper.getExecutionWithContentByBusinessKeyAndName("testProcess", "testItem");
					}
				});

		assertThat(executionsWithContent).hasSize(1).contains(ImmutableMap.of("EXECID", processInstance.getId(), "ITEMID", contentItem));
	}

	private String createContentItem(String processInstanceId) {
		ContentItem contentItem = contentService.newContentItem();
		contentItem.setName("testItem");
		contentItem.setMimeType("application/pdf");
		contentItem.setProcessInstanceId(processInstanceId);
		contentService.saveContentItem(contentItem);
		return contentItem.getId();
	}

}
