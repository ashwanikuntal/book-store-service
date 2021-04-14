package com.trs.bookstoreservice;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mysql.cj.jdbc.MysqlDataSource;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

	@Value("${spring.datasource.url}")
	private String url;
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	@Bean
	public MysqlDataSource dataSource() {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUrl(url);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		return dataSource;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcOperations jdbcTemplate) {
		return new NamedParameterJdbcTemplate(jdbcTemplate);
	}
}
