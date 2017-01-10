/*
 * Copyright 2016 Damien Ferrand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package xyz.iipster;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Ibm i auto configuration.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
@Configuration
@ConditionalOnClass(AS400.class)
@EnableConfigurationProperties(IbmiInformation.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class IbmiAutoConfiguration {
    private final IbmiInformation ibmiInformation;

    @Autowired
    public IbmiAutoConfiguration(IbmiInformation ibmiInformation) {
        this.ibmiInformation = ibmiInformation;
    }

    @Bean
    @ConditionalOnMissingBean(AS400.class)
    public AS400 as400() {
        // Can't specify *CURRENT only for user or password, it has to be for both
        if ("*CURRENT".equals(ibmiInformation.getUser()) && !"*CURRENT".equals(ibmiInformation.getPassword()) ||
                !"*CURRENT".equals(ibmiInformation.getUser()) && "*CURRENT".equals(ibmiInformation.getPassword())) {
            throw new RuntimeException("*CURRENT has to be specified for both user and password.");
        }

        // If username and password are *CURRENT
        if ("*CURRENT".equals(ibmiInformation.getUser()) && "*CURRENT".equals(ibmiInformation.getPassword())) {
            // If not on IBM i, we can't use *CURRENT
            if (!System.getProperty("os.name").equals("OS/400")) {
                throw new RuntimeException("*CURRENT can only be used when running on IBM i");
            }
        }
        return new AS400(ibmiInformation.getAddress(), ibmiInformation.getUser(), ibmiInformation.getPassword());
    }

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    @ConditionalOnBean(AS400.class)
    public DataSource as400JDBCDataSource(AS400 as400, ConfigurableEnvironment configurableEnvironment) {
        final Properties properties = new Properties();
        properties.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.DB2400Dialect");
        PropertiesPropertySource pps = new PropertiesPropertySource("iipster", properties);
        configurableEnvironment.getPropertySources().addLast(pps);
        final AS400JDBCDataSource retVal = new AS400JDBCDataSource(as400);
        retVal.setAutoCommit(false);
        retVal.setNaming("system");
        retVal.setTranslateBinary(true);
        return retVal;
    }
}
