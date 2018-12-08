package com.colossus.common.eclipselink;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.internal.databaseaccess.Accessor;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.sequencing.Sequence;
import org.eclipse.persistence.sessions.Session;

import java.util.UUID;
import java.util.Vector;

/**
* 自定义主键生成策略
* @author chendilin
* @date 2016年8月8日
*/
public class UUIDSequence extends Sequence implements SessionCustomizer{

	private static final long serialVersionUID = 1L;

	public UUIDSequence() {
		super();
	}
 
	public UUIDSequence(String name) {
		super(name);
	}
 
	@Override
	public Object getGeneratedValue(Accessor accessor,
			AbstractSession writeSession, String seqName) {
		
		return UUID.randomUUID().toString().replace("-", "");
	}
 
	@Override
	public boolean shouldUseTransaction() {
		return false;
	}
 
	@Override
	public boolean shouldUsePreallocation() {
		return false;
	}
 
	public void customize(Session session) throws Exception {
		UUIDSequence sequence = new UUIDSequence("system-uuid");
 
		session.getLogin().addSequence(sequence);
	}

	@Override
	public void onConnect() {
	}

	@Override
	public void onDisconnect() {
	}

	@Override
	public boolean shouldAcquireValueAfterInsert() {
		return false;
	}

	@Override
	public Vector<?> getGeneratedVector(Accessor arg0, AbstractSession arg1, String arg2, int arg3) {
		return null;
	}
	
}
