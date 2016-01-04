/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wipro.ats.bdre.md.rest.ext;

import com.wipro.ats.bdre.md.api.base.MetadataAPIBase;
import com.wipro.ats.bdre.md.beans.table.GeneralConfig;
import com.wipro.ats.bdre.md.beans.table.Process;
import com.wipro.ats.bdre.md.beans.table.Properties;
import com.wipro.ats.bdre.md.dao.ProcessDAO;
import com.wipro.ats.bdre.md.dao.PropertiesDAO;
import com.wipro.ats.bdre.md.dao.jpa.BusDomain;
import com.wipro.ats.bdre.md.dao.jpa.ProcessTemplate;
import com.wipro.ats.bdre.md.dao.jpa.PropertiesId;
import com.wipro.ats.bdre.md.dao.jpa.WorkflowType;
import com.wipro.ats.bdre.md.rest.RestWrapper;
import com.wipro.ats.bdre.md.rest.util.Dao2TableUtil;
import com.wipro.ats.bdre.md.rest.util.DateConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by KA294215 on 11-09-2015.
 */

@Controller
@RequestMapping("/flumeproperties")
public class FlumePropertiesAPI extends MetadataAPIBase {
    private static final Logger LOGGER = Logger.getLogger(FlumePropertiesAPI.class);
    @Autowired
    private ProcessDAO processDAO;
    @Autowired
    private PropertiesDAO propertiesDAO;

    @RequestMapping(value = {"/createjobs"}, method = RequestMethod.POST)

