package com.example.hospitalClinical.common.code;

import java.io.Serializable;
import java.util.Objects;

public class CodeDetailId implements Serializable {

    private String groupCode;
    private String code;

    public CodeDetailId() {
    }

    public CodeDetailId(String groupCode, String code) {
        this.groupCode = groupCode;
        this.code = code;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeDetailId that = (CodeDetailId) o;
        return Objects.equals(groupCode, that.groupCode) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupCode, code);
    }
}

