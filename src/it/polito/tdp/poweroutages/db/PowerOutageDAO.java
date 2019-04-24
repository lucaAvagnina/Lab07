package it.polito.tdp.poweroutages.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.model.Blackout;
import it.polito.tdp.poweroutages.model.Nerc;

public class PowerOutageDAO {

	public List<Nerc> getNercList() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}

	public List<Blackout> getAllBlackoutNerc(Nerc nercScelto) {
		
		String sql = "SELECT * FROM poweroutages WHERE nerc_id = ? ORDER BY date_event_began ASC";
		List<Blackout> blackoutList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, nercScelto.getId());
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Blackout b = new Blackout(res.getInt("id"), res.getInt("nerc_id"), res.getInt("customers_affected"), res.getTimestamp("date_event_began").toLocalDateTime(), res.getTimestamp("date_event_finished").toLocalDateTime());
				blackoutList.add(b);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return blackoutList;
	}

}
