package neatlogic.module.event.notify.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.constvalue.Expression;
import neatlogic.framework.common.constvalue.ParamType;
import neatlogic.framework.condition.core.ConditionHandlerFactory;
import neatlogic.framework.condition.core.IConditionHandler;
import neatlogic.framework.dto.ConditionParamVo;
import neatlogic.framework.dto.ExpressionVo;
import neatlogic.framework.event.constvalue.EventProcessStepHandlerType;
import neatlogic.framework.form.constvalue.FormConditionModel;
import neatlogic.framework.notify.core.NotifyPolicyHandlerBase;
import neatlogic.framework.notify.dto.NotifyTriggerVo;
import neatlogic.framework.process.auth.PROCESS_MODIFY;
import neatlogic.framework.process.constvalue.ConditionProcessTaskOptions;
import neatlogic.framework.process.constvalue.ProcessTaskGroupSearch;
import neatlogic.framework.process.constvalue.ProcessUserType;
import neatlogic.framework.process.notify.constvalue.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: EventNotifyPolicyHandler
 * @Package neatlogic.module.event.notify.handler
 * @Description: 事件节点通知策略处理器
 * @Author: linbq
 * @Date: 2021/3/8 11:11

 **/
@Component
public class EventNotifyPolicyHandler extends NotifyPolicyHandlerBase {
    @Override
    public String getName() {
        return EventProcessStepHandlerType.EVENT.getName();
    }

    /**
     * 绑定权限，每种handler对应不同的权限
     */
    @Override
    public String getAuthName() {
        return PROCESS_MODIFY.class.getSimpleName();
    }

//    @Override
//    public INotifyPolicyHandlerGroup getGroup() {
//        return ProcessNotifyPolicyHandlerGroup.TASKSTEP;
//    }

    @Override
    protected List<NotifyTriggerVo> myNotifyTriggerList() {
        List<NotifyTriggerVo> returnList = new ArrayList<>();
        for (ProcessTaskStepNotifyTriggerType notifyTriggerType : ProcessTaskStepNotifyTriggerType.values()) {
            returnList.add(new NotifyTriggerVo(notifyTriggerType));
        }
        //任务
        for (ProcessTaskStepTaskNotifyTriggerType notifyTriggerType : ProcessTaskStepTaskNotifyTriggerType.values()) {
            returnList.add(new NotifyTriggerVo(notifyTriggerType));
        }
        return returnList;
    }

    @Override
    protected List<ConditionParamVo> mySystemParamList() {
        List<ConditionParamVo> notifyPolicyParamList = new ArrayList<>();
        for(ProcessTaskNotifyParam param : ProcessTaskNotifyParam.values()) {
            notifyPolicyParamList.add(createConditionParam(param));
        }
        for(ProcessTaskStepNotifyParam param : ProcessTaskStepNotifyParam.values()) {
            notifyPolicyParamList.add(createConditionParam(param));
        }
        for(ProcessTaskStepTaskNotifyParam param : ProcessTaskStepTaskNotifyParam.values()) {
            notifyPolicyParamList.add(createConditionParam(param));
        }
        return notifyPolicyParamList;
    }

    @Override
    protected List<ConditionParamVo> mySystemConditionOptionList() {
        List<ConditionParamVo> notifyPolicyParamList = new ArrayList<>();
        for (ConditionProcessTaskOptions option : ConditionProcessTaskOptions.values()) {
            IConditionHandler condition = ConditionHandlerFactory.getHandler(option.getValue());
            if (condition != null) {
                ConditionParamVo param = new ConditionParamVo();
                param.setName(condition.getName());
                param.setLabel(condition.getDisplayName());
                param.setController(condition.getHandler(FormConditionModel.CUSTOM));
                if (condition.getConfig() != null) {
                    param.setConfig(condition.getConfig().toJSONString());
                }
                param.setType(condition.getType());
                ParamType paramType = condition.getParamType();
                if (paramType != null) {
                    param.setParamType(paramType.getName());
                    param.setParamTypeName(paramType.getText());
                    param.setDefaultExpression(paramType.getDefaultExpression().getExpression());
                    for (Expression expression : paramType.getExpressionList()) {
                        param.getExpressionList().add(new ExpressionVo(expression.getExpression(), expression.getExpressionName(), expression.getIsShowConditionValue()));
                    }
                }

                param.setIsEditable(0);
                notifyPolicyParamList.add(param);
            }
        }
        return notifyPolicyParamList;
    }

    @Override
    protected void myAuthorityConfig(JSONObject config) {
        List<String> groupList = JSON.parseArray(config.getJSONArray("groupList").toJSONString(), String.class);
        groupList.add(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue());
        config.put("groupList", groupList);
        List<String> includeList = JSON.parseArray(config.getJSONArray("includeList").toJSONString(), String.class);
        includeList.add(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.DEFAULT_WORKER.getValue());
        includeList.add(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.FOCUS_USER.getValue());
        config.put("includeList", includeList);
    }
}
