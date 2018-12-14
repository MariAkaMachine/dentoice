package com.mariakamachine.dentoice.config.postgres;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariakamachine.dentoice.data.entity.CostWrapperEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationException;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.String.format;
import static java.sql.Types.JAVA_OBJECT;

@Slf4j
public class CostWrapperEntityJsonbUserType implements UserType {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public int[] sqlTypes() {
        return new int[]{JAVA_OBJECT};
    }

    @Override
    public Class<CostWrapperEntity> returnedClass() {
        return CostWrapperEntity.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == null || y == null) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        try {
            PGobject pGobject = (PGobject) resultSet.getObject(names[0]);
            return MAPPER.readValue(pGobject.getValue(), CostWrapperEntity.class);
        } catch (NullPointerException | IOException e) {
            log.error("could not map value ['{}'] to entity", resultSet.getObject(names[0]), e);
            throw new HibernateException(e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement statement, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        try {
            final String json = MAPPER.writeValueAsString(value);
            PGobject pGobject = new PGobject();
            pGobject.setType("jsonb");
            pGobject.setValue(json);
            statement.setObject(index, pGobject);
        } catch (NullPointerException | JsonProcessingException e) {
            log.error("could not write {} to database", value);
            throw new HibernateException(e);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(value);
            objectOutputStream.flush();
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                return objectInputStream.readObject();
            }
        } catch (Exception e) {
            log.error("failed to make deep copy of {}", value);
            throw new HibernateException(e);
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        Object deepCopy = deepCopy(value);
        if (!(deepCopy instanceof Serializable)) {
            log.warn("{} is not a serializable class");
            throw new SerializationException(format("%s is not a serializable class", value));
        }
        return (Serializable) deepCopy;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

}
