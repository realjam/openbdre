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

package com.wipro.ats.bdre.md.api;

import com.wipro.ats.bdre.exception.MetadataException;
import com.wipro.ats.bdre.md.api.base.MetadataAPIBase;
import com.wipro.ats.bdre.md.beans.ProcessDependencyInfo;
import com.wipro.ats.bdre.md.dao.ProcessDependencyDAO;
import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * This class gets list of Upstream and Downstream processes of a particular process.
 */
public class GetProcessDependency extends MetadataAPIBase {
    public GetProcessDependency() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-dao.xml");
        AutowireCapableBeanFactory acbFactory = context.getAutowireCapableBeanFactory();
        acbFactory.autowireBean(this);
    }

    private static final Logger LOGGER = Logger.getLogger(GetProcessDependency.class);

    private static final String[][] PARAMS_STRUCTURE = {
            {"p", "process-id", " Process id of the process to get it's immediate Upstream and Downstream Processes"},
    };

    /**
     * This method gets list of Upstream and Downstream processes of a particular process.
     *
     * @param params String array having process-id and  environment with their
     * command line notations.
     * @return This method returns list of Upstream and Downstream processes of a particular process.
     */
    @Autowired
    private ProcessDependencyDAO processDependencyDAO;

    public List<ProcessDependencyInfo> execute(String[] params) {
        List<ProcessDependencyInfo> udList;
        try {
            ProcessDependencyInfo processDependencyInfo = new ProcessDependencyInfo();
            CommandLine commandLine = getCommandLine(params, PARAMS_STRUCTURE);
            String pid = commandLine.getOptionValue("process-id");
            LOGGER.debug("process-id  is " + pid);

            //calling proc ListUD
//            udList = s.selectList("call_procedures.GetListUD", processDependencyInfo);
            udList = processDependencyDAO.listUD(Integer.parseInt(pid));
            LOGGER.debug("Details of process is\n" + udList);
            return udList;
        } catch (Exception e) {
            LOGGER.error("Error occurred", e);
            throw new MetadataException(e);
        }
    }

}

