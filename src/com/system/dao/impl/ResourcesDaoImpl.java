/**
 * 
 */
package com.system.dao.impl;

import org.springframework.stereotype.Component;

import com.parkbobo.dao.impl.BaseDaoSupport;
import com.system.dao.ResourcesDao;
import com.system.model.Resources;

/**
 * @author LH
 *
 */
@Component("resourcesDaoImpl")
public class ResourcesDaoImpl extends BaseDaoSupport<Resources> implements
		ResourcesDao {

}
