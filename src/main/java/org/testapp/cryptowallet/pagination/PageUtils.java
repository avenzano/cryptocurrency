package org.testapp.cryptowallet.pagination;

import java.util.List;

public class PageUtils {

	public static <T> Page<T> createPage(List<T> data, int page, int size) {
		int offset = Math.max(0, page - 1) * size;
		// if offset goes beyond the # of deposits, return empty list
		if ( offset >= data.size()) {
			return Page.emptyPage();
		} else {
			List<T> slice = data.subList(offset, Math.min(data.size(), offset+size));
			return new Page<T>(slice, data.size(), offset, size);
		}
	}
}