    public
    @ResponseBody
    RestWrapper list(@RequestParam Map<String, String> map, Principal principal) {
        LOGGER.debug(" value of map is " + map.size());
        RestWrapper restWrapper = null;

        /* com.wipro.ats.bdre.md.beans.table.Process parentProcess = new Process();
        Process childProcess = new Process();
        parentProcess = insertProcess(1, null, "flume action Parent", "Data Ingestion Parent", 2, principal);
        childProcess = insertProcess(23, parentProcess.getProcessId(), "flume action", "flume action", 0, principal);  */
        com.wipro.ats.bdre.md.dao.jpa.Process parentProcess = Dao2TableUtil.buildJPAProcess(1, "flume action Parent", "Data Ingestion Parent", 2);
        com.wipro.ats.bdre.md.dao.jpa.Process childProcess = Dao2TableUtil.buildJPAProcess(23, "child of " + "flume action", "child of " + "flume action", 0);
        List<com.wipro.ats.bdre.md.dao.jpa.Properties> childProps=new ArrayList<>();
        com.wipro.ats.bdre.md.dao.jpa.Properties jpaProperties;

        for (String string : map.keySet()) {
            LOGGER.debug("String is" + string);
            if (map.get(string) == null || ("").equals(map.get(string))) {
                continue;
            }
            Integer splitIndex = string.lastIndexOf("_");
            String key = string.substring(splitIndex + 1, string.length());
            LOGGER.debug("key is " + key);
            if (string.startsWith("source_")) {
                jpaProperties = Dao2TableUtil.buildJPAProperties(childProcess.getProcessId(), "flume", "agent" + parentProcess.getProcessId() + ".sources.source" + childProcess.getProcessId() + "." + key, map.get(string), "Properties for source");
                childProps.add(jpaProperties);
//                insertProperties(childProcess.getProcessId(), "agent" + parentProcess.getProcessId() + ".sources.source" + childProcess.getProcessId() + "." + key, map.get(string), "Properties for source");
            } else if (string.startsWith("channel_")) {
                jpaProperties = Dao2TableUtil.buildJPAProperties(childProcess.getProcessId(), "flume", "agent" + parentProcess.getProcessId() + ".channels.channel" + childProcess.getProcessId() + "." + key, map.get(string), "Properties for channel");
                childProps.add(jpaProperties);
//                insertProperties(childProcess.getProcessId(), "agent" + parentProcess.getProcessId() + ".channels.channel" + childProcess.getProcessId() + "." + key, map.get(string), "Properties for channel");
            } else if (string.startsWith("sink_")) {
                jpaProperties = Dao2TableUtil.buildJPAProperties(childProcess.getProcessId(), "flume", "agent" + parentProcess.getProcessId() + ".sinks.sink" + childProcess.getProcessId() + "." + key, map.get(string), "Properties for sink");
                childProps.add(jpaProperties);
//                insertProperties(childProcess.getProcessId(), "agent" + parentProcess.getProcessId() + ".sinks.sink" + childProcess.getProcessId() + "." + key, map.get(string), "Properties for sink");
            }
        }

        jpaProperties = Dao2TableUtil.buildJPAProperties(childProcess.getProcessId(), "flume", "agent" + parentProcess.getProcessId() + ".sources", "source" + childProcess.getProcessId(), "Source name");
        childProps.add(jpaProperties);
        jpaProperties = Dao2TableUtil.buildJPAProperties(childProcess.getProcessId(), "flume", "agent" + parentProcess.getProcessId() + ".sinks", "sink" + childProcess.getProcessId(), "Sink name");
        childProps.add(jpaProperties);
        jpaProperties = Dao2TableUtil.buildJPAProperties(childProcess.getProcessId(),"flume",  "agent" + parentProcess.getProcessId() + ".channels", "channel" + childProcess.getProcessId(), "Channel name");
        childProps.add(jpaProperties);
        jpaProperties = Dao2TableUtil.buildJPAProperties(childProcess.getProcessId(), "flume", "agent" + parentProcess.getProcessId() + ".sources.source" + childProcess.getProcessId() + ".channels", "channel" + childProcess.getProcessId(), "Channel name for source");
        childProps.add(jpaProperties);
        jpaProperties = Dao2TableUtil.buildJPAProperties(childProcess.getProcessId(), "flume", "agent" + parentProcess.getProcessId() + ".sinks.sink" + childProcess.getProcessId() + ".channel", "channel" + childProcess.getProcessId(), "Channel name for sink");
        childProps.add(jpaProperties);
        jpaProperties = Dao2TableUtil.buildJPAProperties(childProcess.getProcessId(),"flume",  "agent" + parentProcess.getProcessId() + ".sinks.sink" + childProcess.getProcessId() + ".hdfs.processId", parentProcess.getProcessId().toString(), " Parent Process Id");
        childProps.add(jpaProperties);

        /* insertProperties(childProcess.getProcessId(), "agent" + parentProcess.getProcessId() + ".sources", "source" + childProcess.getProcessId(), "Source name");
        insertProperties(childProcess.getProcessId(), "agent" + parentProcess.getProcessId() + ".sinks", "sink" + childProcess.getProcessId(), "Sink name");
        insertProperties(childProcess.getProcessId(), "agent" + parentProcess.getProcessId() + ".channels", "channel" + childProcess.getProcessId(), "Channel name");
        insertProperties(childProcess.getProcessId(), "agent" + parentProcess.getProcessId() + ".sources.source" + childProcess.getProcessId() + ".channels", "channel" + childProcess.getProcessId(), "Channel name for source");
        insertProperties(childProcess.getProcessId(), "agent" + parentProcess.getProcessId() + ".sinks.sink" + childProcess.getProcessId() + ".channel", "channel" + childProcess.getProcessId(), "Channel name for sink");
        insertProperties(childProcess.getProcessId(), "agent" + parentProcess.getProcessId() + ".sinks.sink" + childProcess.getProcessId() + ".hdfs.processId", parentProcess.getProcessId().toString(), " Parent Process Id");  */

        List<com.wipro.ats.bdre.md.dao.jpa.Process> processList = processDAO.createOneChildJob(parentProcess,childProcess,null,childProps);
        List<Process> tableProcessList = Dao2TableUtil.jpaList2TableProcessList(processList);
        Integer counter = tableProcessList.size();
        for (Process process:tableProcessList) {
            process.setCounter(counter);
            process.setTableAddTS(DateConverter.dateToString(process.getAddTS()));
            process.setTableEditTS(DateConverter.dateToString(process.getEditTS()));
        }
        restWrapper = new RestWrapper(tableProcessList, RestWrapper.OK);
        return restWrapper;
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.PUT)
    public
    @ResponseBody
    RestWrapper insert(@ModelAttribute("generalconfig")
                       @Valid GeneralConfig generalConfig, BindingResult bindingResult, Principal principal) {
        LOGGER.debug("Updating jtable for new advanced config");


        RestWrapper restWrapper = null;
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder("<p>Please fix following errors and try again<p><ul>");
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessages.append("<li>");
                errorMessages.append(error.getField());
                errorMessages.append(". Bad value: '");
                errorMessages.append(error.getRejectedValue());
                errorMessages.append("'</li>");
            }
            errorMessages.append("</ul>");
            restWrapper = new RestWrapper(errorMessages.toString(), RestWrapper.ERROR);
            return restWrapper;
        }

        try {

            restWrapper = new RestWrapper(generalConfig, RestWrapper.OK);
            LOGGER.info("Record with configGroup:" + generalConfig.getConfigGroup() + " inserted in Jtable by User:" + principal.getName() + generalConfig);
        } catch (Exception e) {
            restWrapper = new RestWrapper(e.getMessage(), RestWrapper.ERROR);
        }
        return restWrapper;
    }


    @Override
    public Object execute(String[] params) {
        return null;
    }
}
