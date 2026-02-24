package com.syncspace.util;

import com.syncspace.dto.common.PageResponse;
import org.springframework.data.domain.Page;

public final class PageResponseUtil {

    private PageResponseUtil() {
    }

    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
