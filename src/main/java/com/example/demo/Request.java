package com.example.demo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Request
{
    private String fieldA;
    private String fieldB;

    public String getFieldA()
    {
        return fieldA;
    }

    public void setFieldA(String fieldA)
    {
        this.fieldA = fieldA;
    }

    public String getFieldB()
    {
        return fieldB;
    }

    public void setFieldB(String fieldB)
    {
        this.fieldB = fieldB;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("fieldA", fieldA)
                                                                  .append("fieldB", fieldB)
                                                                  .toString();
    }
}
