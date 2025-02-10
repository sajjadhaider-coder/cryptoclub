package com.rasikhoons.cryptoclub.response;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class PaginationResponse {

    private int code;    // HTTP status or custom code
    private String msg;  // Message (could be success or error message)
    private Object data;      // Generic data (could be any object)
    private Object pagination;

    public PaginationResponse(int code, String msg, Object data, Object pagination) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.pagination = pagination;
    }

    @Data
    public static class PaginationDetail {
        private int currentPage;
        private long totalPages;
        private int pageSize;
        private long totalItems;

        public PaginationDetail(int currentPage, long totalPages, int pageSize, long totalItems) {
            super();
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.pageSize = pageSize;
            this.totalItems = totalItems;
        }
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("currentPage", currentPage);
            map.put("totalPages", totalPages);
            map.put("pageSize", pageSize);
            map.put("totalItems", totalItems);
            return map;
        }
    }
}
