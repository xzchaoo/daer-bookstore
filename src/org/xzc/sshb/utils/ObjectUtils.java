package org.xzc.sshb.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.xzc.sshb.domain.Item;

public class ObjectUtils {
	public static void copyItem(Item from, Item to) {

		to.setAuthor( StringEscapeUtils.escapeHtml4( from.getAuthor() ) );
		to.setImgPath( from.getImgPath() );
		to.setLongDescription( StringEscapeUtils.escapeHtml4( from.getAuthor() ) );
		to.setName( StringEscapeUtils.escapeHtml4( from.getAuthor() ) );
		to.setPage( from.getPage() );
		to.setPrice( from.getPrice() );
		to.setRemain( from.getRemain() );
		to.setShortDescription( StringEscapeUtils.escapeHtml4( from.getShortDescription() ) );

	}
}
