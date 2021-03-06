package model.dao;
/**
 * user와 account 테이블을 조인해서 사용하는 DAO클래스입니다.
 * 아이디와 암호를 이욯해서 로그인하면
 * 계좌 목록(계좌유형,계좌번호,잔액)이 나오도록 합니다.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.bean.AccountBean;

public class AccountListDAO {
	private PreparedStatement stmt;
	private Connection conn=ConnectionDAO.getInstance().getConn();
	ResultSet rs;
	
	public AccountListDAO() {//디폴트생성자
		
	}
	public int loginProcess(String id, String pw) {
		String sql=" select * from user where id=? and pw=? ";
		try {
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.setString(2, pw);
			rs=stmt.executeQuery();
			if(rs.next()) {
				return 1; 
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	public List<AccountBean> accountList(String id){
		String sql=" select u.userNo, a.type, a.accNo, a.amount from user u, account a "
				+ "  where u.id=? and u.userNo=a.userNo  ";
		List<AccountBean> accounts=null;
		
		try {
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, id);
			rs=stmt.executeQuery();
			
			accounts=new ArrayList<AccountBean>();
			while(rs.next()) {
				AccountBean account=new AccountBean(rs.getString("userNo"), rs.getString("accNo"), rs.getInt("amount"), rs.getString("type"));
				accounts.add(account);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return accounts;
	}
	@Override
	protected void finalize() throws Throwable {
		try {
			if(stmt!=null) stmt.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
		super.finalize();
	}
	
}
