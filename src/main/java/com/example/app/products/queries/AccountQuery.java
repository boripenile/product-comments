package com.example.app.products.queries;

import java.sql.Timestamp;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import com.example.app.products.account.Account;
import com.example.app.products.jooq.ProductsDownload;
import com.example.app.products.util.ConnectionUtil;

public enum AccountQuery {
	INSTANCE;
	
	public boolean exists(String email) {
		Integer count = 0;
		try (DSLContext context = DSL.using(ConnectionUtil.INSTANCE.getConfiguration())) {
			count = context
					.select(DSL.count(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT.EMAIL))
					.from(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT)
					.where(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT.EMAIL.eq(email))
					.fetchOne().into(Integer.class);
			if (count != null) {
				if (count == 0) {
					return false;
				} else {
					return true;
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Long findId() {
		Long count = 0L;
		try (DSLContext context = DSL.using(ConnectionUtil.INSTANCE.getConfiguration())) {
			count = context
					.select(DSL.max(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT.ID))
					.from(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT)
					.fetchOne().into(Long.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public Account findAccountById(Long id) {
		Account account = null;
		try (DSLContext context = DSL.using(ConnectionUtil.INSTANCE.getConfiguration())) {
			account = context
					.select().from(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT)
					.where(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT.ID.eq(id))
					.fetchOne().into(Account.class);
			return account;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Account findAccountByEmail(String email) {
		Account account = null;
		try (DSLContext context = DSL.using(ConnectionUtil.INSTANCE.getConfiguration())) {
			account = context
					.select().from(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT)
					.where(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT.EMAIL.eq(email))
					.fetchOne().into(Account.class);
			return account;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Account saveAccount(Account account) {
		Account acct = null;
		try (DSLContext context = DSL.using(ConnectionUtil.INSTANCE.getConfiguration())) {			
			context
					.insertInto(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT)
					.set(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT.ID, 
							 account.getId())
					.set(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT.EMAIL, account.getEmail())
					.set(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT.PASSWORD, account.getPassword())
					.set(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT.ROLE, account.getRole())
					.set(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT.CREATED, 
							new Timestamp(System.currentTimeMillis()))
					.execute();
			
			acct = context
					.select().from(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT)
					.where(ProductsDownload.PRODUCTS_DOWNLOAD.ACCOUNT.ID.eq(account.getId()))
					.fetchOne().into(Account.class);

			return acct;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
