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
import com.wipro.ats.bdre.md.beans.HaltDeployInfo;
import com.wipro.ats.bdre.md.dao.DeployDAO;
import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by MI294210 on 9/1/2015.
 */
public class HaltDeploy extends MetadataAPIBase {
    public HaltDeploy() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-dao.xml");
        AutowireCapableBeanFactory acbFactory = context.getAutowireCapableBeanFactory();
        acbFactory.autowireBean(this);
    }

    private static final Logger LOGGER = Logger.getLogger(HaltDeploy.class);
    private static final String[][] PARAMS_STRUCTURE = {
            {"d", "deployment-id", "Deployment id of the job"}
    };

    @Autowired
    DeployDAO deployDAO;

    public HaltDeployInfo execute(String[] params) {

        try {
            HaltDeployInfo haltDeployInfo = new HaltDeployInfo();
            CommandLine commandLine = getCommandLine(params, PARAMS_STRUCTURE);
            String deployId = commandLine.getOptionValue("deployment-id");
            LOGGER.debug("deploymentId is " + deployId);

            haltDeployInfo.setDeploymentId(Integer.parseInt(deployId));
            // s.selectOne("call_procedures.HaltDeploy", haltDeployInfo);
            deployDAO.haltDeploy(Long.valueOf(deployId));
            return haltDeployInfo;
        } catch (Exception e) {
            LOGGER.error("Error occurred", e);
            throw new MetadataException(e);
        }
    }
}

