package com.mariakamachine.dentoice.util.invoice;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.time.LocalDate.now;
import static java.util.Collections.sort;

@Slf4j
public class InvoiceIdGenerator implements IdentifierGenerator {

    private static final String SELECT_ID_QUERY = "SELECT id FROM invoices WHERE id::TEXT LIKE ?";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        LocalDate today = now();
        String year = getYear(today);
        String month = getMonth(today);
        return Long.valueOf(format("%s%s%s", year, month, getNextRunningId(session, year, month)));
    }

    private String getYear(LocalDate today) {
        return valueOf(today.getYear()).substring(2);
    }

    private String getMonth(LocalDate today) {
        String month = valueOf(today.getMonthValue());
        return month.length() == 2 ? month : "0".concat(month);
    }

    private String getNextRunningId(SharedSessionContractImplementor session, String year, String month) {
        List<Long> ids = getIdsFromDatabase(session, year + month);
        if (ids.isEmpty()) {
            return "0001";
        }
        sort(ids);
        return incrementRunningId(ids.get(ids.size() - 1));
    }

    private List<Long> getIdsFromDatabase(SharedSessionContractImplementor session, String idPrefix) {
        List<Long> ids = new ArrayList<>();
        try {
            Connection connection = session.connection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ID_QUERY);
            statement.setString(1, format("%s%%", idPrefix));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getLong("id"));
            }
        } catch (SQLException sql) {
            log.error("could not retrieve id(s) from database", sql);
            throw new RuntimeException("could not retrieve id(s) from database", sql);
        }
        return ids;
    }

    private String incrementRunningId(Long id) {
        String runningId;
        try {
            runningId = valueOf(id + 1).substring(4);
        } catch (NumberFormatException e) {
            log.error("failed to convert id to integer", e);
            throw new RuntimeException("failed to convert id to integer", e);
        }
        return runningId;
    }

}
