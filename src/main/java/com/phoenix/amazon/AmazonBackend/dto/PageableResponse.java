package com.phoenix.amazon.AmazonBackend.dto;

import java.util.List;

public class PageableResponse<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final int totalPages;
    private final long totalElements;
    private final boolean isLastPage;

    public PageableResponse(Builder<T> tBuilder) {
        this.content = tBuilder.content;
        this.pageNumber = tBuilder.pageNumber;
        this.pageSize = tBuilder.pageSize;
        this.totalPages = tBuilder.totalPages;
        this.totalElements = tBuilder.totalElements;
        this.isLastPage = tBuilder.isLastPage;
    }

    public static final class Builder<T> {
        private List<T> content;
        private int pageNumber;
        private int pageSize;
        private int totalPages;
        private long totalElements;
        private boolean isLastPage;

        public Builder() {
        }

        public Builder<T> content(final List<T> content) {
            this.content = content;
            return this;
        }

        public Builder<T> pageNumber(final int pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public Builder<T> pageSize(final int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder<T> totalPages(final int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder<T> totalElements(final long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public Builder<T> isLastPage(final boolean isLastPage) {
            this.isLastPage = isLastPage;
            return this;
        }

        public PageableResponse<T> build() {
            return new PageableResponse<T>(this);
        }
    }

    public List<T> getContent() {
        return content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public boolean isLastPage() {
        return isLastPage;
    }
}
