package com.example.teachingapp;

import java.util.Objects;

public class GroupMapKey {
    private final String subjectCode;
    private final String groupCode;

    public GroupMapKey(String subjectCode, String groupCode) {
        this.subjectCode = subjectCode;
        this.groupCode = groupCode;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMapKey that = (GroupMapKey) o;
        return Objects.equals(subjectCode, that.subjectCode) && Objects.equals(groupCode, that.groupCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectCode, groupCode);
    }
}
