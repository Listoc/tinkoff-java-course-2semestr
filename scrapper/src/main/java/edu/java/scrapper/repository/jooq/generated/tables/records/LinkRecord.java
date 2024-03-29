/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.repository.jooq.generated.tables.records;


import edu.java.scrapper.repository.jooq.generated.tables.Link;

import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class LinkRecord extends UpdatableRecordImpl<LinkRecord> implements Record3<Long, String, OffsetDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>LINK.LINK_ID</code>.
     */
    public void setLinkId(@Nullable Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>LINK.LINK_ID</code>.
     */
    @Nullable
    public Long getLinkId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>LINK.URL</code>.
     */
    public void setUrl(@NotNull String value) {
        set(1, value);
    }

    /**
     * Getter for <code>LINK.URL</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUrl() {
        return (String) get(1);
    }

    /**
     * Setter for <code>LINK.LAST_CHECK</code>.
     */
    public void setLastCheck(@NotNull OffsetDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>LINK.LAST_CHECK</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getLastCheck() {
        return (OffsetDateTime) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row3<Long, String, OffsetDateTime> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row3<Long, String, OffsetDateTime> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return Link.LINK.LINK_ID;
    }

    @Override
    @NotNull
    public Field<String> field2() {
        return Link.LINK.URL;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field3() {
        return Link.LINK.LAST_CHECK;
    }

    @Override
    @Nullable
    public Long component1() {
        return getLinkId();
    }

    @Override
    @NotNull
    public String component2() {
        return getUrl();
    }

    @Override
    @NotNull
    public OffsetDateTime component3() {
        return getLastCheck();
    }

    @Override
    @Nullable
    public Long value1() {
        return getLinkId();
    }

    @Override
    @NotNull
    public String value2() {
        return getUrl();
    }

    @Override
    @NotNull
    public OffsetDateTime value3() {
        return getLastCheck();
    }

    @Override
    @NotNull
    public LinkRecord value1(@Nullable Long value) {
        setLinkId(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value2(@NotNull String value) {
        setUrl(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value3(@NotNull OffsetDateTime value) {
        setLastCheck(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord values(@Nullable Long value1, @NotNull String value2, @NotNull OffsetDateTime value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LinkRecord
     */
    public LinkRecord() {
        super(Link.LINK);
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    @ConstructorProperties({ "linkId", "url", "lastCheck" })
    public LinkRecord(@Nullable Long linkId, @NotNull String url, @NotNull OffsetDateTime lastCheck) {
        super(Link.LINK);

        setLinkId(linkId);
        setUrl(url);
        setLastCheck(lastCheck);
        resetChangedOnNotNull();
    }
}
