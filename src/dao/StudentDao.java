package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entity.Student;
public class StudentDao{

private SessionFactory sessionFactory;

// 查询所有的信息
@SuppressWarnings("unchecked")
 public List<Student> findAllStudent() {
	sessionFactory = new Configuration().configure().buildSessionFactory();
	Session session = sessionFactory.openSession();
	String hql = "from Student as student";
	List<Student> students = null;
	try {
		students = session.createQuery(hql).list();
	} catch (RuntimeException re) {
		re.printStackTrace();
	}
	session.close();
	return students;
}

public SessionFactory getSessionFactory() {
	return sessionFactory;
}

public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
}
}