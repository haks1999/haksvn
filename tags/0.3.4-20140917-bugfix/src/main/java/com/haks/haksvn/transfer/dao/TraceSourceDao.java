package com.haks.haksvn.transfer.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.transfer.model.TraceSourceForTransfer;
import com.haks.haksvn.transfer.model.TransferSource;

@Repository
public class TraceSourceDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	// group by 되어 일단 path 만 나온다. sourceTrace autocomplete 용
	@SuppressWarnings("unchecked")
	public List<String> retrieveTransferSourcePathListByPath(String repositoryKey, String path, int limit){
		Session session = sessionFactory.getCurrentSession();
		
		Criteria crit = session.createCriteria(TransferSource.class,"t_transferSource")
				.createAlias("t_transferSource.transfer", "t_transfer")
				.add(Restrictions.eq("t_transfer.repositoryKey", repositoryKey))
				.add(Restrictions.like("t_transferSource.path", "%"+path+"%").ignoreCase())
				.setMaxResults( limit )
				.addOrder(Order.asc("path"));
		ProjectionList proj = Projections.projectionList();
		proj.add(Projections.groupProperty("path"));
		crit.setProjection(proj);
		return (List<String>)crit.list();
	}
	
	//trace source 조회 용
	public List<TraceSourceForTransfer> retrieveTraceSourceListByPath(String repositoryKey, String path, int limit){
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createSQLQuery(
						"SELECT tr.transfer_seq as \"transferSeq\", "
								+ " tg.transfer_group_seq as \"transferGroupSeq\", "
								+ " ts.transfer_source_seq as \"transferSourceSeq\", "
								+ " tg.transfer_date as \"transferDate\", "
								+ " ts.revision as \"trunkRevision\", "
								+ " tr.revision as \"branchesRevision\", "
								+ " ts.path as \"trunkPath\", "
								+ " ts.dest_path as \"branchesPath\" "  +
						" FROM transfer_group tg " + 
						" INNER JOIN transfer tr on (tg.transfer_group_seq = tr.transfer_group_seq) " + 
						" INNER JOIN transfer_source ts on (tr.transfer_seq = ts.transfer_seq) " + 
						" WHERE transfer_group_state  = :transferGroupState " +
						" AND tg.repository_key = :repositoryKey" +
						" AND ts.path like '%'||:path||'%' " + 
						" ORDER BY tg.transfer_date desc" )
						.addScalar("transferSeq", StandardBasicTypes.INTEGER )
						.addScalar("transferGroupSeq", StandardBasicTypes.INTEGER )
						.addScalar("transferSourceSeq", StandardBasicTypes.INTEGER )
						.addScalar("transferDate", StandardBasicTypes.LONG )
						.addScalar("trunkRevision", StandardBasicTypes.LONG )
						.addScalar("branchesRevision", StandardBasicTypes.LONG )
						.addScalar("trunkPath", StandardBasicTypes.STRING )
						.addScalar("branchesPath", StandardBasicTypes.STRING )
						.setMaxResults(limit)
						.setResultTransformer(Transformers.aliasToBean(TraceSourceForTransfer.class));
		query.setString("transferGroupState", CodeUtils.getTransferGroupTransferedCodeId());
		query.setString("repositoryKey", repositoryKey);
		query.setString("path", path);
		@SuppressWarnings("unchecked")
		List<TraceSourceForTransfer> result = (List<TraceSourceForTransfer>)query.list();
		return result;

	}
}
