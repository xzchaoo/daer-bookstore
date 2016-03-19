package org.xzc.sshb.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State implements Serializable {
	public static final int USER_NORMAL = 100;
	public static final int USER_DELETED = 101;
	public static final int USER_ADMIN = 107;

	public static final int ITEM_NORMAL = 200;
	public static final int ITEM_DELETED = 201;

	public static final int RECORD_PAID = 300;
	public static final int RECORD_SENT = 301;
	public static final int RECORD_RECEIVED = 302;
	public static final int RECORD_FINISHED = 303;
	public static final int RECORD_CANCELED = 304;
	public static final int RECORD_DELETED = 399;

	public static final int RECORDITEM_NOT_RECEIVED = 400;
	public static final int RECORDITEM_RECEIVED = 401;
	public static final int RECORDITEM_EVALUATED = 402;
	public static final int RECORDITEM_CANCELED = 403;
	
	public static final int CHARGEBACK_OK = 500;
	
	public static final Map<String, Integer> DESCRIPTION = new HashMap<String, Integer>();
	
	static {
		DESCRIPTION.put( "USER_NORMAL", 100 );
		DESCRIPTION.put( "USER_DELETED", 101 );
		DESCRIPTION.put( "USER_ADMIN", 107 );

		DESCRIPTION.put( "ITEM_NORMAL", 200 );
		DESCRIPTION.put( "ITEM_DELETED", 201 );

		DESCRIPTION.put( "RECORD_PAID", 300 );
		DESCRIPTION.put( "RECORD_SENT", 301 );
		DESCRIPTION.put( "RECORD_RECEIVED", 302 );
		DESCRIPTION.put( "RECORD_FINISHED", 303 );
		DESCRIPTION.put( "RECORD_CANCELED", 304 );
		DESCRIPTION.put( "RECORD_DELETED", 399 );

		DESCRIPTION.put( "RECORDITEM_NOT_RECEIVED", 400 );
		DESCRIPTION.put( "RECORDITEM_RECEIVED", 401 );
		DESCRIPTION.put( "RECORDITEM_EVALUATED", 402 );
		DESCRIPTION.put( "RECORDITEM_CANCELED", 403 );
		
		DESCRIPTION.put( "CHARGEBACK_OK", 500 );
		
	}
	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		State other = (State) obj;
		if (getId() != other.getId())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}
