package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entity.Chart;

public class ChartDao {
	private SessionFactory sessionFactory;

	// 查询所有的信息
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Chart> findAllChart() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		String hql = "from Chart as chart";
		List<Chart> chartes = null;
		try {
			chartes = session.createQuery(hql).list();
		} catch (RuntimeException re) {
			re.printStackTrace();
		}
		session.close();
		return chartes;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
