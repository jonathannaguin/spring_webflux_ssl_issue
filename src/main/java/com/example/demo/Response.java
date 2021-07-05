package com.example.demo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Response
{
    private String status;

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("status", status)
                                                                  .toString();
    }

    public static final class Builder
    {
        private String status;

        Builder() {}

        public static Builder builder() { return new Builder(); }

        public Builder status(String status)
        {
            this.status = status;
            return this;
        }

        public Response build()
        {
            Response response = new Response();
            response.status = this.status;
            return response;
        }
    }
}
