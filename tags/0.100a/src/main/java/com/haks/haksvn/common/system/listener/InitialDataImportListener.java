package com.haks.haksvn.common.system.listener;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptStatementFailedException;
import org.springframework.stereotype.Component;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.property.model.Property;
import com.haks.haksvn.common.property.service.PropertyService;
import com.haks.haksvn.common.property.util.PropertyUtils;
import com.trilead.ssh2.log.Logger;

@SuppressWarnings("rawtypes")
@Component
public class InitialDataImportListener implements ApplicationListener{

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PropertyService propertyService;
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			
			Property applicationVersionProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getApplicationVersionKey());
			
			if( applicationVersionProp != null && applicationVersionProp.getPropertyValue().equals(System.getProperty("application.version"))) return;
			
			try{
				//TODO 단순 스크립트 실행이 아닌 업그레이드가 가능하도록 변경 필요
				final ResourceDatabasePopulator rdp = new ResourceDatabasePopulator();
				rdp.addScript(new ClassPathResource("init.sql"));
				rdp.populate(dataSource.getConnection());
			}catch(ScriptStatementFailedException e){
				Logger.getLogger(this.getClass()).log(0, "Installation...this message appears only first time.");
			}catch(Exception e){
				e.printStackTrace();
				throw new HaksvnException("initial data import failed");
			}
			
		}
	}

}
