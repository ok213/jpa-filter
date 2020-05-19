package com.example.jpafilter.my;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MySearchCriteria {

    private String key;
    private MySearchOperation operation;
    private Object value;
    private boolean orPredicate;

    public MySearchCriteria(final String key, final MySearchOperation operation, final Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public MySearchCriteria(final String orPredicate, final String key, final MySearchOperation operation, final Object value) {
        super();
        this.orPredicate = orPredicate != null && orPredicate.equals(MySearchOperation.OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public MySearchCriteria(String key, String operation, String prefix, String value, String suffix) {
        MySearchOperation op = MySearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == MySearchOperation.EQUALITY) { // the operation may be complex operation
                final boolean startWithAsterisk = prefix != null && prefix.contains(MySearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(MySearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    op = MySearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = MySearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = MySearchOperation.STARTS_WITH;
                }
            }
        }
        this.key = key;
        this.operation = op;
        this.value = value;
    }

}
