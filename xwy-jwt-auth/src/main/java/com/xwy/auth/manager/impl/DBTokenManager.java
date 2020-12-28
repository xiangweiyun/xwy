package com.xwy.auth.manager.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.xwy.common.exception.XwyException;

/**
 * 使用MySQL存储Token
 * 
 * @author xiangwy
 * @date: 2020-12-02 10:17:02
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@Component
@ConditionalOnProperty(name = "ly.token.store", havingValue = "mysql")
public class DBTokenManager extends AbstractTokenManager {

	/**
	 * 数据源
	 */
	@Autowired
	protected DataSource dataSource;

	/**
	 * 存放鉴权信息的表名
	 */
	@Value("${ly.token.store.tablename:ly_sys_token}")
	protected String tableName;

	/**
	 * 存放Key的字段名
	 */
	@Value("${ly.token.store.keycolumnname:tkey}")
	protected String keyColumnName;

	/**
	 * 存放Token的字段名
	 */
	@Value("${ly.token.store.tokenCloumnname:token}")
	protected String tokenColumnName;

	/**
	 * 存放过期时间的字段名
	 */
	@Value("${ly.token.store.expireColumnName:expire}")
	protected String expireColumnName;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setKeyColumnName(String keyColumnName) {
		this.keyColumnName = keyColumnName;
	}

	public void setTokenColumnName(String tokenColumnName) {
		this.tokenColumnName = tokenColumnName;
	}

	public void setExpireExpireColumnName(String expireColumnName) {
		this.expireColumnName = expireColumnName;
	}

	@Override
	public void delSingleRelationshipByKey(String key) {
		String sql = String.format("delete from %s where %s = ?", tableName, keyColumnName);
		update(sql, key);
	}

	@Override
	public void delRelationshipByToken(String token) {
		String sql = String.format("delete from %s where %s = ?", tableName, tokenColumnName);
		update(sql, token);
	}

	@Override
	protected void createMultipleRelationship(String key, String token) {
		String sql = String.format("insert into %s (%s, %s, %s) values(?, ?, ?)", tableName, keyColumnName,
				tokenColumnName, expireColumnName);
		update(sql, key, token, new Timestamp(System.currentTimeMillis() + tokenExpireSeconds * 1000));
	}

	@Override
	protected void createSingleRelationship(String key, String token) {
		String select = String.format("select count(*) from %s where %s = ?", tableName, keyColumnName);
		System.out.println(select);
		Number count = query(Number.class, select, key);
		if (count != null && count.intValue() > 0) {
			String sql = String.format("update %s set %s = ?, %s = ? where %s = ?", tableName, tokenColumnName,
					expireColumnName, keyColumnName);
			update(sql, token, new Timestamp(System.currentTimeMillis() + tokenExpireSeconds * 1000), key);
		} else {
			String sql = String.format("insert into %s (%s, %s, %s) values(?, ?, ?)", tableName, keyColumnName,
					tokenColumnName, expireColumnName);
			update(sql, key, token, new Timestamp(System.currentTimeMillis() + tokenExpireSeconds * 1000));
		}
	}

	@Override
	public String getKeyByToken(String token) {
		String sql = String.format("select %s from %s where %s = ? and %s > ? limit 1", keyColumnName, tableName,
				tokenColumnName, expireColumnName);
		return query(String.class, sql, token, new Timestamp(System.currentTimeMillis()));
	}

	@Override
	protected void flushExpireAfterOperation(String key, String token) {
		String flushExpireAtSql = String.format("update %s set %s = ? where %s = ?", tableName, expireColumnName,
				tokenColumnName);
		update(flushExpireAtSql, new Timestamp(System.currentTimeMillis() + tokenExpireSeconds * 1000), token);
	}

	private void update(String sql, Object... args) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			int i = 1;
			for (Object arg : args) {
				statement.setObject(i++, arg);
			}
			statement.execute();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new XwyException("token sql 添加或更新操作异常！", 1);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T query(Class<T> clazz, String sql, Object... args) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			int i = 1;
			for (Object arg : args) {
				statement.setObject(i++, arg);
			}
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Object obj = resultSet.getObject(1);
				if (clazz.isInstance(obj)) {
					return (T) obj;
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new XwyException("token sql 添加或更新操作异常！", 1);
		}
		return null;
	}
}