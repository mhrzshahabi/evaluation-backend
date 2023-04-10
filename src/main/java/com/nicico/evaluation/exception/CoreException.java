package com.nicico.evaluation.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum CoreException implements Message {
    NOT_FOUND {
        @Override
        public String key() {
            return this.name();
        }
    },
    ENVIRONMENT_NOT_FOUND {
        @Override
        public String key() {
            return this.name();
        }
    },
    INTERNAL_SERVER {
        @Override
        public String key() {
            return this.name();
        }
    },
    SERVER_DENY {
        @Override
        public String key() {
            return this.name();
        }
    },
    ACCESS_DENIED {
        @Override
        public String key() {
            return this.name();
        }
    },
    NOT_SAVE {
        @Override
        public String key() {
            return this.name();
        }
    },
    NOT_UPDATE {
        @Override
        public String key() {
            return this.name();
        }
    },
    NOT_DELETE {
        @Override
        public String key() {
            return this.name();
        }
    },
    NOT_IMPLEMENT {
        @Override
        public String key() {
            return this.name();
        }
    },
    FILE_NOT_FOUND {
        @Override
        public String key() {
            return this.name();
        }
    },
    GRADE_IS_IN_ANOTHER_GROUP {
        @Override
        public String key() {
            return this.name();
        }
    },
}
