package com.roundtable.roundtable.global.exception;

public class CategoryException extends ApplicationException {
    public CategoryException(String message) {
        super(message);
    }

    public static class CategoryDuplicatedException extends CategoryException {

        public CategoryDuplicatedException(String name) {
            super(name + "에 해당하는 cateogy가 이미 존재합니다.");
        }
    }

    public static class CategoryNotFoundException extends CategoryException {

        public CategoryNotFoundException() {
            super("카테고리가 존재하지 않습니다.");
        }
    }

}
